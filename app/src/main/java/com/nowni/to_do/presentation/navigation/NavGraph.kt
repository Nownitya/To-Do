package com.nowni.to_do.presentation.navigation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.room.Room
import com.nowni.to_do.data.local.database.AppDatabase
import com.nowni.to_do.data.reminder.ReminderScheduler
import com.nowni.to_do.data.repository.TaskRepositoryImpl
import com.nowni.to_do.domain.usecase.GetTasksUseCase
import com.nowni.to_do.presentation.task.TaskEvent
import com.nowni.to_do.presentation.task.TaskViewModel
import com.nowni.to_do.presentation.task.ui.AddEditTaskScreen
import com.nowni.to_do.presentation.task.ui.TaskListScreen


@Composable
fun AppNavGraph() {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(Home)
    val taskListState = rememberLazyListState()

    val context = LocalContext.current

    val database = remember {
        Room.databaseBuilder(
            context, AppDatabase::class.java, "todo_database"
        ).build()
    }

    val repository = remember {
        TaskRepositoryImpl(database.taskDao())
    }

    val getTasksUseCase = remember {
        GetTasksUseCase(repository)
    }

    val scheduler = remember {
        ReminderScheduler(context)
    }

    val viewModel: TaskViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(
                getTasksUseCase = getTasksUseCase, repository = repository, scheduler = scheduler
            ) as T
        }
    })

    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
        entry<Home> {

            val state by viewModel.state.collectAsStateWithLifecycle()

            TaskListScreen(
                tasks = state.tasks,
                listState = taskListState,
                viewModel = viewModel, // check and correct this.
                searchQuery = state.searchQuery,
                onSearchQueryChange = { viewModel.onEvent(TaskEvent.SearchTask(it)) },
                isLoading = state.isLoading,
                error = state.error,
                onAddTask = {
                    backStack.add(AddEditTask())
                },
                onEditTask = { taskId ->
                    backStack.add(AddEditTask(taskId))
                },
                onDeleteTask = { taskId ->
                    viewModel.onEvent(TaskEvent.DeleteTask(taskId))
                },

                onToggleTask = { taskId ->
                    viewModel.onEvent(TaskEvent.ToggleTask(taskId))
                },

                )
        }
        entry<AddEditTask> { key ->

            AddEditTaskScreen(
                taskId = key.taskId,
                viewModel = viewModel,
                onSave = { backStack.removeLastOrNull() },
                onCancel = { backStack.removeLastOrNull() })
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider,
    )
}
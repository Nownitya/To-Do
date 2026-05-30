package com.nowni.to_do.presentation.navigation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nowni.to_do.presentation.task.TaskEvent
import com.nowni.to_do.presentation.task.TaskViewModel
import com.nowni.to_do.presentation.task.ui.AddEditTaskScreen
import com.nowni.to_do.presentation.task.ui.TaskListScreen


@Composable
fun AppNavGraph(
    onThemeToggle:()-> Unit
) {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(Home)
    val taskListState = rememberLazyListState()

    val viewModel: TaskViewModel = hiltViewModel()

    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
        entry<Home> {

            val state by viewModel.state.collectAsStateWithLifecycle()

            TaskListScreen(
                tasks = state.tasks,
                listState = taskListState,
                viewModel = viewModel,
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
                onThemeToggle = onThemeToggle

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
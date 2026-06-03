package com.nowni.to_do.presentation.navigation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nowni.to_do.MainActivity
import com.nowni.to_do.presentation.task.TaskEvent
import com.nowni.to_do.presentation.task.TaskViewModel
import com.nowni.to_do.presentation.task.ui.AddEditTaskScreen
import com.nowni.to_do.presentation.task.ui.SettingsScreen
import com.nowni.to_do.presentation.task.ui.TaskListScreen
import com.nowni.to_do.presentation.theme.ThemeViewModel


@Composable
fun AppNavGraph() {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(Home)
    val taskListState = rememberLazyListState()

    val viewModel: TaskViewModel = hiltViewModel()
    val themeViewModel: ThemeViewModel = hiltViewModel()

    val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()

    val notificationTaskId = MainActivity.notificationTaskId.longValue

    var highlightedTaskId by remember {
        mutableLongStateOf(-1L)
    }

    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
        entry<Home> {

            val state by viewModel.state.collectAsStateWithLifecycle()
            LaunchedEffect(
                notificationTaskId, state.tasks
            ) {
                if (notificationTaskId == -1L) return@LaunchedEffect
                val taskIndex = state.tasks.indexOfFirst {
                    it.id == notificationTaskId
                }
                if (taskIndex >= 0) {
                    taskListState.animateScrollToItem(
                        index = taskIndex, scrollOffset = -100
                    )
                    highlightedTaskId = notificationTaskId
                    kotlinx.coroutines.delay(2500)
                    highlightedTaskId = -1L
                    MainActivity.notificationTaskId.longValue = -1L
                }
            }



            TaskListScreen(
                tasks = state.tasks,
                listState = taskListState,
                highlightedTaskId = highlightedTaskId,
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
                onSettingsClick = {
                    backStack.add(Settings)
                })
        }
        entry<AddEditTask> { key ->
            AddEditTaskScreen(
                taskId = key.taskId,
                viewModel = viewModel,
                onSave = { backStack.removeLastOrNull() },
                onCancel = { backStack.removeLastOrNull() })
        }

        entry<Settings> {
            SettingsScreen(themeMode = themeMode, onThemeSelected = { selectedTheme ->
                themeViewModel.setTheme(selectedTheme)
            }, onBack = { backStack.removeLastOrNull() })

        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider,
    )
}
package com.nowni.to_do.presentation.task.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.presentation.task.TaskEvent
import com.nowni.to_do.presentation.task.TaskUiEvent
import com.nowni.to_do.presentation.task.TaskViewModel
import com.nowni.to_do.presentation.task.ui.component.EmptyTaskList
import com.nowni.to_do.presentation.task.ui.component.ExpandableSearchAppBar
import com.nowni.to_do.presentation.task.ui.component.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    listState: LazyListState,
    viewModel: TaskViewModel,
    isLoading: Boolean,
    error: String?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onToggleTask: (Long) -> Unit,
    onSettingsClick: () -> Unit
) {

    var previousSize by remember {
        mutableIntStateOf(tasks.size)
    }
    LaunchedEffect(tasks.size) {
        if (tasks.isNotEmpty() && tasks.size > previousSize) {
//            listState.scrollToItem(0)
            listState.animateScrollToItem(0)
        }
        previousSize = tasks.size

    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TaskUiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TaskEvent.UndoDelete)
                    }
                }

                TaskUiEvent.NavigateBack -> {
                    // No action needed on task list screen.
                }
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            ExpandableSearchAppBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSettingsClick = onSettingsClick
            )

        }, floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "Add Task"
                )
            }
        }, content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading && tasks.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Text(text = error)
                        }
                    }

                    /*tasks.isEmpty() -> {
                        EmptyTaskList(
                            modifier = Modifier.fillMaxSize()
                        )
                    }*/

                    tasks.isEmpty()-> {
                        if (searchQuery.isBlank()) {
                            EmptyTaskList(
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center){
                                Text(text = "No tasks found.")
                            }

                        }
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = tasks,
                                key = { task -> task.id },
                            ) { task ->
                                val dismissState = rememberSwipeToDismissBoxState(
                                    positionalThreshold = { distance ->
                                        distance * 0.5f
                                    })

                                /*LaunchedEffect(dismissState.currentValue) {
                                    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                        onDeleteTask(task.id)
                                    }
                                }*/

                                if (dismissState.currentValue== SwipeToDismissBoxValue.EndToStart){
                                    LaunchedEffect(task.id) {
                                        onDeleteTask(task.id)
                                    }
                                }
                                /*val color = when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                    else -> MaterialTheme.colorScheme.surface
                                }*/

                                val backgroundColor by animateColorAsState(
                                    targetValue = when (dismissState.targetValue){
                                        SwipeToDismissBoxValue.EndToStart ->
                                            MaterialTheme.colorScheme.errorContainer
                                        else -> MaterialTheme.colorScheme.surface
                                    },
                                    label = "SwipeBackgroundColor"
                                )
                                SwipeToDismissBox(
                                    state = dismissState,
                                    enableDismissFromStartToEnd = false,
                                    backgroundContent = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 16.dp)
                                                .background(backgroundColor),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    },
                                    content = {
                                        TaskItem(
                                            modifier = Modifier.animateItem(),
                                            task = task,
                                            onToggleTask = onToggleTask,
                                            onDeleteTask = { onDeleteTask(task.id) },
                                            onTaskClick = { onEditTask(task.id) })
                                    })
                            }
                        }
                    }
                }
            }


        })

}
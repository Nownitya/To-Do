package com.nowni.to_do.presentation.task.ui

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.presentation.task.ui.component.EmptyTaskList
import com.nowni.to_do.presentation.task.ui.component.ExpandableSearchAppBar
import com.nowni.to_do.presentation.task.ui.component.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    listState: LazyListState,
    isLoading: Boolean,
    error: String?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onToggleTask: (Long) -> Unit,
) {

    var previousSize by remember { mutableStateOf(tasks.size) }
    LaunchedEffect(tasks.size) {
        if (tasks.size > previousSize) {
            listState.scrollToItem(0)
        }
        previousSize = tasks.size

    }

    Scaffold(
        topBar = {
        ExpandableSearchAppBar(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange
        )

    },
        floatingActionButton = {
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
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = error)
                        }
                    }

                    tasks.isEmpty() -> {
                        EmptyTaskList(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = tasks,
                                key = { task -> task.id },
                            ) { task ->
                                TaskItem(
                                    task = task,
                                    onToggleTask = { onToggleTask(task.id) },
                                    onDeleteTask = { onDeleteTask(task.id) },
                                    onTaskClick = { onEditTask(task.id) })
                            }
                        }
                    }
                }
            }


        })

}
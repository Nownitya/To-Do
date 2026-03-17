package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nowni.to_do.core.ui.theme.ToDoTheme
import com.nowni.to_do.core.utils.COMPLETED_TASK_ALPHA
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.model.TaskPriority

@Composable
fun TaskItem(
    task: Task,
    onToggleTask: (Long) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedAlpha = if (task.isCompleted) COMPLETED_TASK_ALPHA else 1f
    val textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task.id) },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
                    .toggleable(
                        value = task.isCompleted,
                        onValueChange = { onToggleTask(task.id) },
                        role = Role.Checkbox
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = null // Handled by the parent Row's toggleable
                )
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 8.dp)
                        .graphicsLayer(alpha = completedAlpha)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = textDecoration,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis

                    )
                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            textDecoration = textDecoration,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            IconButton(
                onClick = { onDeleteTask(task.id) }, modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task: ${task.title}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemNotCompletedPreview() {
    ToDoTheme {
        TaskItem(
            task = Task(
                id = 1,
                title = "Buy Groceries",
                priority = TaskPriority.MEDIUM,
                description = "Milk, Eggs, Bread",
                isCompleted = false
            ), onToggleTask = {}, onDeleteTask = {}, onTaskClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemCompletedPreview() {
    ToDoTheme {
        TaskItem(
            task = Task(
                id = 2,
                title = "Clean the house",
                priority = TaskPriority.LOW,
                description = "Vacuum and dust",
                isCompleted = true
            ), onToggleTask = {}, onDeleteTask = {}, onTaskClick = {})
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun TaskItemPreview() {
    ToDoTheme {
        val tasks = remember {
            mutableStateListOf(
                Task(
                    id = 1,
                    title = "Buy Groceries",
                    priority = TaskPriority.MEDIUM,
                    description = "Milk, Eggs, Bread",
                    isCompleted = false
                ), Task(
                    id = 2,
                    title = "Clean the house",
                    priority = TaskPriority.LOW,
                    description = "Vacuum and dust",
                    isCompleted = true
                ), Task(
                    id = 3,
                    title = "Important Meeting",
                    priority = TaskPriority.HIGH,
                    description = "Discuss project roadmap",
                    isCompleted = false
                ), Task(
                    id = 4,
                    title = "Gym Session",
                    priority = TaskPriority.MEDIUM,
                    isCompleted = false
                ), Task(
                    id = 5,
                    title = "Study for exams",
                    priority = TaskPriority.HIGH,
                    description = "Focus on Chapter 5 to 10",
                    isCompleted = true
                ), Task(
                    id = 6, title = "Walk the dog", priority = TaskPriority.LOW, isCompleted = false
                ), Task(
                    id = 7,
                    title = "Plan summer vacation",
                    priority = TaskPriority.MEDIUM,
                    description = "Check hotels and flights",
                    isCompleted = false
                )
            )
        }
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tasks.forEach { task ->
                    TaskItem(task = task, onToggleTask = { id ->
                        val index = tasks.indexOfFirst { it.id == id }
                        if (index != -1) {
                            tasks[index] =
                                tasks[index].copy(isCompleted = !tasks[index].isCompleted)
                        }
                    }, onDeleteTask = { id -> tasks.removeIf { it.id == id } }, onTaskClick = {})
                }
            }
        }
    }
}
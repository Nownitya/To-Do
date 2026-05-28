package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nowni.to_do.core.ui.theme.ToDoTheme
import com.nowni.to_do.core.utils.COMPLETED_TASK_ALPHA
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.model.TaskPriority
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onToggleTask: (Long) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,

    ) {
    val completedAlpha = if (task.isCompleted) COMPLETED_TASK_ALPHA else 1f
    val textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None

    val dueDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val reminderFormatter = DateTimeFormatter.ofPattern("dd MMM • hh:mm a")


    val today = LocalDate.now()

    val dueDateColor = when {

        task.isCompleted -> MaterialTheme.colorScheme.onSurfaceVariant

        task.dueDate == null -> MaterialTheme.colorScheme.primary

        task.dueDate.isBefore(today) -> MaterialTheme.colorScheme.error

        task.dueDate == today -> MaterialTheme.colorScheme.tertiary

        else -> MaterialTheme.colorScheme.primary
    }



    Surface(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
//            .clickable(onClickLabel = "Open task") {
//                onTaskClick(task.id)
//            },
        ,
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
                    .graphicsLayer(alpha = completedAlpha),
                /*.toggleable(
                    value = task.isCompleted,
                    onValueChange = { onToggleTask(task.id) },
                    role = Role.Checkbox
                ),*/
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .semantics {
                            stateDescription = if (task.isCompleted) {
                                "Completed"
                            } else {
                                "Pending"
                            }
                        },
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleTask(task.id) }
                )
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 8.dp)
                        .clickable(onClickLabel = "Open task") {
                            onTaskClick(task.id)
                        },
                    verticalArrangement = Arrangement.spacedBy(4.dp)
//                        .graphicsLayer(alpha = completedAlpha)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = textDecoration,
                        maxLines = 1,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis,
                        color = if (task.isCompleted) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
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

                    Row(
//                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = when (task.priority) {
                                        TaskPriority.HIGH -> MaterialTheme.colorScheme.errorContainer

                                        TaskPriority.MEDIUM -> MaterialTheme.colorScheme.tertiaryContainer

                                        TaskPriority.LOW -> MaterialTheme.colorScheme.primaryContainer
                                    }, shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = when (task.priority) {
                                    TaskPriority.HIGH -> "High"
                                    TaskPriority.MEDIUM -> "Medium"
                                    TaskPriority.LOW -> "Low"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = when (task.priority) {
                                    TaskPriority.HIGH -> MaterialTheme.colorScheme.onErrorContainer

                                    TaskPriority.MEDIUM -> MaterialTheme.colorScheme.onTertiaryContainer

                                    TaskPriority.LOW -> MaterialTheme.colorScheme.onPrimaryContainer
                                }
                            )
                        }
                        task.dueDate?.let { dueDate ->
                            Text(
                                text = "Due: ${
                                    dueDate.format(dueDateFormatter)
                                }",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (task.isCompleted) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    dueDateColor
                                }
                            )
                        }
                    }

                    task.reminderDateTime?.let { reminder ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(14.dp),
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = if (task.isCompleted) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.secondary
                                }
                            )
                            Text(
                                text = reminder.format(reminderFormatter),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (task.isCompleted) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.secondary
                                }
                            )
                        }
                    }

                }
            }
            IconButton(
                onClick = { onDeleteTask(task.id) }, modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ${task.title}",
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
                    isCompleted = false,
                    dueDate = LocalDate.now().plusDays(1)
                ), Task(
                    id = 2,
                    title = "Clean the house",
                    priority = TaskPriority.LOW,
                    description = "Vacuum and dust",
                    isCompleted = true,
                    dueDate = LocalDate.now().minusDays(5),
                    reminderDateTime = LocalDateTime.now().minusDays(4).plusHours(12)
                        .plusMinutes(20).plusSeconds(30)
                ), Task(
                    id = 3,
                    title = "Important Meeting",
                    priority = TaskPriority.HIGH,
                    description = "Discuss project roadmap",
                    isCompleted = false,
                    dueDate = LocalDate.now().minusDays(5),
                    reminderDateTime = LocalDateTime.of(
                        2026, 5, 10, 12, 20, 30
                    )

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
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
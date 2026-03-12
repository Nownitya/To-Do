package com.nowni.to_do.presentation.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.model.TaskPriority
import com.nowni.to_do.presentation.task.TaskEvent
import com.nowni.to_do.presentation.task.TaskViewModel
import com.nowni.to_do.presentation.task.ui.component.DescriptionInput
import com.nowni.to_do.presentation.task.ui.component.DueDatePicker
import com.nowni.to_do.presentation.task.ui.component.PrioritySelector
import com.nowni.to_do.presentation.task.ui.component.ReminderDateTimePicker
import com.nowni.to_do.presentation.task.ui.component.ReminderToggle
import com.nowni.to_do.presentation.task.ui.component.SaveTaskButton
import com.nowni.to_do.presentation.task.ui.component.TitleInput
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: Long?,
    viewModel: TaskViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val existingTask = taskId?.let { id ->
        state.tasks.firstOrNull { it.id == id }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var dueDate by remember { mutableStateOf<LocalDate?>(null) }
    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    val isEditMode = taskId != null
    val screenTitle = if (isEditMode) "Edit Task" else "Add Task"



    LaunchedEffect(existingTask) {
        existingTask?.let {
            title = it.title
            description = it.description
            priority = it.priority
            dueDate = it.dueDate
            reminderDateTime = it.reminderDate?.atStartOfDay()
            reminderEnabled = it.reminderDate != null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = screenTitle) }, navigationIcon = {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Cancel"
                    )
                }
            })
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TitleInput(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth()
                )
                DescriptionInput(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth()
                )
                PrioritySelector(
                    selected = priority,
                    onPrioritySelected = { priority = it },
                )

                DueDatePicker(
                    date = dueDate, onClick = {
                        // TODO: open date picker dialog and update dueDate
                    })

                ReminderToggle(
                    enabled = reminderEnabled, onToggle = { enabled ->
                        reminderEnabled = enabled
                        if (!enabled) reminderDateTime = null
                    })

                if (reminderEnabled) {
                    ReminderDateTimePicker(
                        dateTime = reminderDateTime, onClick = {
                            // TODO: open date picker dialog and update reminderDateTime
                        })
                }

                Spacer(Modifier.weight(1f))

                SaveTaskButton(
                    enabled = title.isNotBlank(), onClick = {
                        val task = Task(
                            id = existingTask?.id ?: 0L,
                            title = title.trim(),
                            description = description.trim(),
                            priority = priority,
                            isCompleted = existingTask?.isCompleted ?: false,
                            dueDate = dueDate,
                            reminderDate = reminderDateTime?.toLocalDate()
                        )

                        if (isEditMode) {
                            viewModel.onEvent(TaskEvent.UpdateTask(task))
                        } else {
                            viewModel.onEvent(TaskEvent.AddTask(task))
                        }
                        onSave()
                    }, modifier = Modifier.fillMaxWidth()
                )
            }
        })
}



/*
@Preview(showBackground = true)
@Composable
private fun AddEditTaskPreview() {
    ToDoTheme {
        AddEditTaskScreen(
            taskId = 1L, onSave = {}, onCancel = {},
            viewModel = TODO()
        )

    }
}*/

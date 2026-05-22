package com.nowni.to_do.presentation.task.ui

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.model.TaskPriority
import com.nowni.to_do.presentation.task.TaskEvent
import com.nowni.to_do.presentation.task.TaskUiEvent
import com.nowni.to_do.presentation.task.TaskViewModel
import com.nowni.to_do.presentation.task.ui.component.DescriptionInput
import com.nowni.to_do.presentation.task.ui.component.DueDatePicker
import com.nowni.to_do.presentation.task.ui.component.PrioritySelector
import com.nowni.to_do.presentation.task.ui.component.ReminderDateTimePicker
import com.nowni.to_do.presentation.task.ui.component.ReminderToggle
import com.nowni.to_do.presentation.task.ui.component.SaveTaskButton
import com.nowni.to_do.presentation.task.ui.component.TitleInput
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

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
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TaskUiEvent.NavigateBack -> onSave()
                else -> Unit
            }
        }
    }

    val existingTask = taskId?.let { id ->
        state.tasks.firstOrNull { it.id == id }
    }

    var isInitialized by remember(taskId) { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var dueDate by remember { mutableStateOf<LocalDate?>(null) }
    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    val isEditMode = taskId != null
    val screenTitle = if (isEditMode) "Edit Task" else "Add Task"

    var showDueDatePicker by remember {
        mutableStateOf(false)
    }

    var showReminderDatePicker by remember { mutableStateOf(false) }

    var selectedReminderDate by remember { mutableStateOf<LocalDate?>(null) }

    var showReminderTimePicker by remember { mutableStateOf(false) }



    LaunchedEffect(existingTask) {
        if (!isInitialized) {
            existingTask?.let {
                title = it.title
                description = it.description
                priority = it.priority
                dueDate = it.dueDate
                reminderDateTime = it.reminderDateTime
                reminderEnabled = it.reminderDateTime != null

                isInitialized = true
            }
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
                    .fillMaxSize()
                    .imePadding()
                    .navigationBarsPadding(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TitleInput(
                        value = title,
                        onValueChange = { title = it.take(100) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = title.isBlank()

                    )
                    DescriptionInput(
                        value = description,
                        onValueChange = { description = it.take(500) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PrioritySelector(
                        selected = priority,
                        onPrioritySelected = { priority = it },
                    )

                    DueDatePicker(
                        date = dueDate, onClick = {
                            showDueDatePicker = true
                        })
                    if (showDueDatePicker) {
                        val datePickerState = rememberDatePickerState(
                            initialSelectedDateMillis = dueDate?.atStartOfDay(
                                ZoneId.systemDefault()
                            )?.toInstant()?.toEpochMilli()
                        )
                        DatePickerDialog(
                            onDismissRequest = { showDueDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        dueDate = Instant.ofEpochMilli(millis)
                                            .atZone(ZoneId.systemDefault()).toLocalDate()
                                    }
                                    showDueDatePicker = false
                                }) {
                                    Text("Ok")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showDueDatePicker = false
                                }) { Text("Cancel") }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }


                    }

                    if (showReminderDatePicker) {
                        val reminderDatePickerState = rememberDatePickerState(
                            initialSelectedDateMillis = reminderDateTime?.toLocalDate()
                                ?.atStartOfDay(
                                    ZoneId.systemDefault()
                                )?.toInstant()?.toEpochMilli()
                        )
                        DatePickerDialog(onDismissRequest = {
                            showReminderDatePicker = false
                            if (reminderDateTime == null) {
                                reminderEnabled = false
                            }
                        }, confirmButton = {
                            TextButton(
                                onClick = {
                                    reminderDatePickerState.selectedDateMillis?.let { millis ->
                                        selectedReminderDate = Instant.ofEpochMilli(millis)
                                            .atZone(ZoneId.systemDefault()).toLocalDate()
                                        showReminderDatePicker = false
                                        showReminderTimePicker = true
                                    }
                                }) {
                                Text("OK")
                            }
                        }, dismissButton = {
                            TextButton(onClick = { showReminderDatePicker = false
                            if(reminderDateTime==null) {
                                reminderEnabled = false
                            }
                            }) { Text("Cancel") }
                        }) {
                            DatePicker(state = reminderDatePickerState)
                        }
                    }

                    ReminderToggle(
                        enabled = reminderEnabled, onToggle = { enabled ->
                            reminderEnabled = enabled

                            if (enabled) {
                                if (reminderDateTime == null) {
                                    showReminderDatePicker = true
                                }
                            } else {
                                reminderDateTime = null
                            }
                        })

                    if (reminderEnabled) {
                        ReminderDateTimePicker(
                            dateTime = reminderDateTime, onClick = {
                                showReminderDatePicker = true
                            })
                    }

//                    Spacer(Modifier.weight(1f))
                }
                SaveTaskButton(
                    enabled = title.isNotBlank(), onClick = {
                        val task = Task(
                            id = existingTask?.id ?: 0L,
                            title = title.trim(),
                            description = description.trim(),
                            priority = priority,
                            isCompleted = existingTask?.isCompleted ?: false,
                            dueDate = dueDate,
                            reminderDateTime = reminderDateTime
                        )

                        if (isEditMode) {
                            viewModel.onEvent(TaskEvent.UpdateTask(task))
                        } else {
                            viewModel.onEvent(TaskEvent.AddTask(task))
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        })

    if (showReminderTimePicker) {
        val currentTime = reminderDateTime?.toLocalTime() ?: LocalTime.now()

        LaunchedEffect(Unit) {
            val picker = TimePickerDialog(
                context, { _, hour, minute ->
                    selectedReminderDate?.let { date ->
                        val selectedDateTime = LocalDateTime.of(date, LocalTime.of(hour, minute))

                        if (selectedDateTime.isAfter(LocalDateTime.now())) {
                            reminderDateTime = selectedDateTime
                        } else {
                            Toast.makeText(context, "Please select a future time", Toast.LENGTH_SHORT).show()
//                            if (reminderDateTime == null) {reminderEnabled = false}
                        }
                    }
                    showReminderTimePicker = false
                }, currentTime.hour, currentTime.minute, false
            )
            picker.setOnCancelListener {
                showReminderTimePicker = false
                if (reminderDateTime == null) {
                    reminderEnabled = false
                }
            }
            picker.show()
        }
    }


}


/*@Preview(showBackground = true)
@Composable
private fun AddEditTaskPreview() {
    ToDoTheme {
        AddEditTaskScreen(
            taskId = 1L, onSave = {}, onCancel = {},
            viewModel = TODO()
        )

    }
}*/

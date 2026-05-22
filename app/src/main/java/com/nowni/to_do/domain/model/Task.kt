package com.nowni.to_do.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Task(
    val id: Long = 0,
    val title: String,
    val priority: TaskPriority,
    val description: String = "",
    val isCompleted: Boolean = false,
    val isImportant: Boolean = false,
    val dueDate: LocalDate? = null,
    val reminderDateTime: LocalDateTime? = null,
)
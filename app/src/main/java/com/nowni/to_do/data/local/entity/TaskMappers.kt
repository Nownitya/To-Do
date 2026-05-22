package com.nowni.to_do.data.local.entity

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.model.TaskPriority

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        priority = TaskPriority.valueOf(priority),
        isCompleted = isCompleted,
        dueDate = dueDate,
        reminderDateTime = reminderDateTime
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id=id,
        title=title,
        description=description,
        priority= priority.name,
        isCompleted= isCompleted,
        dueDate= dueDate,
        reminderDateTime = reminderDateTime
    )
}
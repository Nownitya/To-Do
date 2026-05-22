package com.nowni.to_do.domain.usecase

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.repository.TaskRepository

class AddTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Long {
        if (task.title.isBlank()) {
            throw IllegalArgumentException("Title cannot be empty")
        }
        return repository.insertTask(task)
    }
}
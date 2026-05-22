package com.nowni.to_do.domain.usecase

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.repository.TaskRepository

class ToggleTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.updateTask(
            task.copy(
                isCompleted = !task.isCompleted
            )
        )
    }
}
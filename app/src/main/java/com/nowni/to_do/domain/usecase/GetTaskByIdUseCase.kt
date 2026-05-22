package com.nowni.to_do.domain.usecase

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.repository.TaskRepository

class GetTaskByIdUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long): Task? {
        return repository.getTaskById(id)
    }
}
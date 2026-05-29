package com.nowni.to_do.domain.usecase

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.repository.TaskRepository
import com.nowni.to_do.domain.sort.SortField
import com.nowni.to_do.domain.sort.SortOptions
import com.nowni.to_do.domain.sort.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(
        searchQuery: String,
        sortField: SortField
    ): Flow<List<Task>> {
        return repository.getTasks(searchQuery = searchQuery, sortField = sortField)
    }
}
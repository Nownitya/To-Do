package com.nowni.to_do.domain.usecase

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.repository.TaskRepository
import com.nowni.to_do.domain.sort.SortField
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(
        searchQuery: String,
        sortField: SortField
    ): Flow<List<Task>> {
        return repository.getTasks(searchQuery = searchQuery, sortField = sortField)
    }
}
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
        sortOptions: SortOptions
    ): Flow<List<Task>> {
        return repository.getTasks().map { tasks ->
            var filtered = tasks
            if (searchQuery.isNotBlank()) {
                filtered = filtered.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                            it.description.contains(searchQuery, ignoreCase = true)
                }
            }
            filtered = sortTasks(filtered, sortOptions)
            filtered

        }
    }

    private fun sortTasks(
        tasks: List<Task>, options: SortOptions
    ): List<Task> {
        val sorted = when (options.primary) {
            SortField.TITLE -> tasks.sortedBy { it.title.lowercase() }

            SortField.PRIORITY -> tasks.sortedBy { it.priority }

            SortField.DUE_DATE -> tasks.sortedBy { it.dueDate }

            SortField.COMPLETION_STATUS -> tasks.sortedBy { it.isCompleted }

            SortField.REMINDER_DATE -> tasks.sortedBy { it.reminderDate }

            SortField.CREATED_DATE -> tasks.sortedBy { it.id }

        }
        return if (options.order == SortOrder.DESCENDING) {
            sorted.reversed()
        } else sorted

    }

}
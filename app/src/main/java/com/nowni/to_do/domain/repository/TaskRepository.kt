package com.nowni.to_do.domain.repository

import androidx.room.Query
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.sort.SortField
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
//    fun getTasks2(): Flow<List<Task>>

    fun getTasks(
        searchQuery: String,
        sortField: SortField
    ): Flow<List<Task>>
    suspend fun getTaskById(id: Long): Task?
    suspend fun insertTask(task: Task):Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}
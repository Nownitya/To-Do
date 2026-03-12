package com.nowni.to_do.data.repository

import com.nowni.to_do.data.local.dao.TaskDao
import com.nowni.to_do.data.local.entity.toDomain
import com.nowni.to_do.data.local.entity.toEntity
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao
): TaskRepository {
    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
}
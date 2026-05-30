package com.nowni.to_do.core.di

import com.nowni.to_do.data.local.dao.TaskDao
import com.nowni.to_do.data.repository.TaskRepositoryImpl
import com.nowni.to_do.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao
    ): TaskRepository {
        return TaskRepositoryImpl(taskDao)

    }
}
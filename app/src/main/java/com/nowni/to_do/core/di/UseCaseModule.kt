package com.nowni.to_do.core.di

import com.nowni.to_do.domain.repository.TaskRepository
import com.nowni.to_do.domain.usecase.AddTaskUseCase
import com.nowni.to_do.domain.usecase.DeleteTaskUseCase
import com.nowni.to_do.domain.usecase.GetTaskByIdUseCase
import com.nowni.to_do.domain.usecase.GetTasksUseCase
import com.nowni.to_do.domain.usecase.TaskUseCases
import com.nowni.to_do.domain.usecase.ToggleTaskUseCase
import com.nowni.to_do.domain.usecase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideTaskUseCase(
        repository: TaskRepository
    ): TaskUseCases {
        return TaskUseCases(
            getTasks = GetTasksUseCase(repository),
            addTask = AddTaskUseCase(repository),
            getTaskById = GetTaskByIdUseCase(repository),
            updateTask = UpdateTaskUseCase(repository),
            deleteTask = DeleteTaskUseCase(repository),
            toggleTask = ToggleTaskUseCase(repository)
        )
    }
}
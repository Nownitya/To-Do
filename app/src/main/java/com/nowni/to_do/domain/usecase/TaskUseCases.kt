package com.nowni.to_do.domain.usecase

data class TaskUseCases(
    val getTasks: GetTasksUseCase,
    val addTask: AddTaskUseCase,
    val getTaskById: GetTaskByIdUseCase,
    val updateTask: UpdateTaskUseCase,
    val deleteTask: DeleteTaskUseCase,
    val toggleTask: ToggleTaskUseCase
)
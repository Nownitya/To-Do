package com.nowni.to_do.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowni.to_do.data.reminder.ReminderScheduler
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.repository.TaskRepository
import com.nowni.to_do.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val repository: TaskRepository,
    private val scheduler: ReminderScheduler
) : ViewModel() {

    // later: viewModelScope.launch{ . . . }
    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state

    private var observeJob: Job? = null

    private val _uiEvent = MutableSharedFlow<TaskUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var recentlyDeletedTask: Task? = null
    private var recentlyDeletedTasks = mutableListOf<Task>()


    init {
        observeTasks()
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
//            is TaskEvent.LoadTasks -> loadTasks()
            is TaskEvent.ToggleTask -> toggleTask(event.taskId)
            is TaskEvent.DeleteTask -> deleteTask(event.taskId)
            is TaskEvent.UndoDelete -> restoreDeletedTask()
            is TaskEvent.AddTask -> addTask(event.task)
            is TaskEvent.UpdateTask -> updateTask(event.task)
            is TaskEvent.SearchTask -> {
                _state.update { it.copy(searchQuery = event.query) }
                observeTasks()
            }

            is TaskEvent.SortTasks -> {
                _state.update { it.copy(sortOptions = event.options) }
                observeTasks()
            }
        }
    }

    /* fun getTaskById(taskId: Long): Task? {
         return cachedTasks.firstOrNull { it.id == taskId }
     }*/

    private fun observeTasks() {
        observeJob?.cancel()


        observeJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getTasksUseCase(
                searchQuery = _state.value.searchQuery,
                sortOptions = _state.value.sortOptions
            ).catch { exception ->
                _state.update {
                    it.copy(
                        isLoading = false, error = exception.message ?: "Unknown error"
                    )
                }
            }.collect { tasks ->
                _state.update {
                    it.copy(
                        tasks = tasks, isLoading = false, error = null
                    )
                }
            }
        }
    }

    private fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.insertTask(task)
                task.reminderDate?.let {
                    scheduler.schedule(
                        taskId = task.id,
                        title = task.title,
                        reminderTime = it.atStartOfDay()
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to add task")
                }
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
                scheduler.cancel(task.id)

                task.reminderDate?.let {
                    scheduler.schedule(
                        taskId= task.id,
                        title = task.title,
                        reminderTime = it.atStartOfDay()
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to update task")
                }
            }
        }
    }

    private fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            val task = state.value.tasks.firstOrNull{ it.id == taskId} ?: repository.getTaskById(taskId)
            task?.let { task ->
                try {
                    repository.deleteTask(task)

                    scheduler.cancel(taskId)
                } catch (e: Exception) {
                    _state.update { it.copy(error = "Delete Failed") }
                }
                recentlyDeletedTask = task
                viewModelScope.launch{
                    _uiEvent.emit(
                        TaskUiEvent.ShowSnackbar(
                            message = "Task Deleted",
                            action = "Undo"
                        )
                    )
                }
            }
        }
    }

    private fun restoreDeletedTask() {
        viewModelScope.launch{
            recentlyDeletedTask?.let { task->
                repository.insertTask(task)

                task.reminderDate?.let {
                    scheduler.schedule(
                        taskId = task.id,
                        title = task.title,
                        reminderTime = it.atStartOfDay()
                    )
                }
                recentlyDeletedTask=null
            }
        }
    }

    private fun toggleTask(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let {
                repository.updateTask(it.copy(isCompleted = !it.isCompleted))
            }
        }
    }

    fun getTaskFromState(taskId: Long): Task? {
        return state.value.tasks.firstOrNull { it.id == taskId }
    }

    /*    private fun applyFilters() {
            var filtered = cachedTasks
            val query = _state.value.searchQuery
            if (query.isNotBlank()) {
                filtered = filtered.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
            }
            filtered = sortTasks(filtered, _state.value.sortOptions)

            _state.update {
                it.copy(
                    tasks = filtered, isLoading = false
                )
            }
        }*/

    /*private fun sortTasks(
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
        return if (options.order == SortOrder.DESCENDING)
            sorted.reversed()
        else sorted

    }*/
}
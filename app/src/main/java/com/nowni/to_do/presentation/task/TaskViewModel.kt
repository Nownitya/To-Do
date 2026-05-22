package com.nowni.to_do.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowni.to_do.data.reminder.ReminderScheduler
import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.usecase.TaskUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val useCase: TaskUseCases, private val scheduler: ReminderScheduler
) : ViewModel() {

    // later: viewModelScope.launch{ . . . }
    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state

    private var observeJob: Job? = null
    private var searchJob: Job? = null

    private val _uiEvent = MutableSharedFlow<TaskUiEvent>(
        extraBufferCapacity = 1
    )
    val uiEvent = _uiEvent.asSharedFlow()

    private var recentlyDeletedTask: Task? = null

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
                _state.update {
                    it.copy(searchQuery = event.query)
                }
                searchJob?.cancel()

                searchJob = viewModelScope.launch {
                    delay(300)
                    observeTasks()
                }
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

    private fun clearError() {
        _state.update {
            it.copy(error = null)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun observeTasks() {
        observeJob?.cancel()


        observeJob = viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
            setLoading(true)
            useCase.getTasks(
                searchQuery = _state.value.searchQuery, sortOptions = _state.value.sortOptions
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
            clearError()
            setLoading(true)
            try {
                val insertedId = useCase.addTask(task)
                task.reminderDateTime?.let {
                    scheduler.schedule(
                        taskId = insertedId, title = task.title, reminderTime = it
                    )
                }
                _uiEvent.emit(TaskUiEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to add task")
                }
            }finally {
                setLoading(false)
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            clearError()
            setLoading(true)
            try {
                useCase.updateTask(task)
                scheduler.cancel(task.id)

                task.reminderDateTime?.let {
                    scheduler.schedule(
                        taskId = task.id, title = task.title, reminderTime = it
                    )
                }
                _uiEvent.emit(TaskUiEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to update task")
                }
            }finally {
                setLoading(false)
            }
        }
    }

    private fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            clearError()
            setLoading(true)
            val task = state.value.tasks.firstOrNull {
                it.id == taskId
            } ?: useCase.getTaskById(taskId)
            task?.let { task ->
                try {
                    useCase.deleteTask(task)

                    scheduler.cancel(taskId)

                    recentlyDeletedTask = task

                    _uiEvent.emit(
                        TaskUiEvent.ShowSnackbar(
                            message = "Task Deleted", action = "Undo"
                        )
                    )
                } catch (e: Exception) {
                    _state.update {
                        it.copy(error = e.message ?: "Delete Failed")
                    }
                }finally {
                    setLoading(false)
                }

            }
        }
    }

    private fun restoreDeletedTask() {
        viewModelScope.launch {
            clearError()
            setLoading(true)
            recentlyDeletedTask?.let { task ->
                try {
                    val restoredTaskId = useCase.addTask(task)

                    task.reminderDateTime?.let {
                        scheduler.schedule(
                            taskId = restoredTaskId, title = task.title, reminderTime = it
                        )
                    }
                    recentlyDeletedTask = null
                } catch (e: Exception) {
                    _state.update {
                        it.copy(error = e.message ?: "Failed to restore task")
                    }
                }finally {
                    setLoading(false)
                }
            }
        }
    }

    private fun toggleTask(taskId: Long) {

        viewModelScope.launch {
            clearError()
            setLoading(true)
            val task = state.value.tasks.firstOrNull {
                it.id == taskId
            } ?: useCase.getTaskById(taskId)

            task?.let {
                try {
                    useCase.toggleTask(it)
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            error = e.message ?: "Failed to update task"
                        )
                    }
                }finally {
                    setLoading(false)
                }
            }
        }
    }

    fun getTaskFromState(taskId: Long): Task? {
        return state.value.tasks.firstOrNull { it.id == taskId }
    }

}
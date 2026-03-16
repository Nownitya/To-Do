package com.nowni.to_do.presentation.task

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.sort.SortOptions

sealed interface TaskEvent{
    /** Task List actions */
    data class ToggleTask(val taskId:Long): TaskEvent
    data class DeleteTask(val taskId: Long): TaskEvent
    object UndoDelete: TaskEvent

    /** Add / Edit */
    data class AddTask(val task: Task): TaskEvent
    data class UpdateTask(val task: Task): TaskEvent

    /** Search & Sort */
    data class SearchTask(val query:String): TaskEvent
    data class SortTasks(val options: SortOptions): TaskEvent



    /** Initial Load */
//    object LoadTasks: TaskEvent
}
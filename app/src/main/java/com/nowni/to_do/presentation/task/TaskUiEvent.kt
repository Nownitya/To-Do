package com.nowni.to_do.presentation.task

sealed class TaskUiEvent {
    data class ShowSnackbar(
        val message:String,
        val action:String?= null
    ): TaskUiEvent()
}
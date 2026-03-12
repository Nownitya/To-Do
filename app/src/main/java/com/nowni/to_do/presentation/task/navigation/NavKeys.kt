package com.nowni.to_do.presentation.task.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class AddEditTask(val taskId: Long? = null) : NavKey

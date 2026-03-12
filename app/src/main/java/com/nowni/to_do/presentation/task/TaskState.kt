package com.nowni.to_do.presentation.task

import com.nowni.to_do.domain.model.Task
import com.nowni.to_do.domain.sort.SortField
import com.nowni.to_do.domain.sort.SortOptions
import com.nowni.to_do.domain.sort.SortOrder

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchQuery: String = "",
    val sortOptions: SortOptions = SortOptions(
        primary = SortField.CREATED_DATE,
        order = SortOrder.DESCENDING
    )
)

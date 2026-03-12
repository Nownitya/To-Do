package com.nowni.to_do.domain.sort

data class SortOptions(
    val primary: SortField,
    val secondary: SortField?=null,
    val order: SortOrder=SortOrder.ASCENDING,
)
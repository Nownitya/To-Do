package com.nowni.to_do.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName= "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0L,
    val title:String,
    val description:String,
    val priority:String,
    val isCompleted:Boolean,
    val dueDate: LocalDate?,
    val reminderDateTime: LocalDateTime?
)
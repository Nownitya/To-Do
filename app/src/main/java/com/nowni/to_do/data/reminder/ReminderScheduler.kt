package com.nowni.to_do.data.reminder

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.setInputMerger
import androidx.work.workDataOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Duration

class ReminderScheduler(private val context: Context) {
    fun schedule(
        taskId: Long,
        title: String,
        reminderTime: LocalDateTime
    ) {
        val delay = Duration.between(
            LocalDateTime.now(),
            reminderTime
        ).toMillis()

        if (delay<=0) return
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf("title" to title)
            )
//            .ad  skId.toString())
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "task_$taskId",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun cancel(taskId: Long) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("task_$taskId")
    }


}
package com.nowni.to_do.data.reminder

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.time.Duration
import java.time.LocalDateTime

class ReminderScheduler(private val context: Context) {

    private fun workName(taskId: Long) = "task_reminder_$taskId"

    fun schedule(
        taskId: Long,
        title: String,
        reminderTime: LocalDateTime
    ) {
        val delay = Duration.between(
            LocalDateTime.now(),
            reminderTime
        ).toMillis()

        if (delay <= 0) return
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf("title" to title)
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                workName(taskId),
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun cancel(taskId: Long) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(workName(taskId))
    }


}
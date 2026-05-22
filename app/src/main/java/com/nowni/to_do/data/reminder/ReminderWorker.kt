package com.nowni.to_do.data.reminder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nowni.to_do.core.notification.NotificationHelper

class ReminderWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val hasPermissions =
            Build.VERSION.SDK_INT<Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermissions) {
            return Result.failure()
        }

        val title = inputData.getString("title") ?: "Task Reminder"

        val notification = NotificationHelper.buildNotification(
            applicationContext,
            title
        )
        
        NotificationManagerCompat.from(applicationContext)
            .notify(System.currentTimeMillis().toInt(), notification)
        return Result.success()
    }
}
package com.nowni.to_do.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.service.autofill.Validators.or
import androidx.core.app.NotificationCompat
import com.nowni.to_do.MainActivity
import com.nowni.to_do.R

object NotificationHelper {
    const val CHANNEL_ID = "task_reminder_channel"
    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID, "Task Reminder", NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Notifications for task reminders" }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun buildNotification(
        context: Context, title: String, taskId: Long
    ): Notification {

        val intent = Intent(
            context,
            MainActivity::class.java
        ).apply {
            putExtra(
                NotificationsConstants.EXTRA_TASK_ID, taskId
            )
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP

        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Reminder")

            .setContentText(title).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

    }
}
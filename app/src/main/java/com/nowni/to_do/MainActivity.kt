package com.nowni.to_do

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableLongStateOf
import com.nowni.to_do.core.notification.NotificationHelper
import com.nowni.to_do.core.notification.NotificationsConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        val notificationTaskId = mutableLongStateOf(-1L)
    }

    private fun handleIntent(intent: Intent?) {
        val taskId = intent?.getLongExtra(
            NotificationsConstants.EXTRA_TASK_ID,
            -1L
        ) ?: -1L

        if (taskId != -1L) {
            notificationTaskId.longValue = taskId
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        // create notification channel
        NotificationHelper.createChannel(this)
        setContent {
            Surface {
                TodoApp()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
}


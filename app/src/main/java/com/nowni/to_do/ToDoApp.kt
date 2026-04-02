package com.nowni.to_do

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.nowni.to_do.core.permission.RequestNotificationPermission
import com.nowni.to_do.core.ui.theme.ToDoTheme
import com.nowni.to_do.presentation.navigation.AppNavGraph

@Composable
fun TodoApp() {
    ToDoTheme {
        Surface {
            RequestNotificationPermission()
            AppNavGraph()
        }
    }
}

package com.nowni.to_do

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nowni.to_do.core.permission.RequestNotificationPermission
import com.nowni.to_do.core.ui.theme.ToDoTheme
import com.nowni.to_do.domain.model.ThemeMode
import com.nowni.to_do.presentation.navigation.AppNavGraph
import com.nowni.to_do.presentation.theme.ThemeViewModel

@Composable
fun TodoApp() {
    val viewModel: ThemeViewModel = hiltViewModel()

    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK-> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    ToDoTheme(darkTheme = isDarkTheme) {
        Surface {
            RequestNotificationPermission()
            AppNavGraph()

        }
    }
}

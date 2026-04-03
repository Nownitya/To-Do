package com.nowni.to_do

import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nowni.to_do.core.permission.RequestNotificationPermission
import com.nowni.to_do.core.ui.theme.ToDoTheme
import com.nowni.to_do.data.preferences.ThemePreferences
import com.nowni.to_do.domain.model.ThemeMode
import com.nowni.to_do.presentation.navigation.AppNavGraph
import com.nowni.to_do.presentation.theme.ThemeViewModel

@Composable
fun TodoApp() {
    val context= LocalContext.current
    val preferences= remember {
        ThemePreferences(context)
    }

    val viewModel: ThemeViewModel = viewModel(
        factory= object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass:Class<T>):T{
                return ThemeViewModel(preferences) as T
            }
        }
    )

    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK-> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }



    ToDoTheme(darkTheme = isDarkTheme) {

        Surface {
            RequestNotificationPermission()
            AppNavGraph(
                onThemeToggle = {
                    val next = when (themeMode) {
                        ThemeMode.DARK -> ThemeMode.LIGHT
                        ThemeMode.LIGHT -> ThemeMode.DARK
                        ThemeMode.SYSTEM -> ThemeMode.DARK
                    }
                    viewModel.setTheme(next)
                }
            )
        }
    }
}

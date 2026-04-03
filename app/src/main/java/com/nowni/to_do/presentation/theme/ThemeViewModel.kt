package com.nowni.to_do.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowni.to_do.data.preferences.ThemePreferences
import com.nowni.to_do.domain.model.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val preferences: ThemePreferences): ViewModel() {
    val themeMode = preferences.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch {
            preferences.setTheme(mode)
        }

    }

}
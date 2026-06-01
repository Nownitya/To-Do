package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nowni.to_do.domain.model.ThemeMode

@Composable
fun ThemeSection(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    Column {
        Text("Appearance",
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        )
        ThemeMode.entries.forEach { theme->
            ListItem(modifier = Modifier.fillMaxWidth()
                .selectable(
                    selected = selectedTheme ==theme,
                    onClick = {
                        onThemeSelected(theme)
                    }
                ),
                headlineContent = {
                    Text(text = theme.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                    )
                },
                leadingContent = {
                    RadioButton(
                        selected = selectedTheme == theme,
                        onClick = null
                    )
                }
            )
        }

    }


}
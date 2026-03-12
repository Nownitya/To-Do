package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nowni.to_do.core.ui.theme.ToDoTheme

@Composable
fun SaveTaskButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Save Task")
    }
}

@Composable
fun SaveTaskButtonPreviewContent(enabled: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Button is ${enabled.toLabel()}")
        SaveTaskButton(
            enabled = enabled,
            onClick = {}
        )
    }
}


private fun Boolean.toLabel(): String =
    if (this) "Enabled" else "Disabled"


@Preview(showBackground = true)
@Composable
private fun SaveTaskButtonEnabledPreview() {
    ToDoTheme {
        SaveTaskButtonPreviewContent(enabled = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveTaskButtonDisabledPreview() {
    ToDoTheme {
        SaveTaskButtonPreviewContent(enabled = false)
    }
}

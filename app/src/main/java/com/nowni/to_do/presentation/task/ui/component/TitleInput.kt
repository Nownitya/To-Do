package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nowni.to_do.core.ui.theme.ToDoTheme

@Composable
fun TitleInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text("Title") },
        singleLine = true,
        isError = isError
    )
}

@Preview(showBackground = true)
@Composable
fun TitleInputPreview() {
    ToDoTheme {
        TitleInput(value = "Sample Title", onValueChange = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TitleInputErrorPreview() {
    ToDoTheme {
        TitleInput(value = "Sample Title with Error", onValueChange = {}, isError = true)
    }
}
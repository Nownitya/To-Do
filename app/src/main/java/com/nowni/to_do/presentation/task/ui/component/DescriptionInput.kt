package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nowni.to_do.core.ui.theme.ToDoTheme

@Composable
fun DescriptionInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value=value,
        onValueChange= onValueChange,
        modifier=modifier,
        label ={ Text("Description")},
        minLines = 3
    )
}

@Preview(showBackground = true)
@Composable
fun DescriptionInputPreview() {
    ToDoTheme {
        DescriptionInput(
            value = "This is a sample description.",
            onValueChange = {}
        )
    }
}
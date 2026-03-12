package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nowni.to_do.core.ui.theme.ToDoTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DueDatePicker(
    date: LocalDate?, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyy")
    val text = date?.format(formatter) ?: "No due date"

    OutlinedTextField(
        value = text,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        enabled = false,
        label = { Text("Due date") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Event, contentDescription = "Pick due date"
            )
        })
}

@Preview(showBackground = true)
@Composable
private fun DueDatePickerPreview() {
    ToDoTheme {
        DueDatePicker(
            date = LocalDate.now(), onClick = {})

    }

}
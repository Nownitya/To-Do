package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ReminderDateTimePicker(
    dateTime: LocalDateTime?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    val text = dateTime?.format(formatter) ?: "Select reminder time"

    OutlinedTextField(
        value = text,
        onValueChange = {},
        modifier= modifier
            .fillMaxWidth()
            .clickable { onClick() },
        enabled = false,
        label = { Text("Reminder") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Alarm,
                contentDescription = "Pick reminder date & time"
            )
        }
    )
}
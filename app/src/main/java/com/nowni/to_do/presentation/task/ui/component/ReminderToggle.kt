package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nowni.to_do.core.ui.theme.ToDoTheme

@Composable
fun ReminderToggle(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Set reminder",
            modifier= Modifier.weight(1f)
        )
        Switch(checked = enabled,
            onCheckedChange = onToggle)
    }

}


@Preview(showBackground = true)
@Composable
fun ReminderTogglePreview() {
    ToDoTheme {
        ReminderToggle(
            enabled = true,
            onToggle = {  },
        )
    }


}

@Preview(showBackground = true) // Added showBackground for better visibility
@Composable
fun ReminderToggleInteractivePreview() {
    var reminderEnabled by remember { mutableStateOf(false) } // Manage state here

    ToDoTheme {
        // Wrap in a Column for spacing and to see both the toggle and its label clearly
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Interactive Reminder Toggle:", style = MaterialTheme.typography.titleMedium)
            ReminderToggle(
                enabled = reminderEnabled,
                onToggle = { isEnabled -> reminderEnabled = isEnabled } // Update state here
            )
        }
    }
}

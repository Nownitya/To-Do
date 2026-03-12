package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import com.nowni.to_do.domain.model.TaskPriority

@Composable
fun PrioritySelector(
    selected: TaskPriority,
    onPrioritySelected: (TaskPriority) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskPriority.entries.forEach { priority ->
            FilterChip(
                selected = selected == priority,
                onClick = { onPrioritySelected(priority) },
//                label = { Text(priority.name.lowercase().replaceFirstChar { it.uppercase() }) }
                label = { Text(priority.displayName()) }
            )
        }
    }
}

private fun TaskPriority.displayName(): String =
    when (this) {
        TaskPriority.LOW -> "Low"
        TaskPriority.MEDIUM -> "Medium"
        TaskPriority.HIGH -> "High"

    }
// name.lowercase().replaceFirstChar { it.uppercase() }


@Preview(showBackground = true)
@Composable
fun PrioritySelectorInitialPreview() {
    var selectedPriority by remember { mutableStateOf(TaskPriority.LOW) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Selected Task Priority (Initial State):",
            style = MaterialTheme.typography.titleMedium
        )
        PrioritySelector(
            selected = selectedPriority,
            onPrioritySelected = { selectedPriority = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrioritySelectorMediumPreview() {
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Selected Task Priority (Medium Selected):",
            style = MaterialTheme.typography.titleMedium
        )
        PrioritySelector(
            selected = selectedPriority,
            onPrioritySelected = { selectedPriority = it }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PrioritySelectorHighPreview() {
    var selectedPriority by remember { mutableStateOf(TaskPriority.HIGH) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Selected Task Priority (High Selected):",
            style = MaterialTheme.typography.titleMedium
        )
        PrioritySelector(
            selected = selectedPriority,
            onPrioritySelected = { selectedPriority = it }
        )
    }
}

@Composable
fun PrioritySelectorAutoTitlePreview(initialPriority: TaskPriority) {
    var selectedPriority by remember { mutableStateOf(initialPriority) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Selected Priority(${initialPriority.displayName()})",
            style = MaterialTheme.typography.titleMedium
        )
        PrioritySelector(
            selected = selectedPriority,
            onPrioritySelected = { selectedPriority = it}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PriorityPreviewLow() {
    ToDoTheme{ PrioritySelectorAutoTitlePreview(TaskPriority.LOW) }
}

@Preview(showBackground = true)
@Composable
private fun PriorityPreviewMedium() {
    ToDoTheme{ PrioritySelectorAutoTitlePreview(TaskPriority.MEDIUM) }
}

@Preview(showBackground = true)
@Composable
private fun PriorityPreviewHigh() {
    ToDoTheme{ PrioritySelectorAutoTitlePreview(TaskPriority.HIGH) }
}


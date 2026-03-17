package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nowni.to_do.core.ui.theme.ToDoTheme

@Composable
fun EmptyTaskList(modifier: Modifier = Modifier) {
    Column(modifier= modifier
        .fillMaxWidth()
        .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(text = "No Task Available!",
                style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap + to add a new task.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyTaskListPreview() {
    ToDoTheme {
        Surface {
            EmptyTaskList()
        }
    }
}
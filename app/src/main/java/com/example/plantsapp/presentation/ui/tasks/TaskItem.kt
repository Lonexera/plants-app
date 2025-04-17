package com.example.plantsapp.presentation.ui.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.presentation.model.TaskWithState
import com.example.plantsapp.presentation.ui.utils.getColorRes
import com.example.plantsapp.presentation.ui.utils.getIconRes
import com.example.plantsapp.presentation.ui.utils.getTitleRes

@Composable
fun TaskItem(
    plant: Plant,
    taskWithState: TaskWithState,
    onTaskClick: (Pair<Plant, Task>) -> Unit,
    modifier: Modifier = Modifier
) {
    val task = taskWithState.task
    val taskColor = colorResource(task.getColorRes())

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Task Icon
        Icon(
            painter = painterResource(task.getIconRes()),
            contentDescription = null,
            tint = taskColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Task Title
        Text(
            text = stringResource(task.getTitleRes()),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // Complete Button
        if (taskWithState.isCompletable) {
            IconButton(
                onClick = { onTaskClick(plant to task) },
                enabled = !taskWithState.isCompleted
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Complete task",
                    tint = if (taskWithState.isCompleted) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    } else {
                        taskColor
                    }
                )
            }
        }
    }
}

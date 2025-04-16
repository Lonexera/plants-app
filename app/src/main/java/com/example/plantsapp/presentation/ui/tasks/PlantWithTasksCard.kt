package com.example.plantsapp.presentation.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.presentation.model.TaskWithState

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlantWithTasksCard(
    plant: Plant,
    tasks: List<TaskWithState>,
    onTaskClick: (Pair<Plant, Task>) -> Unit,
    modifier: Modifier = Modifier
) {
    val notCompletedAmount = tasks.count { !it.isCompleted }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16dp),
                vertical = dimensionResource(R.dimen.margin_32dp)
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.cardview_corner_radius_size))
    ) {
        Column {
            // Plant Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.margin_16dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Plant Image
                GlideImage(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.task_plant_image_size))
                        .clip(RoundedCornerShape(12)),
                    model = plant.plantPicture,
                    contentDescription = "Plant picture",
                    contentScale = ContentScale.Crop,
                    failure = placeholder(R.drawable.ic_baseline_image_24)
                )

                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.margin_16dp)))

                // Plant Name and Tasks Count
                Column {
                    Text(
                        text = plant.name.value,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = pluralStringResource(
                            R.plurals.msg_task_amount_left,
                            notCompletedAmount,
                            notCompletedAmount
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.margin_8dp),
                    bottom = dimensionResource(R.dimen.margin_8dp)
                )
            )

            // Tasks List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.margin_8dp))
            ) {
                items(tasks) { taskWithState ->
                    TaskItem(
                        plant = plant,
                        taskWithState = taskWithState,
                        onTaskClick = onTaskClick
                    )
                }
            }
        }
    }
} 
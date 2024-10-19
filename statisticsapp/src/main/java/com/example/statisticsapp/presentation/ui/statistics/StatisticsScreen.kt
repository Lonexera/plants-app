package com.example.statisticsapp.presentation.ui.statistics

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.plantsapp.domain.model.Task
import com.example.statisticsapp.R
import com.example.statisticsapp.presentation.core.Event
import com.example.statisticsapp.presentation.model.PlantStatisticsInfo
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val plantsWithTasksStatistics by viewModel.plantsWithTasksStatistics.collectAsStateWithLifecycle()

    StatisticsScreen(
        isLoading = isLoading,
        error = viewModel.error,
        plantsWithTasksStatistics = plantsWithTasksStatistics,
    )
}

@Composable
private fun StatisticsScreen(
    isLoading: Boolean,
    error: SharedFlow<Event<Int>>,
    plantsWithTasksStatistics: List<PlantStatisticsInfo>,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        error.collect { event ->
            event.getContentIfNotHandled()?.let {
                snackbarHostState.showSnackbar(
                    message = context.getString(it)
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = isLoading,
            label = "statistics_screen_loading_transition",
        ) { loading ->
            if (loading) {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            } else {
                PlantsWithTasksStatistics(
                    modifier = Modifier.fillMaxSize(),
                    plantsWithTasksStatistics = plantsWithTasksStatistics,
                )
            }
        }
    }
}

@Composable
private fun LoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(0.3f)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PlantsWithTasksStatistics(
    modifier: Modifier = Modifier,
    plantsWithTasksStatistics: List<PlantStatisticsInfo>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = plantsWithTasksStatistics,
            key = { it.plant.name.value },
        ) { plant ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            model = plant.plant.plantPicture,
                            contentDescription = "plant profile image",
                            failure = placeholder(R.drawable.placeholder_image),
                        )
                    }

                    Column(
                        modifier = Modifier.weight(2f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = plant.plant.name.value,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        Column {
                            plant.tasks.forEach { task ->
                                Text(
                                    text = "${task.task.displayName}: ${task.amount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val Task.displayName: String
    get() = when (this) {
        is Task.WateringTask -> "Watering"
        is Task.SprayingTask -> "Spraying"
        is Task.LooseningTask -> "Loosening"
        is Task.TakingPhotoTask -> "Taking Photo"
    }

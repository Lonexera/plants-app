package com.example.plantsapp.presentation.ui.tasks

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantsapp.R
import com.example.plantsapp.presentation.temp.Loading
import com.example.plantsapp.presentation.ui.common.noitems.NoItemsLayout
import com.example.plantsapp.presentation.ui.plantcreation.CameraContract
import com.example.plantsapp.presentation.ui.utils.getCameraImageOutputUri

@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val cameraContract = remember { CameraContract() }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = cameraContract,
    ) { uri ->
        uri?.let { viewModel.onImageCaptured(it) }
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.launchCamera.collect { event ->
            event.getContentIfNotHandled()?.let {
                val uri = context.getCameraImageOutputUri()
                cameraLauncher.launch(uri)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is TasksViewModel.TasksUiState.InitialState -> {
                Loading()
            }

            is TasksViewModel.TasksUiState.Loading -> {
                Loading()
            }

            is TasksViewModel.TasksUiState.DataIsLoaded -> {
                val plantsWithTasks =
                    (uiState as TasksViewModel.TasksUiState.DataIsLoaded).plantsWithTasks

                if (plantsWithTasks.isEmpty()) {
                    NoItemsLayout(
                        imageRes = R.drawable.ic_baseline_calendar_24,
                        title = stringResource(R.string.title_no_tasks),
                        message = stringResource(R.string.msg_no_tasks),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(plantsWithTasks) { (plant, tasks) ->
                            PlantWithTasksCard(
                                plant = plant,
                                tasks = tasks,
                                onTaskClick = { (plant, task) ->
                                    viewModel.onCompleteTaskClicked(plant, task)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

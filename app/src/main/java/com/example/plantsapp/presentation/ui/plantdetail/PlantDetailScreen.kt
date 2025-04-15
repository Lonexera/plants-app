package com.example.plantsapp.presentation.ui.plantdetail

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.presentation.temp.Loading
import com.example.plantsapp.presentation.ui.common.dialog.ConfirmationDialog
import com.example.plantsapp.presentation.ui.utils.formatDate
import com.example.plantsapp.presentation.ui.utils.getColorRes
import com.example.plantsapp.presentation.ui.utils.getIconRes
import java.util.Date

@Composable
fun PlantDetailScreen(
    viewModel: PlantDetailViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.closeScreen.collect {
            onNavigateBack()
        }
    }

    if (showDeleteConfirmation) {
        ConfirmationDialog(
            title = stringResource(R.string.title_delete_dialog_confirmation),
            message = stringResource(R.string.msg_delete_dialog_confirmation),
            onDismissRequest = { showDeleteConfirmation = false },
            onConfirm = {
                viewModel.onDelete()
            },
        )
    }

    PlantDetailScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onDeleteClick = { showDeleteConfirmation = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
private fun PlantDetailScreen(
    uiState: PlantDetailViewModel.UiState,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = uiState.plantName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete_24),
                            contentDescription = "Delete"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(R.color.white),
                    titleContentColor = colorResource(R.color.dark_grey),
                    navigationIconContentColor = colorResource(R.color.dark_grey),
                    actionIconContentColor = colorResource(R.color.dark_grey)
                )
            )
        }
    ) { paddingValues ->
        when (uiState.detailState) {
            PlantDetailViewModel.UiState.PlantDetailState.Loading -> Loading()
            is PlantDetailViewModel.UiState.PlantDetailState.DataIsLoaded -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(all = 16.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            model = uiState.detailState.plant.plantPicture,
                            contentDescription = "Plant picture",
                            contentScale = ContentScale.Crop,
                            failure = placeholder(R.drawable.ic_baseline_image_24),
                        )
                    }

                    item {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                            //                            .padding(horizontal = 16.dp)
                            ,
                            value = uiState.detailState.plant.name.value,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                            label = {
                                Text(text = stringResource(R.string.hint_detail_plant_name))
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            )
                        )
                    }

                    item {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                            //                            .padding(horizontal = 16.dp)
                            ,
                            value = uiState.detailState.plant.speciesName,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                            label = {
                                Text(text = stringResource(R.string.hint_detail_species_name))
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            )
                        )
                    }

                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = stringResource(R.string.title_plant_tasks),
                            fontSize = 18.sp,
                            color = colorResource(R.color.dark_grey),
                        )
                    }

                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Top,
                            contentPadding = PaddingValues(horizontal = 16.dp),
                        ) {
                            items(uiState.detailState.tasks) { task ->
                                TaskItem(task)
                            }
                        }
                    }

                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = stringResource(R.string.title_plant_gallery_text),
                            fontSize = 18.sp,
                            color = colorResource(R.color.dark_grey),
                        )
                    }

                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Top,
                            contentPadding = PaddingValues(horizontal = 16.dp),
                        ) {
                            when (uiState.photosState) {
                                PlantDetailViewModel.UiState.PhotosState.Loading -> {
                                    items(count = 3) {
                                        PhotoSkeletonItem()
                                    }
                                }

                                is PlantDetailViewModel.UiState.PhotosState.Photos -> {
                                    items(uiState.photosState.photos) { (uri, date) ->
                                        PhotoItem(
                                            photo = uri,
                                            date = date,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(intrinsicSize = IntrinsicSize.Min)
            .requiredWidthIn(min = 100.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .background(color = colorResource(id = task.getColorRes())),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = task.getIconRes()),
                    contentDescription = "Task icon",
                    tint = colorResource(R.color.white)
                )
            }

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = task.getShortTitleRes()),
                fontSize = 14.sp,
                color = colorResource(R.color.dark_grey),
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = task.getTaskDetailText(),
                fontSize = 14.sp,
                color = colorResource(R.color.dark_grey),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@StringRes
private fun Task.getShortTitleRes(): Int {
    return when (this) {
        is Task.WateringTask -> R.string.title_plant_detail_watering_task
        is Task.SprayingTask -> R.string.title_plant_detail_spraying_task
        is Task.LooseningTask -> R.string.title_plant_detail_loosening_task
        is Task.TakingPhotoTask -> R.string.title_plant_detail_taking_photo_task
    }
}

@Composable
private fun Task.getTaskDetailText(): String {
    return stringResource(
        id = R.string.title_task_detail,
        pluralStringResource(
            id = R.plurals.msg_creation_frequency_units,
            count = frequency,
            frequency
        )
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PhotoItem(
    photo: Uri,
    date: Date,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.width(132.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            model = photo,
            contentDescription = "Plant picture",
            contentScale = ContentScale.Crop,
            failure = placeholder(R.drawable.ic_baseline_image_24),
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            text = date.formatDate(DATE_FORMAT_WITH_YEAR),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PhotoSkeletonItem() {
    // TODO: add shimmering effect
    Box(
        modifier = Modifier
            .size(height = 120.dp, width = 132.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
    )
}

private const val DATE_FORMAT_WITH_YEAR = "d MMM yyyy HH:mm"

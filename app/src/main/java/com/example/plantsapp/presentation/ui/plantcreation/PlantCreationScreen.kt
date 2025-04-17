package com.example.plantsapp.presentation.ui.plantcreation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.plantsapp.R
import com.example.plantsapp.presentation.temp.Loading
import com.example.plantsapp.presentation.ui.utils.getCameraImageOutputUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlantCreationScreen(
    viewModel: PlantCreationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val cameraContract = remember { CameraContract() }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = cameraContract,
    ) { uri ->
        uri?.let { viewModel.onImageCaptured(it) }
    }

    val galleryContract = remember { ImagePickerContract() }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = galleryContract,
    ) { uri ->
        uri?.let { viewModel.onImageSelected(uri) }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlantCreationViewModel.Event.NavigateBack -> onNavigateBack()
                is PlantCreationViewModel.Event.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_plant_creation_screen)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Plant Image
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clickable { viewModel.onImageClick() }
            ) {
                if (uiState.selectedImageUri != null) {
                    GlideImage(
                        modifier = Modifier.fillMaxSize(),
                        model = uiState.selectedImageUri,
                        contentDescription = "Plant picture",
                        contentScale = ContentScale.Crop,
                        failure = placeholder(R.drawable.ic_baseline_image_24),
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_image_24),
                        contentDescription = "Add plant photo",
                        tint = colorResource(R.color.light_grey),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Plant Name Input
            OutlinedTextField(
                value = uiState.plantName,
                onValueChange = viewModel::onPlantNameChange,
                label = { Text(stringResource(R.string.hint_plant_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Species Name Input
            OutlinedTextField(
                value = uiState.speciesName,
                onValueChange = viewModel::onSpeciesNameChange,
                label = { Text(stringResource(R.string.hint_species_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Frequency Inputs
            FrequencyInput(
                value = uiState.wateringFrequency,
                onValueChange = viewModel::onWateringFrequencyChange,
                label = stringResource(R.string.hint_watering_frequency),
                options = uiState.frequencyOptions
            )

            Spacer(modifier = Modifier.height(8.dp))

            FrequencyInput(
                value = uiState.sprayingFrequency,
                onValueChange = viewModel::onSprayingFrequencyChange,
                label = stringResource(R.string.hint_spraying_frequency),
                options = uiState.frequencyOptions
            )

            Spacer(modifier = Modifier.height(8.dp))

            FrequencyInput(
                value = uiState.looseningFrequency,
                onValueChange = viewModel::onLooseningFrequencyChange,
                label = stringResource(R.string.hint_loosening_frequency),
                options = uiState.frequencyOptions
            )

            Spacer(modifier = Modifier.height(8.dp))

            FrequencyInput(
                value = uiState.takingPhotoFrequency,
                onValueChange = viewModel::onTakingPhotoFrequencyChange,
                label = stringResource(R.string.hint_taking_photo_frequency),
                options = uiState.frequencyOptions
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = viewModel::onSaveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.title_button_save))
            }
        }
    }

    if (uiState.showImagePickerDialog) {
        ImagePickerDialog(
            onGalleryClick = {
                galleryLauncher.launch(null)
                viewModel.onImagePickerDismiss()
            },
            onCameraClick = {
                val uri = context.getCameraImageOutputUri()
                cameraLauncher.launch(uri)
                viewModel.onImagePickerDismiss()
            },
            onDismiss = viewModel::onImagePickerDismiss
        )
    }

    if (uiState.isLoading) {
        Loading()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FrequencyInput(
    value: Int?,
    onValueChange: (Int) -> Unit,
    label: String,
    options: List<Int>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value?.toString() ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text("$option") },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ImagePickerDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.title_intent_picker_dialog)) },
        text = { Text(stringResource(R.string.msg_intent_picker_dialog)) },
        confirmButton = {
            TextButton(onClick = onGalleryClick) {
                Text(stringResource(R.string.title_intent_picker_dialog_btn_gallery))
            }
        },
        dismissButton = {
            TextButton(onClick = onCameraClick) {
                Text(stringResource(R.string.title_intent_picker_dialog_btn_camera))
            }
        }
    )
} 
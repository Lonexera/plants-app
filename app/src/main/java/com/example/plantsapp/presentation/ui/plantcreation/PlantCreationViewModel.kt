package com.example.plantsapp.presentation.ui.plantcreation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.R
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.repository.TasksRepository
import com.example.plantsapp.domain.usecase.SaveUriInStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.Date
import javax.inject.Inject
import kotlin.Exception
import kotlinx.coroutines.flow.*

@Suppress("TooGenericExceptionCaught")
@HiltViewModel
class PlantCreationViewModel @Inject constructor(
    @FirebaseQualifier private val plantsRepository: PlantsRepository,
    @FirebaseQualifier private val tasksRepository: TasksRepository,
    private val validator: PlantCreationValidator,
    private val saveUriInStorageUseCase: SaveUriInStorageUseCase
) : ViewModel() {

    data class PlantTaskFrequencies(
        val wateringFrequency: Int?,
        val sprayingFrequency: Int?,
        val looseningFrequency: Int?,
        val takingPhotoFrequency: Int?
    )

    data class UiState(
        val plantName: String = "",
        val speciesName: String = "",
        val selectedImageUri: Uri? = null,
        val wateringFrequency: Int? = null,
        val sprayingFrequency: Int? = null,
        val looseningFrequency: Int? = null,
        val takingPhotoFrequency: Int? = null,
        val frequencyOptions: List<Int> = emptyList(),
        val isLoading: Boolean = false,
        val showImagePickerDialog: Boolean = false
    )

    sealed class Event {
        object NavigateBack : Event()
        data class ShowError(val message: Int) : Event()
    }

    private val _selectedPicture: MutableLiveData<Uri> = MutableLiveData()
    val selectedPicture: LiveData<Uri> get() = _selectedPicture

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    init {
        loadFrequencyOptions()
    }

    private fun loadFrequencyOptions() {
        val options = listOf(1, 2, 3, 4, 5, 6, 7, 14, 21, 30)
        _uiState.update { it.copy(frequencyOptions = options) }
    }

    fun onPlantNameChange(name: String) {
        _uiState.update { it.copy(plantName = name) }
    }

    fun onSpeciesNameChange(species: String) {
        _uiState.update { it.copy(speciesName = species) }
    }

    fun onWateringFrequencyChange(frequency: Int) {
        _uiState.update { it.copy(wateringFrequency = frequency) }
    }

    fun onSprayingFrequencyChange(frequency: Int) {
        _uiState.update { it.copy(sprayingFrequency = frequency) }
    }

    fun onLooseningFrequencyChange(frequency: Int) {
        _uiState.update { it.copy(looseningFrequency = frequency) }
    }

    fun onTakingPhotoFrequencyChange(frequency: Int) {
        _uiState.update { it.copy(takingPhotoFrequency = frequency) }
    }

    fun onImageClick() {
        _uiState.update { it.copy(showImagePickerDialog = true) }
    }

    fun onImagePickerDismiss() {
        _uiState.update { it.copy(showImagePickerDialog = false) }
    }

    fun onGalleryClick() {
        // TODO: Implement gallery picker
        _uiState.update { it.copy(showImagePickerDialog = false) }
    }

    fun onCameraClick() {
        // TODO: Implement camera
        _uiState.update { it.copy(showImagePickerDialog = false) }
    }

    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            try {
                val newSavedUri = saveUriInStorageUseCase.saveImage(uri)
                _uiState.update { it.copy(selectedImageUri = newSavedUri) }
            } catch (exception: IOException) {
                Timber.e(exception)
                _events.emit(Event.ShowError(R.string.error_copying_image))
            }
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            val plantName = _uiState.value.plantName
            val speciesName = _uiState.value.speciesName
            val frequencies = PlantTaskFrequencies(
                wateringFrequency = _uiState.value.wateringFrequency,
                sprayingFrequency = _uiState.value.sprayingFrequency,
                looseningFrequency = _uiState.value.looseningFrequency,
                takingPhotoFrequency = _uiState.value.takingPhotoFrequency,
            )

            val validationResult = validator.validate(
                plantName,
                speciesName,
                frequencies,
            )

            when (validationResult) {
                is PlantCreationValidator.ValidatorOutput.Success -> {
                    try {
                        _uiState.update { it.copy(isLoading = true) }
                        addPlant(
                            plantName,
                            speciesName,
                            selectedPicture.value,
                            frequencies,
                        )
                    } catch (e: Exception) {
                        Timber.e(e)
                    } finally {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }

                is PlantCreationValidator.ValidatorOutput.Error -> {
                    _events.emit(Event.ShowError(validationResult.errorMessageRes))
                }
            }
        }
    }

    fun onImageCaptured(uri: Uri) {
        _selectedPicture.value = uri
    }


    @Suppress("LongParameterList")
    private suspend fun addPlant(
        plantName: String,
        speciesName: String,
        plantPicture: Uri?,
        frequencies: PlantTaskFrequencies
    ) {
        try {
            val createdPlant = Plant(
                Plant.Name(plantName),
                speciesName,
                plantPicture?.toString()
            )

            plantsRepository.addPlant(createdPlant)
            addTasks(createdPlant, frequencies)

            _events.emit(Event.NavigateBack)
        } catch (e: Exception) {
            Timber.e(e)
            _events.emit(Event.ShowError(R.string.error_indistinctive_name))
        }
    }

    private suspend fun addTasks(
        plant: Plant,
        frequencies: PlantTaskFrequencies
    ) {
        val todayDate = Date()
        tasksRepository.addTasks(
            plant,
            listOf(
                Task.WateringTask(frequencies.wateringFrequency!!, todayDate),
                Task.SprayingTask(frequencies.sprayingFrequency!!, todayDate),
                Task.LooseningTask(frequencies.looseningFrequency!!, todayDate),
                Task.TakingPhotoTask(frequencies.takingPhotoFrequency!!, todayDate)
            )
        )
    }

    companion object {
        private const val MIN_WATERING_FREQUENCY = 1
        private const val MAX_WATERING_FREQUENCY = 31
    }
}

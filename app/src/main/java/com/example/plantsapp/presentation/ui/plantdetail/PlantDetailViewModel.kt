package com.example.plantsapp.presentation.ui.plantdetail

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.domain.repository.PlantPhotosRepository
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.repository.TasksRepository
import com.example.plantsapp.domain.usecase.DeletePlantUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

@Suppress("TooGenericExceptionCaught")
class PlantDetailViewModel @AssistedInject constructor(
    @FirebaseQualifier private val plantsRepository: PlantsRepository,
    @FirebaseQualifier private val tasksRepository: TasksRepository,
    @FirebaseQualifier private val plantPhotosRepository: PlantPhotosRepository,
    private val deletePlantUseCase: DeletePlantUseCase,
    @Assisted plantName: String
) : ViewModel() {

    data class UiState(
        val plantName: String,
        val detailState: PlantDetailState = PlantDetailState.Loading,
        val photosState: PhotosState = PhotosState.Loading,
    ) {
        sealed class PlantDetailState {
            data object Loading : PlantDetailState()
            data class DataIsLoaded(val plant: Plant, val tasks: List<Task>) : PlantDetailState()
        }

        sealed class PhotosState {
            data object Loading : PhotosState()
            data class Photos(val photos: List<Pair<Uri, Date>>) : PhotosState()
        }
    }

    private val _uiState = MutableStateFlow<UiState>(UiState(plantName))
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen: SharedFlow<Unit> get() = _closeScreen.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                plantsRepository.getPlantByName(Plant.Name(plantName)).let { plant ->
                    launch {
                        val tasks = tasksRepository.getTasksForPlant(plant)

                        _uiState.update {
                            it.copy(
                                detailState = UiState.PlantDetailState.DataIsLoaded(
                                    plant = plant,
                                    tasks = tasks,
                                )
                            )
                        }
                    }
                    launch {
                        val photos = plantPhotosRepository
                            .getPlantPhotos(plant)
                            .sortedByDescending { (_, creationDate) -> creationDate.time }

                        _uiState.update {
                            it.copy(
                                photosState = UiState.PhotosState.Photos(photos = photos)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            val plant = (_uiState.value.detailState as? UiState.PlantDetailState.DataIsLoaded)
                ?.plant ?: return@launch

            try {
                _uiState.update { it.copy(detailState = UiState.PlantDetailState.Loading) }

                deletePlantUseCase(plant)

                _closeScreen.emit(Unit)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}

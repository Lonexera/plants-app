package com.example.plantsapp.presentation.ui.plantdetail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    sealed class PlantDetailUiState {
        object InitialState : PlantDetailUiState()
        data class DataIsLoaded(val plant: Plant, val tasks: List<Task>) : PlantDetailUiState()
        object LoadingState : PlantDetailUiState()
        object FinalState : PlantDetailUiState()
    }

    val appBarTitle: LiveData<String> = MutableLiveData(plantName)

    private val _plantDetailUiState: MutableLiveData<PlantDetailUiState> =
        MutableLiveData(PlantDetailUiState.InitialState)
    val plantDetailUiState: LiveData<PlantDetailUiState> get() = _plantDetailUiState

    sealed class PlantPhotosUiState {
        object InitialState : PlantPhotosUiState()
        data class PhotosAreLoaded(val photos: List<Pair<Uri, Date>>) : PlantPhotosUiState()
    }

    private val _plantPhotosUiState: MutableLiveData<PlantPhotosUiState> =
        MutableLiveData(PlantPhotosUiState.InitialState)
    val plantPhotosUiState: LiveData<PlantPhotosUiState> get() = _plantPhotosUiState

    private lateinit var plant: Plant

    init {
        viewModelScope.launch {
            try {
                plantsRepository.getPlantByName(Plant.Name(plantName)).run {
                    plant = this
                    val tasks = tasksRepository.getTasksForPlant(plant)

                    _plantDetailUiState.value = PlantDetailUiState.DataIsLoaded(
                        plant = plant,
                        tasks = tasks
                    )

                    _plantPhotosUiState.value = PlantPhotosUiState.PhotosAreLoaded(
                        photos = plantPhotosRepository
                            .getPlantPhotos(plant)
                            .sortedByDescending { (_, creationDate) -> creationDate.time }
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            try {
                _plantDetailUiState.value = PlantDetailUiState.LoadingState

                deletePlantUseCase(plant)
                _plantDetailUiState.value = PlantDetailUiState.FinalState
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}

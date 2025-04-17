package com.example.plantsapp.presentation.ui.tasks

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.domain.repository.PlantPhotosRepository
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.usecase.CompleteTaskUseCase
import com.example.plantsapp.domain.usecase.GetTasksForPlantAndDateUseCase
import com.example.plantsapp.presentation.core.Event
import com.example.plantsapp.presentation.model.TaskWithState
import com.example.plantsapp.presentation.ui.utils.isSameDay
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

@Suppress("TooGenericExceptionCaught")
class TasksViewModel @AssistedInject constructor(
    @FirebaseQualifier private val plantsRepository: PlantsRepository,
    @FirebaseQualifier private val plantPhotosRepository: PlantPhotosRepository,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val getTasksForPlantAndDateUseCase: GetTasksForPlantAndDateUseCase,
    @Assisted private val date: Date
) : ViewModel() {

    sealed class TasksUiState {
        object InitialState : TasksUiState()
        data class DataIsLoaded(
            val plantsWithTasks: List<Pair<Plant, List<TaskWithState>>>
        ) : TasksUiState()

        object Loading : TasksUiState()
    }

    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState.InitialState)
    val uiState: StateFlow<TasksUiState> get() = _uiState.asStateFlow()

    private val _launchCamera: MutableSharedFlow<Event<Unit>> = MutableSharedFlow()
    val launchCamera: SharedFlow<Event<Unit>> = _launchCamera
    private var takingPhotoTaskWithPlant: Pair<Plant, Task>? = null

    init {
        viewModelScope.launch {
            try {
                val plantsWithTasks = fetchTasks()
                _uiState.update { TasksUiState.DataIsLoaded(plantsWithTasks) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun onCompleteTaskClicked(plant: Plant, task: Task) {
        viewModelScope.launch {
            when (task) {
                is Task.TakingPhotoTask -> {
                    takingPhotoTaskWithPlant = plant to task
                    _launchCamera.emit(Event(Unit))
                }

                else -> {
                    try {
                        _uiState.update { TasksUiState.Loading }
                        completeTask(plant, task)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    fun onImageCaptured(uri: Uri) {
        viewModelScope.launch {
            try {
                _uiState.update { TasksUiState.Loading }

                val (plant, task) = takingPhotoTaskWithPlant
                    ?: throw IllegalStateException("Cannot access stored plant and task for taking photo")

                plantPhotosRepository.savePhoto(plant, uri)
                completeTask(plant, task)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private suspend fun completeTask(plant: Plant, task: Task) {
        completeTaskUseCase(plant, task, date)

        _uiState.update {
            TasksUiState.DataIsLoaded(
                plantsWithTasks = fetchTasks()
            )
        }
    }

    private suspend fun fetchTasks(): List<Pair<Plant, List<TaskWithState>>> {
        return plantsRepository.fetchPlants()
            .map { plant ->
                plant to
                        getTasksForPlantAndDateUseCase(plant, date)
                            .map { it.toTaskWithState() }
            }
            .filter { it.second.isNotEmpty() }
    }

    private fun Pair<Task, Boolean>.toTaskWithState(): TaskWithState {
        val (task, isCompleted) = this
        return TaskWithState(
            task = task,
            isCompleted = isCompleted,
            isCompletable = date.isSameDay(Date())
        )
    }
}

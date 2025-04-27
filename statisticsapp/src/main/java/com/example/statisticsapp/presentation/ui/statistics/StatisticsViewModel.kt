package com.example.statisticsapp.presentation.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.statisticsapp.R
import com.example.statisticsapp.domain.usecase.GetPlantsUseCase
import com.example.statisticsapp.domain.usecase.GetTaskCompletionsAmountUseCase
import com.example.statisticsapp.domain.usecase.GetTasksForPlantUseCase
import com.example.statisticsapp.presentation.core.Event
import com.example.statisticsapp.presentation.model.PlantStatisticsInfo
import com.example.statisticsapp.presentation.model.TaskStatisticsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@Suppress("TooGenericExceptionCaught")
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val getTasksForPlantUseCase: GetTasksForPlantUseCase,
    private val getTaskCompletionsAmountUseCase: GetTaskCompletionsAmountUseCase
) : ViewModel() {

    private val _plantsWithTasksStats: MutableStateFlow<List<PlantStatisticsInfo>> =
        MutableStateFlow(emptyList())
    val plantsWithTasksStatistics: StateFlow<List<PlantStatisticsInfo>> get() = _plantsWithTasksStats.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error: MutableSharedFlow<Event<Int>> = MutableSharedFlow()
    val error: SharedFlow<Event<Int>> get() = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                _isLoading.update { true }
                _plantsWithTasksStats.update { fetchPlantsStatisticsInfo() }
            } catch (e: Exception) {
                Timber.e(e)
                _error.emit(Event(R.string.error_data_loading))
            } finally {
                _isLoading.update { false }
            }
        }
    }

    private suspend fun fetchPlantsStatisticsInfo(): List<PlantStatisticsInfo> {
        return getPlantsUseCase().map { plant ->
                val tasks = getTasksForPlantUseCase(plant).map { task ->
                    TaskStatisticsInfo(
                        task = task,
                        amount = getTaskCompletionsAmountUseCase(plant, task)
                    )
                }

                PlantStatisticsInfo(plant, tasks)
            }
        }
}

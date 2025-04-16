package com.example.plantsapp.presentation.ui.plants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.repository.PlantsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantsViewModel @Inject constructor(
    @FirebaseQualifier private val repository: PlantsRepository
) : ViewModel() {

    data class UiState(
        val plants: List<Plant> = emptyList(),
        val searchQuery: String = "",
        val isLoading: Boolean = false,
    )

    sealed interface Event {
        data class NavigateToPlantDetail(val plant: Plant) : Event
        data object NavigateToPlantCreation : Event
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> get() = _event.asSharedFlow()

    private val allPlants = repository.observePlants()

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        combine(
            allPlants,
            _uiState.map { it.searchQuery }.distinctUntilChanged()
        ) { allPlants, searchQuery ->
            showLoading()

            val filteredPlants = allPlants.filter {
                it.name.value.contains(searchQuery.trim(), ignoreCase = true) ||
                        it.speciesName.contains(searchQuery.trim(), ignoreCase = true)
            }

            _uiState.update {
                it.copy(
                    plants = filteredPlants,
                    searchQuery = searchQuery,
                )
            }

            hideLoading()
        }
            .launchIn(viewModelScope)
    }

    private fun showLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun hideLoading() {
        _uiState.update { it.copy(isLoading = false) }
    }

    fun onPlantClicked(plant: Plant) {
        viewModelScope.launch {
            _event.emit(Event.NavigateToPlantDetail(plant))
        }
    }

    fun onAddPlantClicked() {
        viewModelScope.launch {
            _event.emit(Event.NavigateToPlantCreation)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}

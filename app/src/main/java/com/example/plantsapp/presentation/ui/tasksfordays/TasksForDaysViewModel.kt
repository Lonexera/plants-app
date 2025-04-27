package com.example.plantsapp.presentation.ui.tasksfordays

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.presentation.core.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TasksForDaysViewModel @Inject constructor(
    private val statisticsAppResolver: StatisticsAppResolver
) : ViewModel() {

    val todayDate: StateFlow<Date> = MutableStateFlow(Date())

    val isStatisticsAppInstalled: StateFlow<Boolean> = MutableStateFlow(
        statisticsAppResolver.isAppInstalled()
    )

    private val _launchStatisticsApp: MutableSharedFlow<Event<Intent>> = MutableSharedFlow()
    val launchStatisticsApp: SharedFlow<Event<Intent>>get() = _launchStatisticsApp

    fun onStatisticsClicked() {
        viewModelScope.launch {
            _launchStatisticsApp.emit(Event(statisticsAppResolver.requireIntent()))
        }
    }
}

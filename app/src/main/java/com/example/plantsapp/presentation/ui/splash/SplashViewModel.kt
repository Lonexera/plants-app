package com.example.plantsapp.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.repository.UserRepository
import com.example.plantsapp.presentation.core.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @FirebaseQualifier private val userRepository: UserRepository
) : ViewModel() {

    enum class Directions {
        AUTH_SCREEN,
        TASKS_SCREEN
    }

    private val _navigate: MutableSharedFlow<Event<Directions>> = MutableSharedFlow()
    val navigate: SharedFlow<Event<Directions>> get() = _navigate.asSharedFlow()

    init {
        viewModelScope.launch {
            val event = if (userRepository.isUserCached()) {
                Directions.TASKS_SCREEN
            } else {
                Directions.AUTH_SCREEN
            }

            _navigate.emit(Event(event))
        }
    }
}

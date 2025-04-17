package com.example.plantsapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.User
import com.example.plantsapp.domain.repository.UserRepository
import com.example.plantsapp.domain.usecase.SignOutUseCase
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
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    @FirebaseQualifier private val userRepository: UserRepository,
) : ViewModel() {

    data class UiState(val user: User?)

    private val _uiState = MutableStateFlow<UiState>(UiState(null))
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _navigateToAuth = MutableSharedFlow<Unit>()
    val navigateToAuth: SharedFlow<Unit> get() = _navigateToAuth.asSharedFlow()

    init {
        try {
            _uiState.update {
                it.copy(user = userRepository.requireUser())
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun onSignOut() {
        viewModelScope.launch {
            signOutUseCase()
            _navigateToAuth.emit(Unit)
        }
    }
}

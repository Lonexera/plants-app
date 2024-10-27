package com.example.plantsapp.presentation.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.R
import com.example.plantsapp.domain.usecase.AuthUseCase
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
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    sealed class AuthState {
        data class Auth(
            val email: String = "",
            val password: String = "",
        ) : AuthState()

        object Loading : AuthState()
        object NavigateToTasks : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Auth())
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    private val _error = MutableSharedFlow<Int>()
    val error: SharedFlow<Int> get() = _error.asSharedFlow()

    @Suppress("TooGenericExceptionCaught")
    fun onSignInResult(token: String?) {
        viewModelScope.launch {
            if (token == null) {
                _error.emit(R.string.error_unable_to_sign_in)
                return@launch
            }

            try {
                _authState.update { AuthState.Loading }
                authUseCase(AuthUseCase.AuthInput.Token(token))
                _authState.update { AuthState.NavigateToTasks }
            } catch (e: Exception) {
                _error.emit(R.string.error_unable_to_sign_in)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _error.emit(R.string.error_unable_to_sign_in)
                return@launch
            }

            try {
                _authState.update { AuthState.Loading }
                authUseCase(
                    AuthUseCase.AuthInput.EmailPassword(
                        email = email,
                        password = password,
                    )
                )
                _authState.update { AuthState.NavigateToTasks }
            } catch (e: Exception) {
                Timber.e(e)
                _error.emit(R.string.error_unable_to_sign_in)
            }
        }
    }
}

package com.example.plantsapp.domain.usecase

interface AuthUseCase {

    sealed class AuthInput {
        data class Token(val token: String) : AuthInput()
        data class EmailPassword(val email: String, val password: String) : AuthInput()
    }

    suspend operator fun invoke(input: AuthInput)
}

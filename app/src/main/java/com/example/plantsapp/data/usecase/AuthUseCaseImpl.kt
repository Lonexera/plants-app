package com.example.plantsapp.data.usecase

import com.example.plantsapp.data.firebase.utils.toUser
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.repository.UserRepository
import com.example.plantsapp.domain.usecase.AuthUseCase
import com.example.plantsapp.domain.workmanager.TasksWorkManager
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException
import java.util.Calendar
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @FirebaseQualifier private val userRepository: UserRepository,
    private val tasksWorkManager: TasksWorkManager
) : AuthUseCase {

    @Throws(IllegalStateException::class)
    override suspend fun invoke(input: AuthUseCase.AuthInput) {
        when (input) {
            is AuthUseCase.AuthInput.Token -> input.getSignedInFirebaseUser()
            is AuthUseCase.AuthInput.EmailPassword -> input.getSignedInFirebaseUser()
        }
            ?.let {
                userRepository.setUser(it.toUser())
                tasksWorkManager.startWork(startDate = Calendar.getInstance())
            } ?: throw IllegalStateException("Cannot sign in")
    }

    private suspend fun AuthUseCase.AuthInput.Token.getSignedInFirebaseUser(): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(this.token, null)
        return auth.signInWithCredential(credential)
            .await()
            .user
    }

    private suspend fun AuthUseCase.AuthInput.EmailPassword.getSignedInFirebaseUser(): FirebaseUser? {
        val credential = EmailAuthProvider.getCredential(this.email, this.password)
        return auth.signInWithCredential(credential)
            .await()
            .user
    }
}

package com.example.plantsapp.data.firebase.repository

import android.content.SharedPreferences
import com.example.plantsapp.data.firebase.utils.user
import com.example.plantsapp.domain.model.User
import com.example.plantsapp.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepository @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : UserRepository {

    private var user: User? = sharedPrefs.user

    override suspend fun setUser(user: User) {
        sharedPrefs.user = user
        this.user = user
    }

    override fun requireUser(): User =
        user ?: throw IllegalStateException("User is not authorized!")

    override fun isUserCached(): Boolean = (user != null)

    override suspend fun clearUser() {
        sharedPrefs.user = null
        user = null
    }
}

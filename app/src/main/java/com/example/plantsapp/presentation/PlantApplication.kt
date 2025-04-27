package com.example.plantsapp.presentation

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.plantsapp.BuildConfig
import com.example.plantsapp.presentation.ui.notification.PlantCareNotificationManager
import com.example.plantsapp.presentation.ui.permission.PermissionHandler
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlantApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var notificationManager: PlantCareNotificationManager

    @Inject
    lateinit var permissionHandler: PermissionHandler

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        if (permissionHandler.hasNotificationPermission()) {
            notificationManager.scheduleDailyNotification()
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

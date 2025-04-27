package com.example.plantsapp.presentation.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.plantsapp.R
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.usecase.GetTasksForPlantAndDateUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Date

@HiltWorker
class PlantCareNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    @FirebaseQualifier private val plantsRepository: PlantsRepository,
    private val getTasksForPlantAndDateUseCase: GetTasksForPlantAndDateUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val plantsWithTasks = plantsRepository.fetchPlants()
                .map { plant ->
                    plant to getTasksForPlantAndDateUseCase(plant, Date())
                }
                .filter { it.second.isNotEmpty() }

            if (plantsWithTasks.isNotEmpty()) {
                showNotification(plantsWithTasks)
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun showNotification(plantsWithTasks: List<Pair<Plant, List<Pair<Task, Boolean>>>>) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_plant_24)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(buildNotificationText(plantsWithTasks))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildNotificationText(plantsWithTasks: List<Pair<Plant, List<Pair<Task, Boolean>>>>): String {
        val notCompletedTasks = plantsWithTasks
            .flatMap { (plant, tasks) ->
                tasks.filter { !it.second }
                    .map { (task, _) -> plant to task }
            }

        return if (notCompletedTasks.isEmpty()) {
            context.getString(R.string.notification_no_tasks)
        } else {
            context.getString(
                R.string.notification_tasks_remaining,
                notCompletedTasks.size
            )
        }
    }

    companion object {
        private const val CHANNEL_ID = "plant_care_channel"
        private const val NOTIFICATION_ID = 1
    }
} 
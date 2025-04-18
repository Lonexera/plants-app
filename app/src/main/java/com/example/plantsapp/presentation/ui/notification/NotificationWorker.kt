package com.example.plantsapp.presentation.ui.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.usecase.GetTasksForPlantAndDateUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Date

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    @FirebaseQualifier private val plantsRepository: PlantsRepository,
    private val getTasksForPlantAndDateUseCase: GetTasksForPlantAndDateUseCase,
    private val notificationManager: TaskNotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val plantsWithTasks =
            plantsRepository.fetchPlants()
                .map { plant ->
                    plant to getTasksForPlantAndDateUseCase(plant, Date())
                }
                .filter { it.second.isNotEmpty() }

        plantsWithTasks.forEach { (plant, tasksWithState) ->
            val tasks = tasksWithState
                .filter { (_, isCompleted) -> !isCompleted }
                .map { (task, _) -> task }

            if (tasks.isNotEmpty()) {
                notificationManager.showTaskNotifications(plant, tasks)
            }
        }

        return Result.success()
    }

    companion object {
        const val NOTIFICATION_WORK_NAME = "com.example.plantsapp.NOTIFICATION_WORK"
    }
}

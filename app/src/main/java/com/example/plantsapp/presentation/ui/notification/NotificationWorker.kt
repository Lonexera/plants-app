package com.example.plantsapp.presentation.ui.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.usecase.GetTasksForPlantForDateUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Date

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val plantsRepository: PlantsRepository,
    private val getTasksForPlantForDateUseCase: GetTasksForPlantForDateUseCase,
    private val notificationManager: TaskNotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val plantsWithTasks =
            plantsRepository.fetchPlants()
                .map { plant ->
                    plant to getTasksForPlantForDateUseCase(plant, Date())
                }
                .filter { it.second.isNotEmpty() }

        plantsWithTasks.forEach { (plant, tasksWithState) ->
            val tasks = tasksWithState
                .filter { it.isCompleted == false }
                .map { it.task }

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

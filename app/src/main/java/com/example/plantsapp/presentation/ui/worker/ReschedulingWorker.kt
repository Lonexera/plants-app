package com.example.plantsapp.presentation.ui.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.repository.TasksRepository
import com.example.plantsapp.domain.usecase.GetTasksForPlantAndDateUseCase
import com.example.plantsapp.presentation.ui.utils.minusDays
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Date

@HiltWorker
class ReschedulingWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    @FirebaseQualifier private val plantsRepository: PlantsRepository,
    private val getTasksForPlantAndDateUseCase: GetTasksForPlantAndDateUseCase,
    @FirebaseQualifier private val tasksRepository: TasksRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val currentDate = Date()

        plantsRepository.fetchPlants()
            .forEach { plant ->
                getTasksForPlantAndDateUseCase(plant, currentDate.minusDays(1))
                    .filter { (_, isCompleted) -> !isCompleted }
                    .forEach { (task, _) ->
                        tasksRepository.updateTask(plant, task, currentDate)
                    }
            }

        return Result.success()
    }

    companion object {
        const val RESCHEDULING_WORK_NAME = "com.example.plantsapp.RESCHEDULING_WORK"
    }
}

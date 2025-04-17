package com.example.plantsapp.di.module

import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.presentation.ui.plantdetail.PlantDetailViewModel
import com.example.plantsapp.presentation.ui.tasks.TasksViewModel
import dagger.assisted.AssistedFactory
import java.util.Date

@AssistedFactory
interface AssistedPlantDetailViewModel {
    fun create(plantName: Plant.Name): PlantDetailViewModel
}

@AssistedFactory
interface AssistedTasksViewModel {
    fun create(date: Date): TasksViewModel
}

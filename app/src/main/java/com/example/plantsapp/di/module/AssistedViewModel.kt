package com.example.plantsapp.di.module

import com.example.plantsapp.presentation.ui.tasks.TasksViewModel
import dagger.assisted.AssistedFactory
import java.util.Date

@AssistedFactory
interface AssistedTasksViewModel {
    fun create(date: Date): TasksViewModel
}

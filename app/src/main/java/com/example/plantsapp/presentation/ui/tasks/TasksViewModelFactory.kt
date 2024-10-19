package com.example.plantsapp.presentation.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import java.util.Date

class TasksViewModelFactory(
    private val assistedFactory: TasksViewModelAssistedFactory,
    private val date: Date
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return assistedFactory.create(date) as T
        } else {
            throw IllegalArgumentException("ViewModel Class Is Not Found!")
        }
    }
}

@AssistedFactory
interface TasksViewModelAssistedFactory {

    fun create(date: Date): TasksViewModel
}

package com.example.plantsapp.presentation.ui.plantdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantsapp.domain.model.Plant
import dagger.assisted.AssistedFactory

class DetailViewModelFactory(
    private val assistedFactory: DetailViewModelAssistedFactory,
    private val plantName: Plant.Name
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantDetailViewModel::class.java)) {
            return assistedFactory.create(plantName.value) as T
        } else {
            throw IllegalArgumentException("ViewModel Class Is Not Found!")
        }
    }
}

@AssistedFactory
interface DetailViewModelAssistedFactory {
    fun create(plantName: String): PlantDetailViewModel
}

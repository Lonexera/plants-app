package com.example.plantsapp.presentation.ui.plantcreation

import androidx.annotation.StringRes
import com.example.plantsapp.R
import javax.inject.Inject

class PlantCreationValidator @Inject constructor() {

    fun validate(
        plantName: String,
        speciesName: String,
        frequencies: PlantCreationViewModel.PlantTaskFrequencies
    ): ValidatorOutput {
        return when {
            plantName.isBlank() -> ValidatorOutput.Error(R.string.error_invalid_name)
            speciesName.isBlank() -> ValidatorOutput.Error(R.string.error_invalid_species)
            frequencies.wateringFrequency == null -> ValidatorOutput.Error(R.string.error_invalid_watering_frequency)
            frequencies.sprayingFrequency == null -> ValidatorOutput.Error(R.string.error_invalid_spraying_frequency)
            frequencies.looseningFrequency == null -> ValidatorOutput.Error(R.string.error_invalid_loosening_frequency)
            frequencies.takingPhotoFrequency == null ->
                ValidatorOutput.Error(R.string.error_invalid_taking_photo_frequency)
            else -> ValidatorOutput.Success
        }
    }

    sealed class ValidatorOutput {
        object Success : ValidatorOutput()
        data class Error(
            @StringRes val errorMessageRes: Int
        ) : ValidatorOutput()
    }
}

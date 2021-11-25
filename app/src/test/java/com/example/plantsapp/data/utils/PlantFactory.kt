package com.example.plantsapp.data.utils

import android.net.Uri
import com.example.plantsapp.domain.model.Plant
import java.util.Date

internal fun createPlant(
    name: String = "",
    speciesName: String = "",
    plantPicture: Uri? = null,
    creationDate: Date = Date()
): Plant {
    return Plant(
        name = Plant.Name(name),
        speciesName = speciesName,
        plantPicture = plantPicture,
        creationDate = creationDate
    )
}

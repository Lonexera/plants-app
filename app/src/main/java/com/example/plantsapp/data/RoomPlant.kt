package com.example.plantsapp.data

import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plantsapp.domain.model.Plant
import java.util.Date

@Entity(tableName = "plants")
data class RoomPlant(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "species_name") val speciesName: String,
    @ColumnInfo(name = "plant_picture") val plantPicture: String?,
    @ColumnInfo(name = "watering_frequency") val wateringFrequencyDays: Int,
    @ColumnInfo(name = "spraying_frequency") val sprayingFrequencyDays: Int,
    @ColumnInfo(name = "loosening_frequency") val looseningFrequencyDays: Int,
    @ColumnInfo(name = "creation_date") val creationDateMillis: Long

) {
    companion object {
        fun from(plant: Plant, creationDate: Date): RoomPlant {
            return RoomPlant(
                plant.name.value,
                plant.speciesName,
                plant.plantPicture?.toString(),
                plant.wateringFrequencyDays,
                plant.sprayingFrequency,
                plant.looseningFrequency,
                creationDate.time
            )
        }
    }

    fun toPlant(): Plant {
        return Plant(
            Plant.Name(name),
            speciesName,
            plantPicture?.toUri(),
            wateringFrequencyDays,
            sprayingFrequencyDays,
            looseningFrequencyDays
        )
    }
}

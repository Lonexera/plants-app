package com.example.plantsapp.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.domain.model.TaskKeys
import java.util.Date

@Deprecated("Room will no longer be used in app")
@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            onDelete = CASCADE,
            entity = RoomPlant::class,
            parentColumns = ["name"],
            childColumns = ["plantName"]
        )
    ]
)
data class RoomTask(
    val taskKey: String,
    val plantName: String,
    val frequency: Int,
    val lastUpdateDate: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun toTask(): Task {
        return TaskKeys.getFromKey(taskKey).toTask(
            taskId = id,
            frequency = frequency,
            lastUpdateDate = Date(lastUpdateDate)
        )
    }

    companion object {
        fun from(plant: Plant, task: Task): RoomTask {
            return RoomTask(
                TaskKeys.from(task).key,
                plant.name.value,
                task.frequency,
                task.lastUpdateDate.time
            )
        }
    }
}

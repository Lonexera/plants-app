package com.example.plantsapp.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.plantsapp.domain.model.Task
import java.util.Date

@Deprecated("Room will no longer be used in app")
@Entity(
    tableName = "taskHistory",
    foreignKeys = [
        ForeignKey(
            onDelete = CASCADE,
            entity = RoomTask::class,
            parentColumns = ["id"],
            childColumns = ["taskId"]
        )
    ]
)
data class RoomTaskCompletion(
    val taskId: Int,
    val completionDate: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        fun fromTask(task: Task, completionDate: Date): RoomTaskCompletion {
            return RoomTaskCompletion(
                taskId = task.id,
                completionDate = completionDate.time
            )
        }
    }
}

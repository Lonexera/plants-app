package com.example.plantsapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.plantsapp.data.entity.RoomTask

@Dao
interface RoomTasksDao {

    @Transaction
    @Query("SELECT * FROM tasks WHERE tasks.plantName = :plantName")
    suspend fun getTasksForPlantName(plantName: String): List<RoomTask>

    // TODO change this to @Update annotation and provide here RoomTask
    @Query("UPDATE tasks " +
            "SET lastUpdateDate = :completionDate " +
            "WHERE id = :taskId")
    suspend fun update(taskId: Int, completionDate: Long)

    @Insert
    suspend fun insert(tasks: List<RoomTask>)
}

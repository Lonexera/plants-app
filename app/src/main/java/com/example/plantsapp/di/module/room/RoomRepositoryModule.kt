package com.example.plantsapp.di.module.room

import com.example.plantsapp.data.room.repository.RoomPlantsRepository
import com.example.plantsapp.data.room.repository.RoomTasksHistoryRepository
import com.example.plantsapp.data.room.repository.RoomTasksRepository
import com.example.plantsapp.di.module.RoomQualifier
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.repository.TasksHistoryRepository
import com.example.plantsapp.domain.repository.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RoomRepositoryModule {

    @[Binds RoomQualifier]
    fun bindPlantsRepository(repo: RoomPlantsRepository): PlantsRepository

    @[Binds RoomQualifier]
    fun bindTasksRepository(repo: RoomTasksRepository): TasksRepository

    @[Binds RoomQualifier]
    fun bindTasksHistoryRepository(repo: RoomTasksHistoryRepository): TasksHistoryRepository
}

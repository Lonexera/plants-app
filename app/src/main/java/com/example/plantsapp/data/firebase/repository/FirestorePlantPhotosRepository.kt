package com.example.plantsapp.data.firebase.repository

import android.net.Uri
import com.example.plantsapp.data.firebase.utils.addImage
import com.example.plantsapp.data.firebase.utils.getPlantImagesStoragePath
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.repository.PlantPhotosRepository
import com.example.plantsapp.domain.repository.UserRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class FirestorePlantPhotosRepository @Inject constructor(
    private val storage: FirebaseStorage,
    @FirebaseQualifier private val userRepository: UserRepository
) : PlantPhotosRepository {

    private val storageRef = storage.reference

    override suspend fun savePhoto(plant: Plant, photoUri: Uri) {
        storageRef.addImage(
            user = userRepository.requireUser(),
            plantName = plant.name,
            picture = photoUri
        )
    }

    override suspend fun getPlantPhotos(plant: Plant): List<Pair<Uri, Date>> {
        return getPlantPhotosFolder(plant)
            .listAll()
            .await()
            .items
            .map {
                val imageUrl = it.downloadUrl.await()
                val creationDate = Date(it.metadata.await().creationTimeMillis)

                imageUrl to creationDate
            }
    }

    override suspend fun deletePlantPhotos(plant: Plant) {
        getPlantPhotosFolder(plant)
            .listAll()
            .await()
            .items
            .forEach { it.delete().await() }
    }

    private fun getPlantPhotosFolder(plant: Plant): StorageReference {
        return storageRef
            .child(
                getPlantImagesStoragePath(
                    userUid = userRepository.requireUser().uid,
                    plantName = plant.name.value
                )
            )
    }
}

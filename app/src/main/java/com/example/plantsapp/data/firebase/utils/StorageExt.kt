package com.example.plantsapp.data.firebase.utils

import android.net.Uri
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.User
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

@Throws(IllegalArgumentException::class)
suspend fun StorageReference.addImage(user: User, plantName: Plant.Name, picture: Uri): Uri {
    val storageImagePath = getPictureStoragePath(
        userUid = user.uid,
        pictureName = picture.lastPathSegment
            ?: throw IllegalArgumentException("Provided picture uri has unsupported name!"),
        plantName = plantName.value
    )
    return child(storageImagePath)
        .apply {
            putFile(picture)
                .await()
        }
        .downloadUrl
        .await()
}

fun getPlantImagesStoragePath(
    userUid: String,
    plantName: String
): String {
    return buildString {
        append(userUid)
        append("/")
        append(STORAGE_PICTURES_DIR_PATH)
        append(plantName)
    }
}

private fun getPictureStoragePath(
    userUid: String,
    plantName: String,
    pictureName: String
): String {
    return buildString {
        append(getPlantImagesStoragePath(userUid, plantName))
        append("/")
        append(pictureName)
    }
}

private const val STORAGE_PICTURES_DIR_PATH = "images/"

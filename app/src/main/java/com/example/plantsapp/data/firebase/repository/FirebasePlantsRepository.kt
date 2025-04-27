package com.example.plantsapp.data.firebase.repository

import androidx.core.net.toUri
import com.example.plantsapp.data.firebase.entity.FirebasePlant
import com.example.plantsapp.data.firebase.utils.addImage
import com.example.plantsapp.di.module.FirebaseQualifier
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.repository.PlantsRepository
import com.example.plantsapp.domain.repository.UserRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebasePlantsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    @FirebaseQualifier private val userRepository: UserRepository
) : PlantsRepository {

    private val plantsCollection
        get() = firestore
            .collection(KEY_COLLECTION_USERS)
            .document(userRepository.requireUser().uid)
            .collection(KEY_COLLECTION_PLANTS)

    private val storageRef = storage.reference

    override fun observePlants(): Flow<List<Plant>> {
        return callbackFlow {
            val subscription = plantsCollection
                .addSnapshotListener { value, error ->
                    when {
                        error != null -> {
                            Timber.e("Listen failed..", error)
                        }

                        value != null -> {
                            val plants = value
                                .toObjects<FirebasePlant>()
                                .map { it.toPlant() }

                            trySend(plants)
                        }
                    }
                }

            awaitClose {
                subscription.remove()
            }
        }
    }

    override suspend fun fetchPlants(): List<Plant> {
        return plantsCollection
            .get()
            .await()
            .toObjects<FirebasePlant>()
            .map { it.toPlant() }
    }

    override suspend fun addPlant(plant: Plant) {
        val storageImageUri = plant.plantPicture?.let {
            storageRef.addImage(
                user = userRepository.requireUser(),
                plantName = plant.name,
                picture = it.toUri()
            )
        }

        getPlantDocumentByName(plant.name)
            .set(FirebasePlant.from(plant, storageImageUri))
            .await()
    }

    override suspend fun getPlantByName(name: Plant.Name): Plant {
        return getPlantDocumentByName(name)
            .get()
            .await()
            .toObject<FirebasePlant>()
            ?.toPlant()
            ?: throw NoSuchElementException("Unable to find plant with name = $name")
    }

    override suspend fun deletePlant(plant: Plant) {
        getPlantDocumentByName(plant.name)
            .delete()
            .await()
    }

    private fun getPlantDocumentByName(plantName: Plant.Name): DocumentReference {
        return plantsCollection.document(plantName.value)
    }

    companion object {
        // TODO maybe move this collection name somewhere
        private const val KEY_COLLECTION_USERS = "users"
        private const val KEY_COLLECTION_PLANTS = "plants"
    }
}

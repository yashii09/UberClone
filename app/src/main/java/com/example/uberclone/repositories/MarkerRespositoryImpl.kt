package com.example.uberclone.repositories

import com.example.uberclone.utilities.DestinationMarker
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await


//It define how you do it in the repository
class MarkerRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): MarkerRepository {
    //override - overriding the fun declared in MarkerRepository interface
    //suspend - the function is a suspending function, so it can be called inside a coroutine without blocking the main thread

    override suspend fun addMarker(
        marker: LatLng,
        name: String
    ) {
        try {
            val data = hashMapOf(
                "latitude" to marker.latitude,
                "longitude" to marker.longitude,
            )

            //.collection - get the marker collection, create the marker collection if it is not exist
            firestore.collection("markers")
                .document(name) // creates or overwrite the document with ID equals to name
                .set(data) // add the data to the document
                .await()

        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getAllMarkersFromFirebase(): List<DestinationMarker> {
        val result = firestore
            .collection("markers").get().await()
        //.get() - get the data from the collection
        //await() - function used to return a task
        //Since firestore returns a task in asynchronous, await suspends the coroutine until the data is loaded is loaded
        return result.map { document ->
            DestinationMarker(
                id = document.id,
                latitude = document.getDouble("latitude") ?: 0.0,
                longitude = document.getDouble("longitude")?: 0.0,
                title = document.getString("title") ?: ""
            )
        }
    }


}
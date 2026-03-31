package com.example.uberclone.repositories

import com.example.uberclone.utilities.DestinationMarker
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


//It defines how you do it in the repository
class MarkerRepositoryImpl @Inject constructor(
    private val firebase: FirebaseFirestore
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
            firebase.collection("markers")
                .document(name) // creates or overwrite the document with ID equals to name
                .set(data) // add the data to the document
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Flow-is a cold asynchronous stream that emits values over time without blocking the UI thread
    // Reactive streams automatically notifies observers. Compose UI when data changes
    // Lifecycle-aware can be collected safely within compose using collect as state or collect as state with lifecycle function
    // It's part of Kotlin's coroutine library and is designed to handle sequential emission of data in a non-blocking way
    // we use flow for dynamic data updates, coroutines integration and Compose UI reactively
    // Whenever the new marker is added to your database, the flow will emit and notify UI to recompose and render and rerenders the Ui with the newly updated list
    // Flow is cancelable, meaning it stops emitting when the composable leaves the composition, avoiding the memory leaks.
    override suspend fun getAllMarkersFromFirebase(): Flow<List<DestinationMarker>> = callbackFlow {
        //in this getAllMarkersFromFirebase() we used the reactive stream of the flow and the callback style API, which is listener, and the firebase collection
        // the bridge between them is trySend and the awaitClose functions

        /* This code gets the data once during the runtime when the app launches
        val result = firestore
            .collection("markers")
            .get().await()
        //.get() - get the data from the collection
        //await() - function used to return a task
        //Since firestore returns a task in asynchronous, await suspends the coroutine until the data is loaded
         */

        // need to fetch and get real time data changes
        // attaches a real time listener to the collection
        val listener = firebase.collection("markers")
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null && !snapshot.isEmpty) {
                    // if an error occurs then error callback is executed
                    if (error != null) {
                        error.printStackTrace()
                        close(error)
                        return@addSnapshotListener
                    }
                    // whenever the data in markers changes added, modified or removed, the snapshot callback is triggered
                    if (snapshot != null && !snapshot.isEmpty) {
                        val markers = snapshot.map { myMarker ->

                            DestinationMarker(
                                id = myMarker.id,
                                latitude = myMarker.getDouble("latitude") ?: 0.0,
                                longitude = myMarker.getDouble("longitude") ?: 0.0,
                                title = myMarker.getString("title") ?: ""
                            )
                        }
                        // Sending the mapped list destination markers to the flow consumers
                        // In Kotlin coroutines when working with flows and callback based APIs like firebase firestore,
                        // trySend and await close servers special purposes to bridge between callback style APIs and reactive streams flow in this function to get all markers from firestore
                        trySend(markers)
                        // trySend is the non-blocking emission of values to a flow from a callback flow or mutable shared flow to safely emit values without suspending the coroutine
                        //we use trySend bcoz firebase'add snapshot listener uses callbacks, but we want to expose data as a flow
                    } else {
                        trySend(emptyList())
                    }

                }

            }
        // suspends the coroutine until the flow is canceled, then runs cleanup code.
        awaitClose { listener.remove() }

    }
}


package com.example.uberclone.repositories

import com.example.uberclone.utilities.DestinationMarker
import com.google.android.gms.maps.model.LatLng

interface MarkerRepository {
    // this interface define what you can do with the marker repository

    // suspend function means the function is a suspending function, so it can be called inside a coroutine
    suspend fun addMarker(marker: LatLng, name: String)

    suspend fun getAllMarkersFromFirebase() : List<DestinationMarker>

}
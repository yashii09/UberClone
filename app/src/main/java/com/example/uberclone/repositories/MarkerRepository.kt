package com.example.uberclone.repositories

import com.example.uberclone.utilities.DestinationMarker
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface MarkerRepository {
    suspend fun addMarker(marker: LatLng, name: String)
    
    // Functions returning Flow should not be suspended
    suspend fun getAllMarkersFromFirebase() : Flow<List<DestinationMarker>>
}
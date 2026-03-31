package com.example.uberclone.repositories

import androidx.compose.runtime.MutableState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.StateFlow

// This interface is used to get the current location of the user in the app.

// This interfaces defines a contract for managing the device location updates in an Android application.
// This abstract location tracking, start or stop updates, current location exposure as a reactive stream and permission state
interface LocationRepository {
    // the purpose of below line is to provide the app with real time location updates. Automatically notifies observers when location changes.
    val userLocation: StateFlow<LatLng?> // StateFlow is a hot observable flow that always holds the latest value
    val hasLocationPermission : MutableState<Boolean> // This is a compose observable  state holder tracks whether the location permissions are granted and trigger permission requests when false
    // MutableState optimized for compose UI recomposition when permission state changes, the start location updates function starts listening for location updates


    fun startLocationUpdates() // starts listening for location updates
    fun stopLocationUpdates() // stops listening for location updates to save battery when location is no longer needed or the app goes to background


}
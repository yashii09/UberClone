package com.example.uberclone.repositories

import android.Manifest
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

//This implements the LocationRepository interface
class LocationRepositoryIml @Inject  constructor(
    @ApplicationContext private val context: Context // helps to inject application context in case where you need to perform operations across different ->
    // components of your app without linking them to any specific lifecycle
)
    : LocationRepository {

        // (_) used to indicate that this variable is meant for internal use only
        // create a private, mutable state holder for the user's location
        private val _userLocation = MutableStateFlow<LatLng?>(null)
        // exposes the read-only version of the _userLocation, StateFlow<LatLng?> consumers can observe this flow but can't modify it, asStateFlow() - convert the mutable _userLocation into an immutable StateFlow for external access
        override val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

        override val hasLocationPermission = mutableStateOf(false)

        // FusedLocationProviderClient: is a Google Play service API that provide access to the device's location services
        // combining multiple location sources GPS, Wi-Fi cellular to provide optimal location data
        private val fusedLocationClient: FusedLocationProviderClient
        by lazy { // used to initialize fusedLocationClient when it is first accessed, rather than at the point of declaration, this conserves resources, especially if the object may not be needed immediately
            //used to obtain the instance of the FusedLocationProviderClient which provide access to location service
            LocationServices.getFusedLocationProviderClient((context))

        }

        // Handling incoming location updates from android's fused location provider
        // callback: receives periodic location updates & updates an observable state with the latest coordinates
        private val locationCallback = object: LocationCallback() {
            // this function called when new locations available, this contains last location and all locations
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { currentLocation ->
                    _userLocation.value = LatLng(
                        currentLocation.latitude,
                        currentLocation.longitude
                    )
                }
            }
        }

        // creating the location request
        @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        override fun startLocationUpdates() {
            val locationRequest = LocationRequest.Builder(
                // high accuracy requests the most precise location possible
                Priority.PRIORITY_HIGH_ACCURACY, 10000
            ).apply {
                setMinUpdateIntervalMillis(5000)
            }.build()

            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException){
                Log.v("TAGY", "Permission Exception ${e.message}")
            }
        }

        override fun stopLocationUpdates() {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

    }
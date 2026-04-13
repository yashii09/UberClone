package com.example.uberclone.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uberclone.repositories.DirectionsRepository
import com.example.uberclone.repositories.LocationRepository
import com.example.uberclone.repositories.MarkerRepository
import com.example.uberclone.utilities.DestinationMarker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.SuccessContinuation
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// this ViewModel needs to be created by Hilt
// A Hilt view model is an annotation you put on your ViewModel class to tell
// held dependency injection library that this ViewModel needs to be created by hilt
// automatically provides its dependencies like repositories via constructor injection
@HiltViewModel
class MapViewModel @Inject constructor(
    private val markerRepository: MarkerRepository,
    private val locationRepository: LocationRepository,
    private val directionsRepository: DirectionsRepository
) : ViewModel(){
    fun addMarker(marker: LatLng,
                  name: String,
                  onSuccess: () -> Unit,
                  onFailure: (Exception) -> Unit
    ){
        viewModelScope.launch { //this launches a coroutine bound to the View model's lifecycle
            // If the ViewModel is cleared , coroutine will be automatically canceled
            // it keep, or it keeps work off the main thread, so this will be done in background thread
            try { //try to call repository.add marker and insert marker with the name
                // if it completes successfully, call onSuccess callback to notify your UI ,
                // if anything fails, execute the catch and call onFailure callback to notify your UI
                markerRepository.addMarker(marker, name)
                onSuccess()
            }catch (e: Exception){
                onFailure(e)
            }


        }

    }
    //list of markers in firestore
    //for both compose & non-compose UI
    //private val _markers = MutableStateFlow<List<DestinationMarker>>(emptyList())
    //val markersInFirebase: StateFlow<List<DestinationMarker>> = _markers.asStateFlow()
    // State flow itself is read only, means it can be observed but not changed,
    // this means the UI can collect markers to get updates, but it can't call dot value to modify it

    //Another way to get list of markers from firebase - for compose only UI
    //it creates an observable, mutable list of destination marker items that jetpack compose can track
    // and update its UI(recomposition)
    // snapshotStateList is basically a special compose collection that notifies the composition when its content changed
    // any composable that reads 'markers' will recompose when you add, remove, update items
    val markers : SnapshotStateList<DestinationMarker> = mutableStateListOf()
    private var listenerActive = false

    init{
        loadMarkersFromFirestore()
    }

    fun loadMarkersFromFirestore(){
        if(listenerActive) return
        //Since getAllMarkersFromFirebase() is a suspend fun, we need to call it inside the coroutine scope
        viewModelScope.launch {
                // here we are listening to the real time updates from a data stream firestore in an android viewModel
                // while managing the listener state to avoid duplicate subscriptions
                // getAllMarkersFromFirebase() cancels the previous collection if a new data arrives before the previous emission is preceded and persist
                markerRepository.getAllMarkersFromFirebase().collectLatest {
                    newMarkers -> // only processes the latest data, avoiding black backlog build up
                    markers.clear() // markers.clear resets the existing list markers dot add all replaces with fresh data
                    markers.addAll(newMarkers) // replaces with fresh data
                }

        }
        listenerActive = true // marks the listener as active to prevent redundant subscriptions
    }

    // Get current location
    val userLocation = locationRepository.userLocation
    val hasLocationPermission = locationRepository.hasLocationPermission

    fun startLocationUpdates(){
        locationRepository.startLocationUpdates()
    }

    override fun onCleared() {
        locationRepository.stopLocationUpdates()
    }

    fun onPermissionResult(granted: Boolean){
        hasLocationPermission.value = granted
        if(granted){
            startLocationUpdates()
        }
    }

    /******************************************************************************/

    // Directions API


    // current location is stored here
    private val _selectedLocation = MutableStateFlow<LatLng?>(null)
    val selectedLocation: StateFlow<LatLng?> = _selectedLocation

    // this updates the _selectedLocation with the new LatLng value when called
    fun setSelectedLocation(location: LatLng){
        _selectedLocation.value = location
    }

    //observable data stream for tracking route points
    // Consumers can observe changes but can't modify directly
    private val _routePoints = MutableStateFlow<List<LatLng>?>(null)
    val routepoints: StateFlow<List<LatLng>?> = _routePoints.asStateFlow()

    // designed to calculate and retrieve a route form the user's current location to a specified marker on a map
    fun fetchRouteFromCurrentPositionToMarker(maker: LatLng){

        viewModelScope.launch {
            locationRepository.userLocation.value?.let {
                currentLocation ->
                val route = directionsRepository.getRoute(
                    currentLocation,
                    maker
                )

                if(route != null) {
                    _routePoints.value = route

                } else {
                   Log.v("TAGY", "Route is null")
                }

            } ?: run {
                Log.v("TAGY", "user ocation is null")
            }
        }
    }

}
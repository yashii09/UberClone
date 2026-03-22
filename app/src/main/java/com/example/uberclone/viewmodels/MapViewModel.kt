package com.example.uberclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uberclone.repositories.MarkerRepository
import com.example.uberclone.utilities.DestinationMarker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.SuccessContinuation
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// this ViewModel needs to be created by Hilt
// A Hilt view model is an annotation you put on your ViewModel class to tell
// held dependency injection library that this ViewModel needs to be created by hilt
// automatically provides its dependencies like repositories via constructor injection
@HiltViewModel
class MapViewModel @Inject constructor(
    private val markerRepository: MarkerRepository
) : ViewModel(){
    fun addMarker(marker: LatLng,
                  name: String,
                  onSuccess: () -> Unit,
                  onFailure: (Exception) -> Unit
    ){
        viewModelScope.launch { //this launches a coroutine bound to the Viewmodel's lifecycle
            // If the ViewModel is cleared , coroutine will be automatically cancelled
            // it keep or it keeps work off the main thread, so this will be done in background thread
            try { //try to call repository.addmarker and insert marker with the name
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
    private val _markers = MutableStateFlow<List<DestinationMarker>>(emptyList())
    val markersInFirebase: StateFlow<List<DestinationMarker>> = _markers.asStateFlow()
    // State flow itself is read only, means it can be observed but not changed,
    // this means the UI can collect markers to get updates, but it can't call dot value to modify it

    init{
        loadMarkersFromFirestore()
    }

    fun loadMarkersFromFirestore(){
        //Since getAllMarkersFromFirebase() is a suspend fun, we need to call it inside the coroutine scope
        viewModelScope.launch {
            try {
                _markers.value = markerRepository.getAllMarkersFromFirebase()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}
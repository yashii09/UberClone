package com.example.uberclone.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uberclone.location.RequestLocationPermission
import com.example.uberclone.utilities.ExpandableFAB
import com.example.uberclone.viewmodels.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.widgets.DisappearingScaleBar

@Composable
fun UberCloneMap(){

    val cameraPositionState = rememberCameraPositionState() // part of Google Maps compose library
    //it is used to manage the camera positions and view of Google map in JC

    //Testing addMarker()
    //hiltViewViewModel() : retrieved the viewModel instance , automatically uses hilt to create and inject dependencies into it
    val viewModel : MapViewModel = hiltViewModel()
    //compose knows which ViewModel you want, which is the map viewModel and hilt will find the hilt viewmodel annotation on map viewModel
    // and sees what dependencies it needs in the constructor

    viewModel.addMarker(
        LatLng(37.7749, -122.4194),
        "San Francisco",
        onSuccess = { Log.v("TAGY", "Marker added successfully") },
        onFailure = { exception ->  }
    )
    viewModel.addMarker(
        LatLng(41.8719, 12.5674),
        name = "Italy",
        onSuccess = { Log.v("TAGY", "Marker added successfully") },
        onFailure = { exception ->  }

    )

    //getting markers from firebase
    // collecting a stateflow from your viewmodel and exposes it as compose that the UI can react to
    //val markers by viewModel.markersInFirebase.collectAsState()

    //This prevents recreating the state reference on recomposition
    val markers = remember { viewModel.markers }


    //current location
    val userLocation by viewModel.userLocation.collectAsState()
    //request location permissions
    RequestLocationPermission(viewModel){
        viewModel.startLocationUpdates()
    }

    // Directions API

    // observe state changes in routePoints
    // viewModel.routePoints - provide updates on the route points.
    // StateFlow is a way to hold a state that can emit updates to the UI when the data changes
    // collectAsStateWithLifecycle - it collects the StateFlow and converts it into compose friendly state variable. ->
    // This allows the UI to reactively update whenever the routePoints change
    // by - used for delegation of properties from one object to another
    val routePoints by viewModel.routepoints.collectAsStateWithLifecycle()

    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
    // this variable holds a nullable LatLng object(representing a geographical coordinate)
    //remember is used to store this state across recomposition
    // null indicates no marker position is selected initially
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }




    Box(modifier = Modifier.fillMaxSize()){

        //1 - Google Map
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,

            onMapClick = { myMarker ->
                markerPosition = myMarker
                viewModel.setSelectedLocation(myMarker)
            }
        ) {

            //display the current location marker
            userLocation?.let { myCurrentLocation ->
                Marker( // used to create a visual marker on the map
                    // MarkerState class requires the LatLng object to determine where the marker should be places on the map
                    state = MarkerState(myCurrentLocation),
                    title = "Current Location",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            }



            markers.forEach { marker ->
                Marker (
                    state = MarkerState(LatLng(marker.latitude, marker.longitude)),
                    title = marker.title
                )
            }

            // manages the display and behavior of a marker that appears when a user selects a location on the map
            markerPosition?.let {
                Marker(
                    state = MarkerState(it),
                    title = "Selected Location",
                    snippet = "${it.latitude}, ${it.longitude}",
                    onClick = {
                        viewModel.setSelectedLocation(markerPosition!!)
                        false
                    }
                )
            }
        }

        // 2- Expandable FAB
        ExpandableFAB(
            modifier = Modifier.align(Alignment.BottomStart)
                .padding(16.dp)
        )

        // 3 - ScaleBar - is a widget that shows the current scale of the map in feet
        // and meters when zoomed in into the map , changing to miles and kilometers
        // respectively when zooming out and disappearing
        DisappearingScaleBar(
            modifier = Modifier.padding(5.dp)
                .align(Alignment.TopStart),
            cameraPositionState = cameraPositionState

        )

    }
}
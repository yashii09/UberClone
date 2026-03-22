package com.example.uberclone.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.uberclone.utilities.ExpandableFAB
import com.example.uberclone.viewmodels.MapViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.widgets.DisappearingScaleBar

@Composable
fun UberCloneMap(){

    val cameraPositionState = rememberCameraPositionState() // part of google mao compose library
    //it is used to manage the camera positions and view of google map in JC

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

    //getting markers from firebase
    // collecting a stateflow from your viewmodel and exposes it as a compose that the UI can react to
    val markers by viewModel.markersInFirebase.collectAsState()





    Box(modifier = Modifier.fillMaxSize()){

        //1 - Google Map
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState
        ) {
            markers.forEach { marker ->
                Marker (
                    state = MarkerState(LatLng(marker.latitude, marker.longitude)),
                    title = marker.title
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
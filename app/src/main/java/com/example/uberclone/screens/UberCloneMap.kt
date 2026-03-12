package com.example.uberclone.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun UberCloneMap(){

    val cameraPositionState = rememberCameraPositionState() // part of google mao compose library
    //it is used to manage the camera positions and view of google map in JC

    Box(modifier = Modifier.fillMaxSize()){

        //1 - Google Map
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState
        )
    }
}
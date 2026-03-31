package com.example.uberclone.location

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.uberclone.viewmodels.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

// This file is used to request location permission from the user

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    viewModel: MapViewModel,
    onPermissionGranted: () -> Unit = {}
){
    // this creates a state holder that manages multiple runtime permissions specifically for location access for both fine and coarse
    // tracks permission status reactively and handles permission requests
    // A compose utility(rememberMultiplePermissionsState) remembers permission state across recomposition
    // providing current permission status, methods to launch permission requests and automatic recomposition when permission change
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            //access a fine location GPS level accuracy 5 to 50m required for precise location tracking
            Manifest.permission.ACCESS_FINE_LOCATION,
            // a coarse location network based accuracy from 100 to 500m
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        if(locationPermissions.allPermissionsGranted){
            viewModel.hasLocationPermission.value = true
            onPermissionGranted()
        } else {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }
}
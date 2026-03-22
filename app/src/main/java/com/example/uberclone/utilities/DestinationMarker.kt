package com.example.uberclone.utilities

import com.google.android.gms.maps.model.LatLng


data class DestinationMarker(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String
){
    //Convert DestinationMarker to LatLng
    fun toLatLng() = LatLng(latitude, longitude)
}

package com.example.uberclone.repositories

import com.example.uberclone.retrofit.DirectionsResponse
import com.google.android.gms.maps.model.LatLng

interface DirectionsRepository {

    // fetch the route between current position and a marker
    suspend fun getRoute(
        origin: LatLng,
        destination: LatLng
    ): List<LatLng>?

    // Fetch the route between multiple markers
    suspend fun getRouteForMultipleMarkers(
        origin: LatLng,
        // The waypoint falls between the origin and destination in a route
        // It can help define the path taken by the user. Represented as a list of LatLng
        // This can represent places where a user wants to stop or points that should be included in the route for various reasons.
        // This is a limit on the number of waypoints which is 24
        waypoints: List<LatLng>,
        destination: LatLng
    ): DirectionsResponse?
}
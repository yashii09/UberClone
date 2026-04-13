package com.example.uberclone.retrofit


//These three data classes model structure the response form the Google Maps Directions API
// This represents the entire response from the Google Maps Direction API
// It contains a list of Route objects. This allows you to receive multiple route options if you request alternatives in your API call
data class DirectionsResponse(
    val routes: List<Route>
)

// This class represents a single route between an origin and a destination
data class Route(
    // This property of this class contains PolyLine object, which is essential for displaying the route on a map,
    // The PolyLine encapsulates the encoded path of the route which can be used for visualization
    val overview_polyline: PolyLine
)

// This class contains a property names as points, which is a string.
// This string is an encoded version of the route's path generated using Google's polyline algorithm.
// The encoded polyline is a compressed representation that reduces the data size, making it efficient to trnasmit and store.
// It needs to be decoded for displaying on a map, ensuring the route is visualized correctly.
data class PolyLine(
    val points: String
)
package com.example.uberclone.repositories

import com.example.uberclone.retrofit.DirectionsResponse
import com.example.uberclone.retrofit.DirectionsService
import com.google.maps.android.PolyUtil
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Named

class DirectionsRepositoryImpl @Inject constructor(
    @Named("maps_api_key") private val apiKey: String,
    private val directionsService: DirectionsService
) : DirectionsRepository {

    override suspend fun getRoute(
        origin: LatLng,
        destination: LatLng
    ): List<LatLng>? {
        return try {
            // 1 - converting origin & destination into strings for api call
            val originStr = "${origin.latitude},${origin.longitude}"
            val destinationStr = "${destination.latitude},${destination.longitude}"

            // 2 - making the api call
            val response = directionsService.getDirections(
                origin = originStr,
                destination = destinationStr,
                apiKey = apiKey
            )

            // 3 - response processing
            response.routes.firstOrNull()           // access the first route in the response if available
                ?.overview_polyline?.points         // this will get the overview polyline containing encoded points that define the route
                                                    // These encoded points are of type string
                ?.let {
                    // 4- Polyline decoding
                    PolyUtil.decode(it).map { latLng ->
                        LatLng(latLng.latitude,
                            latLng.longitude)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getRouteForMultipleMarkers(
        origin: LatLng,
        waypoints: List<LatLng>,
        destination: LatLng
    ): DirectionsResponse? {
        // Implementation for multiple markers
        return null 
    }
}

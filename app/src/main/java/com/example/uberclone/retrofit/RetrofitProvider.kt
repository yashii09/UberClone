package com.example.uberclone.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// This object is designed for setting up and providing a Retrofit service for making network requests in an app
// This object is singleton. This ensures that only one instance of this provider is created, which is efficient for managing network requests across the app
object RetrofitProvider {

    // this takes the base url as a parameter
    // This parameter allow flexibility, letting you specify the base URL for any API you are connecting to.
    // This is useful for wolring with different environments(e.g., development, staging, production)
    fun createDirectionsService(
        baseUrl: String
    ): DirectionsService {

        // this retrofit builder starts building the retrofit instance
        return Retrofit.Builder()
            .baseUrl(baseUrl) // this sets the base url for the API endpoint
            .addConverterFactory(GsonConverterFactory.create()) // Gson is used to convert JSON response to Kotlin objects and vice versa
            .build() // build finalizes and create retrofit instance
            .create(DirectionsService::class.java) // generates an implementation of directions service interface, which contains the defined API endpoints for accessing Google Maps Directions API
    }
}

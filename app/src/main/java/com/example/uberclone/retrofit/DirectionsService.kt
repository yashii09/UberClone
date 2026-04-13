package com.example.uberclone.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

//In order to implement retrofit, we have three sections
//1. Interface
interface DirectionsService {

//      Base URL: https://maps.googleapis.com/maps/api/
//      End Point: directions/json
//      ?destination=Montreal
//      &origin=Toronto
//      &key=AIzaSyBhBPJRRvVt7M4jEVpF7ZUM-KVrqhNxZPQ

    // This interface defines a contract for making requests to the Google Maps direction api using retrofit
    // All methods in this interface will be converted into http calls
    // The @GET annotation maps to the HTTP GET method, which is used to retrieve data from a server
    // When this endpoint is called, the application intends to retrieve data in JSON format
    @GET("directions/json")
    suspend fun getDirections( // suspends marks this as a coroutine
        // Query refers to a string that you use to search for data, which can be a keyword or a phrase
        @Query("destination") destination: String,
        @Query("origin") origin:String,
        @Query("key") apiKey:String
    ) : DirectionsResponse




}

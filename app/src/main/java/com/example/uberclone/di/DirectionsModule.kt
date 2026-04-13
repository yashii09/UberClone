package com.example.uberclone.di

import com.example.uberclone.repositories.DirectionsRepository
import com.example.uberclone.repositories.DirectionsRepositoryImpl
import com.example.uberclone.retrofit.DirectionsService
import com.example.uberclone.retrofit.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class )
object DirectionsModule {


    @Provides
    @Singleton
    // design to create and return a instance of the DirectionsService
    // provides a way to access the Google Maps Directions API through a Retrofit service
    fun provideDirectionsService(): DirectionsService {
        return  RetrofitProvider.createDirectionsService( // creates the service insatnce.
            baseUrl = "https://maps.googleapis.com/maps/api" // specifies the endpoint of the Google Maps API
        )

    }

    @Provides
    @Singleton
    fun provideDirectionsRepository(
        apiKey: String,
        directionsService: DirectionsService
    ) : DirectionsRepository {
        return DirectionsRepositoryImpl(
            apiKey = apiKey,
            directionsService = directionsService
        )
    }

    @Provides
    @Named("maps_api_key")
    fun provideApiKey(): String = "AIzaSyBhBPJRRvVt7M4jEVpF7ZUM-KVrqhNxZPQ"
}
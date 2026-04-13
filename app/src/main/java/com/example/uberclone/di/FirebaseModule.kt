package com.example.uberclone.di

import com.example.uberclone.repositories.MarkerRepository
import com.example.uberclone.repositories.MarkerRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//modules tell how to create & provide dependencies to the rest of your app

@Module // this contains methods that provide dependencies
@InstallIn(SingletonComponent::class) // It makes provided dependencies live as long as the application
//SingletonComponent means these dependencies exist for the lifetime of your application
// They are singletons one shared across the app
object FirebaseModule {

    @Provides //// this marks a function as a dependency provider
    @Singleton
    //Firebase Firestore fun provide Firebase Firestore and return Firebase Firestore
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance() // this is the standard firebase sdk way to get the singleton
        // anywhere you ask for Firebase Firestore, it will return the same instance
    }

    @Provides // this function tells hilt how to create Firebase Firestore
    // singleton annotation help hilt to create only one instance of Firebase Firestore
    @Singleton
    fun provideMarkerRepository( // anywhere you inject marker repository, hilt will provide this function to build it
        firestore: FirebaseFirestore
    ): MarkerRepository = MarkerRepositoryImpl(firestore)
}




package com.example.uberclone.di

import android.content.Context
import com.example.uberclone.repositories.LocationRepository
import com.example.uberclone.repositories.LocationRepositoryIml
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // this contains methods that provide dependencies
@InstallIn(SingletonComponent::class) // It makes provided dependencies live as long as the application
object LocationModule  {

    @Provides // this marks a function as a dependency provider
    @Singleton //SingletonComponent means these dependencies exist for the lifetime of your application
    fun provideLocationRepository(
        @ApplicationContext context: Context
    ) : LocationRepository = LocationRepositoryIml(context)
}
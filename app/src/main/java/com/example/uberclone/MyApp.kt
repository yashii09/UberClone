package com.example.uberclone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// simply telling hey hilt, please generate all the code here
// that is required for DI to work in this app
// When you annotate your application class with Android Hilt annotation,
// hilt generates a base class that starts with DI container
// hilt can now inject dependencies anywhere in your app Activities fragment view models, services and etc.
// Without @HiltAndroidApp, Hilt won't know where to start look for dependencies
@HiltAndroidApp
class MyApp : Application() {
}
package com.sto.kn.app

import android.app.Application
import com.sto.kn.lib.*

class DemoApplication : Application() {
    val locationService by lazy { LocationServiceImpl(this) }
    private val firebaseService by lazy { AndroidFirebaseService() }

    lateinit var storeCoordinator: StoreCoordinator

    override fun onCreate() {
        super.onCreate()

        storeCoordinator = StoreCoordinator(firebaseService, locationService, DemoState())
    }
}
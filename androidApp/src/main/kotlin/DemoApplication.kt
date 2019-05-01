package com.akqa.kn.app

import android.app.Application
import com.akqa.kn.lib.*

class DemoApplication : Application() {
    val locationService by lazy { LocationServiceImpl(this) }
    val wikiRepository = WikiRepositoryImpl()

    override fun onCreate() {
        super.onCreate()

        FirebaseService.configure()
    }
}
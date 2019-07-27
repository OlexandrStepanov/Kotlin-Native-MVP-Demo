package com.sto.kn.app

import android.app.Application
import com.sto.kn.lib.*

class DemoApplication : Application() {
    val locationService by lazy { LocationServiceImpl(this) }
    val wikiRepository = WikiRepositoryImpl()
    val firebaseService = AndroidFirebaseService()
}
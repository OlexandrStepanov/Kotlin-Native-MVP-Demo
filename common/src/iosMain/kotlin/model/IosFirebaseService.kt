package com.akqa.kn.lib

import firebase.FIRApp

class FirebaseServiceImpl : AbstractFirebaseService() {

    override fun initialize() {
        println("Firebase initialize()")

        FIRApp.configure()
    }

}

package com.akqa.kn.lib

import firebase.FIRApp

actual class FirebaseService {
    actual companion object {
        actual fun configure() {
            println("Firebase initialize()")

            FIRApp.configure()
        }
    }
}

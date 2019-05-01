package com.akqa.kn.lib

import android.util.Log

actual class FirebaseService {

    actual companion object {
        private val TAG = FirebaseService::class.simpleName

        actual fun configure() {
            //  On android doesn't require
            Log.d(TAG, "initialize()")
        }
    }

}
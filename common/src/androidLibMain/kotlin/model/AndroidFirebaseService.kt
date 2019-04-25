package com.akqa.kn.lib

import android.util.Log

class FirebaseServiceImpl : AbstractFirebaseService() {

    private val TAG = FirebaseServiceImpl::class.simpleName

    override fun initialize() {
        //  On android doesn't require
        Log.d(TAG, "initialize()")
    }

}
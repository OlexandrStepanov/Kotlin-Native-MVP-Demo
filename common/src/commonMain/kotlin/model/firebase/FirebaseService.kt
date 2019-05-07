package com.akqa.kn.lib

interface FirebaseDocument {
    val json: String
}

typealias FirebaseLoadHandler= (List<FirebaseDocument>) -> Unit

interface FirebaseService {
    fun loadAllDocuments(collectionRef: String, handler: FirebaseLoadHandler)
}


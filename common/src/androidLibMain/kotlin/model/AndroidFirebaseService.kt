package com.akqa.kn.lib
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

class AndroidFirebaseDocument(json: String): FirebaseDocument {
    override val json: String = json
}

class AndroidFirebaseService: FirebaseService {
    private val TAG = this.javaClass.simpleName

    @ImplicitReflectionSerializer
    override fun loadAllDocuments(collectionRef: String, handler: FirebaseLoadHandler) {
        val collection = FirebaseFirestore.getInstance().collection(collectionRef)
        collection.get().addOnSuccessListener { result ->
            result.documents.mapNotNull { doc ->
                if (doc.data != null) {
                    val json = Json.stringify(doc.data as Map<String, Any>)
                    AndroidFirebaseDocument(json)
                }
                else {
                    Logger.e(TAG, "Error getting Firestore document: $doc")
                    null
                }
            }

        }.addOnFailureListener { exception ->
            Logger.e(TAG, "Error getting Firestore documents: $exception")
        }
    }

}
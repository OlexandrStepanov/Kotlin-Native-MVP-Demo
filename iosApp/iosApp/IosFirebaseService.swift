//
//  IosFirebaseService.swift
//  iosApp
//
//  Created by Oleksandr Stepanov on 06/05/2019.
//

import Foundation
import common
import FirebaseFirestore

class IosFirebaseDocument: FirebaseDocument {
    var json: String
    
    init(json: String) {
        self.json = json
    }
}

class IosFirebaseService: FirebaseService {
    let db = Firestore.firestore()
    
    func loadAllDocuments(collectionRef: String, handler: @escaping ([FirebaseDocument]) -> Void) {
        db.collection("posts").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                let documents: [IosFirebaseDocument] = querySnapshot!.documents.compactMap { doc in
                    if let jsonData = try? JSONSerialization.data(withJSONObject: doc.data(), options: [.prettyPrinted]),
                        let jsonString = String.init(data: jsonData, encoding: .utf8) {
                        return IosFirebaseDocument(json: jsonString)
                    }
                    return nil
                }
                
                handler(documents)
            }
        }
    }
}

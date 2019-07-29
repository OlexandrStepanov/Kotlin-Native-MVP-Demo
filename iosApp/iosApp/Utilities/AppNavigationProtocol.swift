//
//  BaseViewModel.swift
//  TagHeuerConnected
//
//  Created by Dmitriy Serdiuk on 08/02/2017.
//  Copyright © 2017 Tag Heuer. All rights reserved.
//

import UIKit
import common

protocol AppNavigationProtocol {
    
    var storeCoordinator: StoreCoordinator { get }
    
    var appDelegate: AppDelegate { get }
}

extension AppNavigationProtocol {
    
    var storeCoordinator: StoreCoordinator {
        return appDelegate.storeCoordinator
    }
    
    var appDelegate: AppDelegate {
        return UIApplication.shared.delegate as! AppDelegate
    }
}

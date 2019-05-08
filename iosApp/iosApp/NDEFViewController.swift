//
//  NDEFViewController.swift
//  iosApp
//
//  Created by Osypov Alim on 4/23/19.
//

import UIKit
import common

class NDEFViewController: UIViewController, NFCView {
    
    func addTag(tag: NFCTag) {
        let text = tag.data
        DispatchQueue.main.async {
            self.dataLabel.text = text
        }
    }
    
    @IBOutlet private var dataLabel: UILabel!
    
    @IBAction func startScan() {
        presenter?.start()
    }
    
    private var presenter: NFCPresenter?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        dataLabel.text = ""
        
        presenter = NFCPresenter(nfcService: NFCServiceImpl(), view: self)
    }
    


}

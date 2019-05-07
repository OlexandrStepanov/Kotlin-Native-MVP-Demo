//
//  PostsViewController.swift
//  iosApp
//
//  Created by Oleksandr Stepanov on 06/05/2019.
//

import UIKit
import common

class PostsViewController: UIViewController, PostsView, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet var tableView: UITableView!

    lazy var firebaseService = IosFirebaseService()
    lazy var presenter = PostsPresenter(firebase: firebaseService, view: self)
    
    var results: [Post] = [] {
        didSet {
            tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
            
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.rowHeight = UITableViewAutomaticDimension;
        tableView.estimatedRowHeight = 50.0;
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        presenter.present()
    }
    
    //  MARK: - Table view stuff
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PostCell", for: indexPath)
        
        let post = results[indexPath.row]
        cell.textLabel?.text = post.title
        cell.detailTextLabel?.text = post.text
        
        return cell
    }
}

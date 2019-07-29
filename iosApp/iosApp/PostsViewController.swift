//
//  PostsViewController.swift
//  iosApp
//
//  Created by Oleksandr Stepanov on 06/05/2019.
//

import UIKit
import common

class PostsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, AppNavigationProtocol {
    
    @IBOutlet var tableView: UITableView!
    @IBOutlet var loadingIndicator: UIActivityIndicatorView!

    lazy var viewModel = PostsViewModel.init(view: self)
    
    var posts: [Post] = [] {
        didSet {
            tableView.reloadData()
        }
    }
    
    deinit {
        viewModel.onDestroy()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel.onCreate(store: storeCoordinator.store)
            
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.rowHeight = UITableViewAutomaticDimension;
        tableView.estimatedRowHeight = 50.0;
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        viewModel.reloadPosts()
    }
    
    //  MARK: - Table view stuff
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return posts.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PostCell", for: indexPath)
        
        let post = posts[indexPath.row]
        cell.textLabel?.text = post.title
        cell.detailTextLabel?.text = post.text
        
        return cell
    }
}

extension PostsViewController: PostsViewModelView {
    
    func set(showLoadingIndicator: Bool) {
        if showLoadingIndicator {
            self.loadingIndicator.startAnimating()
        }
        else {
            self.loadingIndicator.stopAnimating()
        }
    }
    
    func set(posts: [Post]) {
        self.posts = posts
    }
}

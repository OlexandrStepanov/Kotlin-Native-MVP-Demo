import Foundation
import UIKit
import common

class SearchViewController : UIViewController, SearchView, UITableViewDelegate, UITableViewDataSource {
    
    lazy var theWikiRepository = WikiRepositoryImpl()
    lazy var presenter = SearchPresenter(
            repository: theWikiRepository,
            locationService: LocationServiceImpl(),
            view: self
    )
    
    override func viewDidLoad() {
        super.viewDidLoad()
        searchResultsTable.delegate = self
        searchResultsTable.dataSource = self
        print("theWikiRepository: \(theWikiRepository)")
        presenter.start()
    }
    
    var results: [SearchViewResultItem] = [] {
        didSet {
            self.searchResultsTable.reloadData()
        }
    }
    
    var query: String {
        get {
            return searchQueryTextField.text ?? ""
        }
    }

    @IBAction func searchQueryUpdated(_ sender: UITextField) {
        presenter.present()
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "searchResultCell", for: indexPath) as! SearchResultCell

        cell.title.text = results[indexPath.row].title

        return cell
    }
    
    @IBOutlet var searchResultsTable: UITableView!
    
    @IBOutlet var searchQueryTextField: UITextField!
}

class SearchResultCell : UITableViewCell {
    
    @IBOutlet var title: UILabel!
}

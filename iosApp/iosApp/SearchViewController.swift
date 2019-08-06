import Foundation
import UIKit
import common

class SearchResultCell : UITableViewCell {
}


class SearchViewController : UIViewController, UITableViewDelegate, UITableViewDataSource, AppNavigationProtocol {
    
    @IBOutlet var searchResultsTable: UITableView!
    @IBOutlet var searchQueryTextField: UITextField!
    @IBOutlet var stateLabel: UILabel!
    
    lazy var viewModel = WikiSearchViewModel.init(view: self)
    
    deinit {
        viewModel.stopUpdating()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        searchResultsTable.delegate = self
        searchResultsTable.dataSource = self
        
        viewModel.onCreate(store: storeCoordinator.store)
        viewModel.startUpdating()
    }
    
    var items: [WikiSearchViewModelViewItem] = [] {
        didSet {
            self.searchResultsTable.reloadData()
        }
    }
    
    @IBAction func searchQueryUpdated(_ sender: UITextField) {
        viewModel.update(query: sender.text ?? "")
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "searchResultCell", for: indexPath) as! SearchResultCell

        cell.textLabel?.text = items[indexPath.row].title

        return cell
    }
}

extension SearchViewController: WikiSearchViewModelView {
    func set(state: WikiSearchState.State) {
        switch state {
            
        case WikiSearchState.State.nolocation:
            stateLabel.text = NSLocalizedString("No location received", comment: "")
            
        case WikiSearchState.State.loading:
            stateLabel.text = NSLocalizedString("Loading Wiki pages ...", comment: "")
            
        case WikiSearchState.State.loadedandfiltered:
            stateLabel.text = ""
            
        default:
            break
        }
    }
    
    func set(items: [WikiSearchViewModelViewItem]) {
        self.items = items
    }
}

import Foundation
import UIKit
import common

class SearchResultCell : UITableViewCell {    
    @IBOutlet var title: UILabel!
}


class SearchViewController : UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet var searchResultsTable: UITableView!
    @IBOutlet var searchQueryTextField: UITextField!
    
    lazy var locationService = LocationServiceImpl()
    lazy var api = WikiRepositoryImpl()
    lazy var viewModel = SearchViewModelImpl(
            repository: api,
            locationService: locationService
    )
    
//    let disposable = ReaktiveCompositeDisposable()
    
    deinit {
//        disposable.dispose()
        viewModel.stop()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        searchResultsTable.delegate = self
        searchResultsTable.dataSource = self
        
        setupObservers()
        viewModel.start()
    }
    
    func setupObservers() {
//        disposable.add(disposable:
//            ReaktiveExtensionsKt.subscribe(viewModel.results) { [weak self] (list: [SearchViewModelResultItem]) -> KotlinUnit in
//            self?.results = list
//            return KotlinUnit()
//        }
    }
    
    var results: [SearchViewModelResultItem] = [] {
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
        viewModel.query.onNext(value: sender.text)
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
}

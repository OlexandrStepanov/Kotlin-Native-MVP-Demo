package com.akqa.kn.lib

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



interface SearchView {
    class ResultItem(val title: String, val distance: Double, val url: String)

    var results: List<ResultItem>
    val query: String
}



class SearchPresenter(
    private val repository: WikiRepository,
    private val locationService: LocationService,
    val view: SearchView
) {

    private fun onLocationUpdate() {
        present()
    }

    private val onLocationUpdateCallback = ::onLocationUpdate

    fun start() {
        locationService.onLocationUpdateListeners += onLocationUpdateCallback
        locationService.start()
        present()
    }

    fun stop() {
        locationService.stop()
        locationService.onLocationUpdateListeners -= onLocationUpdateCallback
    }

    fun present() {
        locationService.location?.let { location ->
            GlobalScope.launch(coroutineDispatcher) {

                view.results = repository.getNearestPages(location).filter {
                    if (view.query.isNotEmpty()) it.title.contains(view.query) else true
                }.sortedBy {
                    it.dist
                }.take(10).map {
                    SearchView.ResultItem(
                        it.title,
                        it.dist,
                        "https://en.wikipedia.org/wiki/${it.title}"
                    )
                }
            }
        }
    }

}

expect val coroutineDispatcher: CoroutineDispatcher
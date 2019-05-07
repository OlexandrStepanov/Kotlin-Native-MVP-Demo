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

                view.results = repository.getNearestPages(location).map {
                    SearchView.ResultItem(
                        it.title,
                        it.dist,
                        "https://en.wikipedia.org/wiki/${it.title}"
                    )
                }.filter {
                    it.matches(view.query)
                }
            }
        }
    }

}

private fun SearchView.ResultItem.matches(query: String): Boolean =
    this.title.contains(query)

expect val coroutineDispatcher: CoroutineDispatcher
package com.akqa.kn.lib

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.behavior.behaviorSubject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.ThreadLocal


interface SearchViewModel{
    data class ResultItem(val title: String, val distance: Double, val url: String)

    val results: BehaviorSubject<List<ResultItem> >
    val query: BehaviorSubject<String>
}



class SearchViewModelImpl (
    private val repository: WikiRepository,
    private val locationService: LocationService
) {

    val results: BehaviorSubject<List<SearchViewModel.ResultItem> > = behaviorSubject(emptyList())
    val query: BehaviorSubject<String> = behaviorSubject("")

    val disposable = CompositeDisposable()

    private fun onLocationUpdate() {
        reload(query.value)
    }

    private val onLocationUpdateCallback = ::onLocationUpdate

    fun start() {
        locationService.onLocationUpdateListeners += onLocationUpdateCallback
        locationService.start()

        disposable +=
        query.subscribe {
            Logger.d("Search", "query = $it")
//            reload(it)
        }
    }

    fun stop() {
        locationService.stop()
        locationService.onLocationUpdateListeners -= onLocationUpdateCallback

        disposable.dispose()
    }

    internal fun reload(query: String) {
//        val results = this.results

        locationService.location?.let { location ->
            GlobalScope.launch(coroutineDispatcher) {

//                results.onNext(
                val results = repository.getNearestPages(location).filter {
                    if (query.isNotEmpty()) it.title.contains(query) else true
                }.sortedBy {
                    it.dist
                }.take(10).map {
                    SearchViewModel.ResultItem(
                        it.title,
                        it.dist,
                        "https://en.wikipedia.org/wiki/${it.title}"
                    )
                }

                Logger.d("Search", "Loaded results: $results")
            }
        }
    }

}

expect val coroutineDispatcher: CoroutineDispatcher
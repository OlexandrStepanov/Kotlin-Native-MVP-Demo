package com.sto.kn.lib

import com.sto.kn.lib.redux.PartialSubscriber
import com.sto.kn.lib.redux.Subscriber


class WikiSearchViewModel(private val view: View) : BaseViewModel(), PartialSubscriber.SubStateChangeListener<DemoState, WikiSearchState> {

    interface View {
       data class Item(val title: String, val distance: Double, val url: String)

        fun set(state: WikiSearchState.State)
        fun set(items: List<Item>)
    }

    // BaseViewModel

    override fun onCreateSubscriber(): Subscriber<DemoState> = PartialSubscriber(this)

    // SubStateChangeListener

    override fun getSubState(state: DemoState): WikiSearchState = state.wikiSearchState

    override fun onSubStateChanged(wikiState: WikiSearchState) {
        view.set(wikiState.state)
        view.set(wikiState.filteredPages.map {
            View.Item(it.title, it.dist, "https://en.wikipedia.org/wiki/${it.title}")
        })

    }

    // Public Api

    fun startUpdating() {
        mStore.dispatch(WikiSearchActions.StartUpdatingLocation)
    }

    fun stopUpdating() {
        mStore.dispatch(WikiSearchActions.StopUpdatingLocation)
    }

    fun update(query: String) {
        mStore.dispatch(WikiSearchActions.SearchQueryUpdated(query))
    }
}

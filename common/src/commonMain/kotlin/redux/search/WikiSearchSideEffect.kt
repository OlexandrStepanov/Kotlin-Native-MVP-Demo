package com.sto.kn.lib

import com.sto.kn.lib.redux.Action
import com.sto.kn.lib.redux.Dispatcher
import com.sto.kn.lib.redux.SideEffect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class WikiSearchSideEffect(private val repository: WikiRepository): SideEffect<DemoState>() {

    override fun dispatch(state: () -> DemoState, action: Action, dispatcher: Dispatcher) {

        if (action is WikiSearchActions.LocationUpdate) {
            dispatcher.dispatch(WikiSearchActions.LoadingWikiPages)

            GlobalScope.launch(coroutineDispatcher) {
                val pages = repository.getNearestPages(action.newLocation)
                dispatcher.dispatch(WikiSearchActions.WikiPagesLoaded(pages))
            }
        }
    }
}
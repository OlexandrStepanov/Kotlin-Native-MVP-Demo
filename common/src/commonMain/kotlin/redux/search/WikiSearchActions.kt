package com.sto.kn.lib

import com.sto.kn.lib.redux.Action

sealed class WikiSearchActions: Action {
    object StartUpdatingLocation: WikiSearchActions()
    object StopUpdatingLocation: WikiSearchActions()
    data class LocationUpdate(val newLocation: Location): WikiSearchActions()

    object LoadingWikiPages: WikiSearchActions()
    data class WikiPagesLoaded(val pages: List<WikiPage>): WikiSearchActions()
    data class SearchQueryUpdated(val query: String): WikiSearchActions()
}
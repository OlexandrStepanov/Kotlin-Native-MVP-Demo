package com.sto.kn.lib

import kotlinx.serialization.Serializable

@Serializable
data class WikiSearchState (
        val pages: List<WikiPage> = emptyList(),
        val query: String = "",
        val filteredPages: List<WikiPage> = emptyList(),
        val state: State = State.NoLocation) {

    enum class State {
        NoLocation, Loading, LoadedAndFiltered
    }
}


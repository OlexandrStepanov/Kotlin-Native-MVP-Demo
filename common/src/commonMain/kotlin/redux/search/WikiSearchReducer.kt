package com.sto.kn.lib

import com.sto.kn.lib.redux.Action
import com.sto.kn.lib.redux.Reducer


internal class WikiSearchReducer : Reducer<WikiSearchState> {

    private fun filter(pages: List<WikiPage>, query: String): List<WikiPage> {
        return pages.filter {
            if (query.isEmpty()) {
                true
            } else {
                var result = false
                val words = it.title.split(" ")
                for (word in words) {
                    if (word.startsWith(query, ignoreCase = true)) {
                        result = true
                        break
                    }
                }
                result
            }
        }.sortedBy {
            it.dist
        }.take(10)
    }

    override fun reduce(state: WikiSearchState, action: Action): WikiSearchState =
            when (action) {

                is WikiSearchActions.LoadingWikiPages -> {
                    state.copy(state = WikiSearchState.State.Loading)
                }

                is WikiSearchActions.WikiPagesLoaded -> {
                    state.copy(pages = action.pages,
                            filteredPages = filter(action.pages, state.query),
                            state = WikiSearchState.State.LoadedAndFiltered)
                }

                is WikiSearchActions.SearchQueryUpdated -> {
                    state.copy(filteredPages = filter(state.pages, action.query), query = action.query)
                }

                else -> state
            }
}


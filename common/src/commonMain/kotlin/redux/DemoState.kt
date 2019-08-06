package com.sto.kn.lib

import kotlinx.serialization.Serializable


@Serializable
data class DemoState (
    val postsState: PostsState = PostsState.NotLoaded,
    val wikiSearchState: WikiSearchState = WikiSearchState()
)

val DemoState.Companion.defaultState: DemoState
        get() = DemoState()
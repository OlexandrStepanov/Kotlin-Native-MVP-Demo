package com.sto.kn.lib

import kotlinx.serialization.Serializable


@Serializable
data class PostsState (
    val isLoading: Boolean = false,
    val results: List<Post> = emptyList()
)


@Serializable
data class DemoState (
    val postsState: PostsState = PostsState()
)
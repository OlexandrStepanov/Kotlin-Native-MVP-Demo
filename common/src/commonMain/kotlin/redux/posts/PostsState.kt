package com.sto.kn.lib

import kotlinx.serialization.Serializable

@Serializable
sealed class PostsState {
    object NotLoaded : PostsState()
    object Loading : PostsState()
    data class Loaded(val posts: List<Post>) : PostsState()
}
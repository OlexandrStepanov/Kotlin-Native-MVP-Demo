package com.sto.kn.lib

import com.sto.kn.lib.redux.Action

sealed class PostsActions: Action {
    class LoadPosts: PostsActions()
    data class PostsLoaded(val posts: List<Post>): PostsActions()
}
package com.sto.kn.lib

import com.sto.kn.lib.redux.Action
import com.sto.kn.lib.redux.Reducer


internal class PostsReducer : Reducer<PostsState> {

    override fun reduce(state: PostsState, action: Action): PostsState =
        when (action) {
            is PostsActions.LoadPosts-> {
                PostsState.Loading
            }
            is PostsActions.PostsLoaded-> {
                PostsState.Loaded(posts = action.posts)
            }
            else -> state
        }
}


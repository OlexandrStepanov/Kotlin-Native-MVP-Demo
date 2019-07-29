package com.sto.kn.lib

import com.sto.kn.lib.redux.Action
import com.sto.kn.lib.redux.Reducer


internal class PostsReducer : Reducer<PostsState> {

    override fun reduce(state: PostsState, action: Action): PostsState =
        when (action) {
            is PostsActions.LoadPosts-> {
                PostsState(isLoading = true, results =  state.results)
            }
            is PostsActions.PostsLoaded-> {
                PostsState(isLoading = false, results =  action.posts)
            }
            else -> state
        }
}


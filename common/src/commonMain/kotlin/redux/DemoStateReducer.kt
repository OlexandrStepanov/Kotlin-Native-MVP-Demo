package com.sto.kn.lib


import com.sto.kn.lib.redux.*

class DemoStateReducer : Reducer<DemoState> {

    // Members

    private val mPostsReducer: PostsReducer = PostsReducer()

    // Reducer

    override fun reduce(state: DemoState, action: Action): DemoState =
            DemoState(
                mPostsReducer.reduce(state.postsState, action)
            )
}


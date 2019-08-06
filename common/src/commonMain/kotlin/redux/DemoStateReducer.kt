package com.sto.kn.lib


import com.sto.kn.lib.redux.*

class DemoStateReducer : Reducer<DemoState> {

    // Members

    private val mPostsReducer = PostsReducer()
    private val mWikiSearchReducer = WikiSearchReducer()

    // Reducer

    override fun reduce(state: DemoState, action: Action): DemoState =
            DemoState(
                    mPostsReducer.reduce(state.postsState, action),
                    mWikiSearchReducer.reduce(state.wikiSearchState, action)
            )
}


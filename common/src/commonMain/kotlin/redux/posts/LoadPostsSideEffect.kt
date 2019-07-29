package com.sto.kn.lib

import com.sto.kn.lib.redux.Action
import com.sto.kn.lib.redux.Dispatcher
import com.sto.kn.lib.redux.SideEffect

internal class LoadPostsSideEffect(private val firebase: FirebaseService): SideEffect<DemoState>() {

    override fun dispatch(state: () -> DemoState, action: Action, dispatcher: Dispatcher) {

        if (action is PostsActions.LoadPosts) {
            firebase.loadAllDocuments("posts") { documents ->
                val posts = documents.mapNotNull { doc ->
                    Logger.d(TAG, "Trying to parse Post: ${doc.json}")
                    var post: Post? = null
                    try {
                        post = Post.parse(doc.json)
                    } catch (e: Throwable) {
                        Logger.e(TAG, "Can't parse Post from data above. ", e)
                    }
                    post
                }

                dispatcher.dispatch(PostsActions.PostsLoaded(posts))
            }
        }
    }
}


package com.sto.kn.lib

import com.sto.kn.lib.redux.*
import com.sto.kn.lib.redux.PartialSubscriber.SubStateChangeListener

class PostsViewModel(private val view: View) : BaseViewModel(), SubStateChangeListener<DemoState, PostsState> {

    interface View {
        fun set(showLoadingIndicator: Boolean)
        fun set(posts: List<Post>)
    }

    // BaseViewModel

    override fun onCreateSubscriber(): Subscriber<DemoState> = PartialSubscriber(this)

    // SubStateChangeListener

    override fun getSubState(state: DemoState): PostsState = state.postsState

    override fun onSubStateChanged(subState: PostsState) {
        view.set(subState.isLoading)
        view.set(subState.results)
    }

    // Public Api

    fun reloadPosts() {
        mStore.dispatch(PostsActions.LoadPosts())
    }
}

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

    override fun onSubStateChanged(subState: PostsState) =
        when (subState) {
            is PostsState.NotLoaded -> {
                view.set(false)
            }
            is PostsState.Loading -> {
                view.set(true)
            }
            is PostsState.Loaded -> {
                view.set(false)
                view.set(subState.posts)
            }
        }

    // Public Api

    fun reloadPosts() {
        mStore.dispatch(PostsActions.LoadPosts)
    }
}

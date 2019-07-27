package com.sto.kn.lib

data class PostsViewState(val results: List<Post>)

class PostsViewModel (
        private val firebase: FirebaseService
) {

    private val TAG = "PostsPresenter"

    private val state = KMutableLiveData<PostsViewState>()

    fun state() : KLiveData<PostsViewState> {
        return state
    }

    fun reload() {
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

            state.value = PostsViewState(posts)
        }
    }

}
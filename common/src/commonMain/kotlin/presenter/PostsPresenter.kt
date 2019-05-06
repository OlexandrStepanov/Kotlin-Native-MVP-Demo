package com.akqa.kn.lib

interface PostsView {
    var results: List<Post>
}


class PostsPresenter(
        private val firebase: FirebaseService,
        val view: PostsView
) {

    private val TAG = "PostsPresenter"

    fun present() {
        firebase.loadAllDocuments("posts") { documents ->
            view.results = documents.mapNotNull {doc ->
                Logger.d(TAG, "Trying to parse Post: ${doc.json}")
                var post: Post? = null
                try {
                    post = Post.parse(doc.json)
                }
                catch (e: Throwable) {
                    Logger.e(TAG, "Can't parse Post from data above. ", e)
                }
                post
            }
        }
    }

}
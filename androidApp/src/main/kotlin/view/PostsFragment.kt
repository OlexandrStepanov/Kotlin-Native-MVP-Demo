package com.akqa.kn.app

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import androidx.lifecycle.*

import com.akqa.kn.lib.*

class PostsListViewAdapter(context: Context, private val layoutResource: Int, posts: List<Post>) : ArrayAdapter<Post>(context, layoutResource, posts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var view = convertView

        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(layoutResource, null)
        }

        view?.let {view ->
            getItem(position)?.let {post ->
                view.findViewById<TextView>(R.id.titleTextView).text = post.title
                view.findViewById<TextView>(R.id.detailsTextView).text = post.text
            }
        }

        return view
    }
}


class PostsFragment : Fragment() {

    private lateinit var listViewAdapter: PostsListViewAdapter

    var posts: List<Post> by Delegates.observable(emptyList()) { _, _, newValue ->
        listViewAdapter = PostsListViewAdapter(activity!!.baseContext, R.layout.post_cell, newValue)
        this.view!!.findViewById<ListView>(R.id.listView).adapter = listViewAdapter
    }

    private val application by lazy { activity?.application as DemoApplication }
    private val viewModel by lazy { PostsViewModel(application.firebaseService) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.state().toLivedata.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            posts = it.results
        })
    }

    override fun onResume() {
        super.onResume()

        reloadList()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            reloadList()
        }
    }

    private fun reloadList() {
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.reload()
        }
    }

}// Required empty public constructor

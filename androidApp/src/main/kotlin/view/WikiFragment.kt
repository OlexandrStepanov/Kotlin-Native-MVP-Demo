package com.sto.kn.app

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.sto.kn.lib.PermissionManager
import com.sto.kn.lib.WikiSearchState
import com.sto.kn.lib.WikiSearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates


class WikiFragment : Fragment(), WikiSearchViewModel.View {

    private val resultsList: MutableList<String> = mutableListOf()
    private lateinit var listViewAdapter: ArrayAdapter<String>

    var items: List<WikiSearchViewModel.View.Item> by Delegates.observable(emptyList()) { property, oldValue, newValue ->
        resultsList.clear()
        newValue.mapTo(resultsList) { it.title }
        listViewAdapter.notifyDataSetChanged()
    }

    private val application by lazy { activity?.application as DemoApplication }
    private val viewModel by lazy { WikiSearchViewModel(this) }
    private lateinit var editText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wiki, container, false)

        listViewAdapter = ArrayAdapter(activity?.baseContext, android.R.layout.simple_list_item_1, resultsList)
        view.findViewById<ListView>(R.id.listView).adapter = listViewAdapter

        editText = view.findViewById<EditText>(R.id.editText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    viewModel.update(p0.toString())
                }
                else {
                    viewModel.update("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        GlobalScope.launch(Dispatchers.Main) {
            application.locationService.requestPermissions(permissionManager)
            viewModel.onCreate(application.storeCoordinator.store)
            viewModel.startUpdating()
        }

        return view
    }

    private val permissionManager by lazy { PermissionManagerImpl(activity!!) }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        viewModel.stopUpdating()
        super.onDestroy()
    }

    //  WikiSearchViewModel.View

    override fun set(state: WikiSearchState.State) {

    }

    override fun set(items: List<WikiSearchViewModel.View.Item>) {
        this.items = items
    }

}// Required empty public constructor

private class PermissionManagerImpl(private val activity: Activity) : PermissionManager {

    private var nextCode = 0
    private val codeToContinuation = mutableMapOf<Int, Continuation<Boolean>>()

    override suspend fun requestPermission(permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        return suspendCoroutine {
            val code = nextCode++
            codeToContinuation[code] = it

            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    code
            )
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val continuation = codeToContinuation.remove(requestCode) ?: return
        continuation.resume(grantResults.single() == PackageManager.PERMISSION_GRANTED)
    }
}
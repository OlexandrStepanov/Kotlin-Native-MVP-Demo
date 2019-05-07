package com.akqa.kn.app

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.akqa.kn.app.DemoApplication
import com.akqa.kn.app.R
import com.akqa.kn.lib.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates
import android.nfc.*
import android.widget.Toast


class MainActivity : AppCompatActivity(), SearchView {

    private val resultsList: MutableList<String> = mutableListOf()
    private lateinit var listViewAdapter: ArrayAdapter<String>

    override var results: List<SearchView.ResultItem> by Delegates.observable(emptyList()) { property, oldValue, newValue ->
        resultsList.clear()
        newValue.mapTo(resultsList) { it.title }
        listViewAdapter.notifyDataSetChanged()
    }

    override val query get() = editText.text.toString()

    private val application by lazy { super.getApplication() as DemoApplication }
    private val repository get() = application.wikiRepository
    private val presenter by lazy { SearchPresenter(repository, application.locationService, this) }
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_main)

        listViewAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultsList)
        findViewById<ListView>(R.id.listView).adapter = listViewAdapter

        editText = findViewById<EditText>(R.id.editText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                presenter.present()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        GlobalScope.launch(Dispatchers.Main) {
            application.locationService.requestPermissions(permissionManager)
            presenter.start()
        }
    }

    override fun onDestroy() {
        presenter.stop()
        super.onDestroy()
    }

    private val permissionManager = PermissionManagerImpl(this)

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            readFromIntent(intent)
        }
    }

    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val context = this
            with(parcelables) {
                val inNdefMessage = this[0] as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)
                Toast.makeText(context, inMessage, Toast.LENGTH_LONG).show()
             }
        }
    }
}

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
package com.akqa.kn.app

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager


class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView? = null
    private var wikiFragment = WikiFragment()
    private var postsFragment = PostsFragment()

    private var currentFragment: Fragment? = null
    private val fragmentManager by lazy { supportFragmentManager }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_wiki -> switchFragment(wikiFragment)
            R.id.navigation_firebase -> switchFragment(postsFragment)
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.navigation) as BottomNavigationView
        bottomNavigationView?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        switchFragment(wikiFragment)
    }

    fun switchFragment(fragment: Fragment) {
        if (currentFragment !== fragment) {
            val transaction = fragmentManager.beginTransaction()

            currentFragment?.let {
                transaction.hide(it)
            }

            currentFragment = fragment
            if (!fragment.isAdded) {
                transaction.add(R.id.frame, fragment)
            }

            transaction.show(fragment)
            transaction.commit()
        }
    }

//    override fun onNewIntent(intent: Intent?) {
//        if (intent != null) {
//            readFromIntent(intent)
//        }
//    }
//
//    private fun readFromIntent(intent: Intent) {
//        val action = intent.action
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
//            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
//            val context = this
//            with(parcelables) {
//                val inNdefMessage = this[0] as NdefMessage
//                val inNdefRecords = inNdefMessage.records
//                val ndefRecord_0 = inNdefRecords[0]
//
//                val inMessage = String(ndefRecord_0.payload)
//                Toast.makeText(context, inMessage, Toast.LENGTH_LONG).show()
//             }
//        }
//    }
}


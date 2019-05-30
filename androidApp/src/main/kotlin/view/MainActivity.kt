package com.akqa.kn.app

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
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
}


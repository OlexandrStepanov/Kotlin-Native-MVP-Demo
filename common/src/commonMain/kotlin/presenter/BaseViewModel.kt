package com.sto.kn.lib

import com.sto.kn.lib.redux.*


abstract class BaseViewModel {

    // Members

    protected lateinit var mStore: Store<DemoState>
    private var mSubscription: Subscription? = null

    // Public Api

    fun onCreate(store: Store<DemoState>) {
        mStore = store
        val subscriber = onCreateSubscriber()
        mSubscription = mStore.subscribe(subscriber)
        subscriber.onStateChanged(mStore.getState())
    }

    fun onDestroy() {
        mSubscription?.unsubscribe()
    }

    abstract fun onCreateSubscriber(): Subscriber<DemoState>
}



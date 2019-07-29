package com.sto.kn.lib.redux

import com.sto.kn.lib.redux.Store.Creator

/**
 * Enhancers that give the store new capabilities.
 */
object StoreEnhancers {

    /**
     * An [Enhancer] to dispatch an initial action when the reducer is set.
     *
     * @param <S> The state type
     * @param initialAction A initial [Action]
     * @return An [Enhancer]
     */
    fun <S> applyInitialAction(initialAction: Action, dispatchWhenReplaced: Boolean = true): Enhancer<S> = object : Enhancer<S> {
        override fun enhance(next: Creator<S>): Creator<S> = object : Creator<S> {
            override fun create(initialState: S, reducer: Reducer<S>): Store<S> = object : Store<S>(initialState, reducer) {

                // Members

                private val store = next.create(initialState, reducer)

                // Constructor

                init {
                    // Dispatch the initial action when the reducer is initially set
                    dispatch(initialAction)
                }

                // Store

                override fun subscribe(subscriber: Subscriber<S>): Subscription = store.subscribe(subscriber)

                override fun getState(): S = store.getState()

                override fun replaceReducer(reducer: Reducer<S>) {
                    store.replaceReducer(reducer)
                    // Dispatch the initial action if the reducer is replaced
                    // and the dispatchWhenReplaced is set to true
                    if (dispatchWhenReplaced) {
                        dispatch(initialAction)
                    }
                }

                override fun dispatch(action: Action) = store.dispatch(action)
            }
        }
    }
}

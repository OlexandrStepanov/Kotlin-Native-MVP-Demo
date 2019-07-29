package com.sto.kn.lib.redux

import kotlinx.atomicfu.*

/**
 * Base implementation of the [Store] interface.
 *
 * @param <S> The state type
 * @param initialState The initial state
 * @param reducer The [Reducer]
 */
open class BaseStore<S>
protected constructor(initialState: S, reducer: Reducer<S>) : Store<S>(initialState, reducer) {

    // Members

    protected val isReducing = atomic(false)
    protected val subscribers = ArrayList<Subscriber<S>>()

    // Store

    override fun subscribe(subscriber: Subscriber<S>): Subscription {
        subscribers.add(subscriber)
        return object : Subscription {
            override fun unsubscribe() {
                subscribers.remove(subscriber)
            }
        }
    }

    override fun replaceReducer(reducer: Reducer<S>) {
        this.reducer = reducer
    }

    override fun getState(): S = storedState

    override fun dispatch(action: Action) {
        if (isReducing.compareAndSet(false, true)) {
            try {
                val previousState = storedState
                storedState = reducer.reduce(storedState, action)

                if (previousState != storedState) {
                    subscribers.forEach { it.onStateChanged(storedState) }
                }
            } catch (e: Exception) {
                throw IllegalStateException("Dispatch exception: ${e.message}")
            } finally {
                isReducing.update { false }
            }
        }
    }

    // Creator

    class Creator<S> : Store.Creator<S> {

        override fun create(initialState: S, reducer: Reducer<S>) = BaseStore(initialState, reducer)
    }

    companion object {

        fun <S> create(initialState: S,  reducer: Reducer<S>, vararg enhancers: Enhancer<S>): Store<S> =
                createStore(Creator(), initialState, reducer, *enhancers)
    }
}

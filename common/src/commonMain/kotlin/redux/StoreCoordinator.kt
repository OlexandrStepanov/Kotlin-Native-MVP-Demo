package com.sto.kn.lib

import com.sto.kn.lib.redux.*

class StoreCoordinator(
        val firebase: FirebaseService,
        val initialState: DemoState
) {
    val mStore: Store<DemoState> =
            BaseStore.create(initialState, DemoStateReducer(), Middleware.applyMiddlewares(LoadPostsSideEffect(firebase)))
}


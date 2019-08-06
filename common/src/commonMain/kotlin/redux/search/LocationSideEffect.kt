package com.sto.kn.lib

import com.sto.kn.lib.redux.Action
import com.sto.kn.lib.redux.Dispatcher
import com.sto.kn.lib.redux.SideEffect

internal class LocationSideEffect(private val locationService: AbstractLocationService): SideEffect<DemoState>() {

    override fun dispatch(state: () -> DemoState, action: Action, dispatcher: Dispatcher) {

        fun locationListener() {
            locationService.location?.let {
                dispatcher.dispatch(WikiSearchActions.LocationUpdate(it))
            }
        }

        if (action is WikiSearchActions.StartUpdatingLocation) {
            locationService.onLocationUpdateListeners += ::locationListener
            locationService.start()
        }
        else if (action is WikiSearchActions.StopUpdatingLocation) {
            locationService.onLocationUpdateListeners -= ::locationListener
            locationService.stop()
        }
    }
}
package com.akqa.kn.lib

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NFCPresenter(
        private val nfcService: NFCService,
        val view: NFCView
) {

    private fun onTagUpdate() {
        nfcService.scanResultListeners -= onTagUpdateCallback
        present()
    }

    private val onTagUpdateCallback = ::onTagUpdate

    fun start() {
        nfcService.scanResultListeners += onTagUpdateCallback
        nfcService.initiateScan()
    }

    fun present() {
        nfcService.tag?.let {
            val tag = it.copy()
            GlobalScope.launch(coroutineDispatcher) {
                view.addTag(tag)
            }
        }
    }
}


package com.akqa.kn.lib

interface NFCService {
    fun initiateScan()
    val scanResultListeners: MutableList<() -> Unit>
    val tag: NFCTag?
}

abstract class AbstractNFCService: NFCService {
    override val scanResultListeners = mutableListOf<() -> Unit>()

    final override var tag: NFCTag? = null
        private set

    private fun notifyListeners() {
        scanResultListeners.forEach { it() }
    }

    protected fun update(tag: NFCTag) {
        this.tag = tag
        notifyListeners()
    }
}
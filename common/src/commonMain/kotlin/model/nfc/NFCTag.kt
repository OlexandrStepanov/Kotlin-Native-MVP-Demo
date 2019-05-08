package com.akqa.kn.lib

data class NFCTag(val data: String) {
    companion object {
        val invalidTag = NFCTag("Invalid NFC tag")
    }
}
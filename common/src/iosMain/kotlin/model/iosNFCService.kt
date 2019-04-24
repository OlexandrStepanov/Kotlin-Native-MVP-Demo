package com.akqa.kn.lib

import platform.CoreNFC.*
import platform.darwin.NSObject
import platform.Foundation.NSError
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create

fun NSData.string(): String? {
    return NSString.create(this, NSUTF8StringEncoding) as String?
}

fun NFCNDEFPayload.toNFCTag(): NFCTag {
    var dataString: String? = null
    when (this.typeNameFormat) {
        NFCTypeNameFormatNFCWellKnown -> {
            dataString = this.type.string()
            if (dataString != null) {
                dataString = dataString + ": " + this.payload.string()
            }
        }
        NFCTypeNameFormatAbsoluteURI -> {
            dataString = this.payload.string()
        }
        NFCTypeNameFormatMedia -> {
            dataString = this.type.string()
            if (dataString != null) {
                dataString = "Media Type " + dataString
            }
        }
        NFCTypeNameFormatNFCExternal -> {
            dataString = "External Type"
        }
        NFCTypeNameFormatUnknown -> {
            dataString = "Unknown Type"
        }
        NFCTypeNameFormatUnchanged -> {
            dataString = "Unchanged Type"
        }
        else -> {
            dataString = "Invalid data"
        }
    }
    if (dataString != null) {
        return NFCTag(dataString)
    } else {
        return NFCTag("Invalid data")
    }
}

class NFCServiceImpl: AbstractNFCService() {

    var readerSession: NFCNDEFReaderSession? = null

    private val delegate = object : NSObject(), NFCNDEFReaderSessionDelegateProtocol {
        override fun readerSession(session: NFCNDEFReaderSession, didDetectNDEFs: List<*>) {
            update(((didDetectNDEFs.last() as NFCNDEFMessage).records().last() as NFCNDEFPayload).toNFCTag())
        }

        override fun readerSession(session: NFCNDEFReaderSession, didInvalidateWithError: NSError) {
            print(didInvalidateWithError.localizedDescription as String)
            readerSession = null
        }
    }

    override fun initiateScan() {
        readerSession = NFCNDEFReaderSession(delegate, null, true)
        readerSession?.alertMessage = "Hold your phone near the NFC chip"
        readerSession?.beginSession()
    }

}

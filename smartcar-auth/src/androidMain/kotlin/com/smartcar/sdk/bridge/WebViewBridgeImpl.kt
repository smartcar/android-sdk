package com.smartcar.sdk.bridge

import android.webkit.JavascriptInterface
import android.webkit.WebView

class WebViewBridgeImpl(
    private val webView: WebView,
    private val channelName: String,
): WebViewBridge {

    override lateinit var onMessageFromJS: ((String) -> Unit)

    // Define a JavaScript interface for receiving messages from the webpage.
    private inner class JSInterface {
        @JavascriptInterface
        fun sendMessage(message: String) {
            onMessageFromJS.invoke(message)
        }
    }
    private val jsInterface = JSInterface()
    private var isJavascriptInterfaceAttached = false

    fun attachJavascriptInterface() {
        if (isJavascriptInterfaceAttached) return
        webView.addJavascriptInterface(jsInterface, channelName)
        isJavascriptInterfaceAttached = true
    }

    fun detachJavascriptInterface() {
        if (!isJavascriptInterfaceAttached) return
        webView.removeJavascriptInterface(channelName)
        isJavascriptInterfaceAttached = false
    }

    override fun evaluateJavaScript(script: String, callback: (String?) -> Unit) {
        webView.evaluateJavascript(script) { result ->
            callback(result)
        }
    }
}

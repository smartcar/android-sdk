package com.smartcar.sdk.bridge

import android.webkit.JavascriptInterface
import android.webkit.WebView

class WebViewBridgeImpl(
    private val webView: WebView,
    channelName: String,
): WebViewBridge {

    override lateinit var onMessageFromJS: ((String) -> Unit)

    // Define a JavaScript interface for receiving messages from the webpage.
    private inner class JSInterface {
        @JavascriptInterface
        fun postMessage(message: String) {
            onMessageFromJS.invoke(message)
        }
    }

    init {
        // Add the JavaScript interface to the WebView.
        webView.addJavascriptInterface(JSInterface(), channelName)
    }

    override fun evaluateJavaScript(script: String, callback: (String?) -> Unit) {
        webView.evaluateJavascript(script) { result ->
            callback(result)
        }
    }
}

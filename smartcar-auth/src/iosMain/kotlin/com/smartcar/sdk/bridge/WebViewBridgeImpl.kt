package com.smartcar.sdk.bridge

import platform.Foundation.NSNull
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKWebView
import platform.darwin.NSObject

class WebViewBridgeImpl(
    private val webView: WKWebView,
    interfaceName: String,
): WebViewBridge {

    override lateinit var onMessageFromJS: ((String) -> Unit)

    private val messageHandler = object : NSObject(), WKScriptMessageHandlerProtocol {
        override fun userContentController(
            userContentController: WKUserContentController,
            didReceiveScriptMessage: WKScriptMessage
        ) {
            val body = didReceiveScriptMessage.body
            onMessageFromJS.invoke(body.toString())
        }
    }

    init {
        // Configure the WKUserContentController to use our message handler
        val userContentController = webView.configuration.userContentController
        userContentController.addScriptMessageHandler(messageHandler, interfaceName)
    }

    override fun evaluateJavaScript(script: String, callback: (String?) -> Unit) {
        webView.evaluateJavaScript(script) { result, error ->
            when {
                error != null -> callback(null)
                result is NSNull -> callback(null)
                else -> callback(result?.toString())
            }
        }
    }
}

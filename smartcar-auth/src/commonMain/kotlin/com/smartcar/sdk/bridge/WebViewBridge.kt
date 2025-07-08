package com.smartcar.sdk.bridge

interface WebViewBridge {
    var onMessageFromJS: ((String) -> Unit)
    fun evaluateJavaScript(script: String, callback: (String?) -> Unit)
}

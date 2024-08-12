package com.smartcar.sdk

import android.net.Uri
import android.webkit.WebView

class ConnectActivity : OAuthCaptureActivity() {

    override fun initWebView(webView: WebView) {
        val rpc = RPCInterface(this, activityResultRegistry, webView)
        lifecycle.addObserver(rpc)
        webView.addJavascriptInterface(rpc, "SmartcarSDK")
        super.initWebView(webView)
    }

    override fun onInterceptUri(uri: Uri) {
        SmartcarAuth.receiveResponse(uri)
        super.onInterceptUri(uri)
    }
}

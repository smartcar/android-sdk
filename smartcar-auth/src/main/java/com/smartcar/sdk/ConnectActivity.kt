package com.smartcar.sdk

import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView

class ConnectActivity : OAuthCaptureActivity() {

    override fun initWebView(webView: WebView) {
        clearWebViewData(webView)

        val rpc = RPCInterface(this, activityResultRegistry, webView)
        lifecycle.addObserver(rpc)
        webView.addJavascriptInterface(rpc, "SmartcarSDK")
        super.initWebView(webView)
    }

    override fun onDestroyWebView(webView: WebView) {
        clearWebViewData(webView)
        super.onDestroyWebView(webView)
    }

    private fun clearWebViewData(webView: WebView) {
        webView.stopLoading()
        webView.loadUrl("about:blank")
        webView.clearCache(true)
        CookieManager.getInstance().removeAllCookies(null)
        WebStorage.getInstance().deleteAllData()
    }

    override fun onInterceptUri(uri: Uri) {
        SmartcarAuth.receiveResponse(uri)
        super.onInterceptUri(uri)
    }
}

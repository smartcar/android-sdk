package com.smartcar.sdk

import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import com.smartcar.sdk.rpc.ble.BLEService
import com.smartcar.sdk.rpc.oauth.OAuthService

class ConnectActivity : OAuthCaptureActivity() {

    override fun initWebView(webView: WebView) {
        clearWebViewData(webView)

        val rpcService = OAuthService(this, webView)
        val bleService = BLEService(this, webView)

        // Add lifecycle observers
        lifecycle.addObserver(rpcService)
        lifecycle.addObserver(bleService)

        // Attach interfaces to WebView
        webView.addJavascriptInterface(rpcService, rpcService.channelName)
        webView.addJavascriptInterface(bleService, bleService.channelName)

        super.initWebView(webView)
    }

    override fun onDestroyWebView(webView: WebView) {
        clearWebViewData(webView)
        super.onDestroyWebView(webView)
    }

    /**
     * Used to ensure we clear any lingering OEM login sessions.
     */
    private fun clearWebViewData(webView: WebView) {
        webView.clearCache(true)
        CookieManager.getInstance().removeAllCookies(null)
        WebStorage.getInstance().deleteAllData()
    }

    override fun onInterceptUri(uri: Uri) {
        SmartcarAuth.receiveResponse(uri)
        super.onInterceptUri(uri)
    }
}

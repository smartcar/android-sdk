package com.smartcar.sdk

import com.smartcar.sdk.bridge.OAuthCaptureBridgeImpl
import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import com.smartcar.sdk.bridge.WebViewBridgeImpl
import com.smartcar.sdk.rpc.ble.BLEService
import com.smartcar.sdk.rpc.oauth.OAuthService

class ConnectActivity : OAuthCaptureActivity() {
    private lateinit var oauthService: OAuthService
    private lateinit var bleService: BLEService

    override fun initWebView(webView: WebView) {
        clearWebViewData(webView)

        oauthService = OAuthService(OAuthCaptureBridgeImpl(this), WebViewBridgeImpl(webView, "SmartcarSDK"))
        bleService = BLEService(WebViewBridgeImpl(webView, "SmartcarSDKBLE"))

        super.initWebView(webView)
    }

    override fun onDestroyWebView(webView: WebView) {
        oauthService.dispose()
        bleService.dispose()

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

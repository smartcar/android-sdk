package com.smartcar.sdk.activity

import com.smartcar.sdk.bridge.ContextBridgeImpl
import android.net.Uri
import android.webkit.WebView
import com.smartcar.sdk.SmartcarAuth
import com.smartcar.sdk.bridge.WebViewBridgeImpl
import com.smartcar.sdk.rpc.ble.BLEService
import com.smartcar.sdk.rpc.oauth.OAuthService

class ConnectActivity : WebViewActivity() {
    private lateinit var oauthService: OAuthService
    private lateinit var bleService: BLEService

    override fun initWebView(webView: WebView) {
        oauthService = OAuthService(ContextBridgeImpl(this), WebViewBridgeImpl(webView, "SmartcarSDK"))
        bleService = BLEService(ContextBridgeImpl(this), WebViewBridgeImpl(webView, "SmartcarSDKBLE"))

        super.initWebView(webView)
    }

    override fun onDestroyWebView(webView: WebView) {
        oauthService.dispose()
        bleService.dispose()

        super.onDestroyWebView(webView)
    }

    override fun onInterceptUri(uri: Uri) {
        SmartcarAuth.receiveResponse(uri)
        super.onInterceptUri(uri)
    }
}

package com.smartcar.sdk.activity

import com.smartcar.sdk.bridge.ContextBridgeImpl
import android.net.Uri
import android.webkit.WebView
import com.smartcar.sdk.SmartcarAuth
import com.smartcar.sdk.bridge.WebViewBridgeImpl
import com.smartcar.sdk.rpc.oauth.OAuthService

class ConnectActivity : WebViewActivity() {
    private lateinit var oauthService: OAuthService

    override fun initWebView(webView: WebView) {
        oauthService = OAuthService(ContextBridgeImpl(this), WebViewBridgeImpl(webView, "SmartcarSDK"))

        super.initWebView(webView)
    }

    override fun onDestroyWebView(webView: WebView) {
        oauthService.dispose()

        super.onDestroyWebView(webView)
    }

    override fun onInterceptUri(uri: Uri) {
        SmartcarAuth.receiveResponse(uri)
        super.onInterceptUri(uri)
    }
}

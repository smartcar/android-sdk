package com.smartcar.sdk.activity

import com.smartcar.sdk.bridge.ContextBridgeImpl
import android.net.Uri
import android.webkit.WebView
import com.smartcar.sdk.SmartcarAuth
import com.smartcar.sdk.bridge.WebViewBridgeImpl
import com.smartcar.sdk.rpc.ble.BLEService
import com.smartcar.sdk.rpc.oauth.OAuthService

class ConnectActivity : WebViewActivity() {
    private var oauthService: OAuthService? = null
    private var bleService: BLEService? = null
    private lateinit var oauthBridge: WebViewBridgeImpl
    private lateinit var bleBridge: WebViewBridgeImpl

    override fun initWebView(webView: WebView) {
        oauthBridge = WebViewBridgeImpl(webView, "SmartcarSDK")
        bleBridge = WebViewBridgeImpl(webView, "SmartcarSDKBLE")

        super.initWebView(webView)
    }

    override fun onAllowedHostChanged(isAllowedHost: Boolean) {
        if (!::oauthBridge.isInitialized || !::bleBridge.isInitialized) return

        if (isAllowedHost) {
            if (oauthService == null) {
                oauthService = OAuthService(ContextBridgeImpl(this), oauthBridge)
            }
            if (bleService == null) {
                bleService = BLEService(ContextBridgeImpl(this), bleBridge)
            }
            oauthBridge.attachJavascriptInterface()
            bleBridge.attachJavascriptInterface()
        } else {
            destroyServices()
        }
    }

    private fun destroyServices() {
        if (::oauthBridge.isInitialized) {
            oauthBridge.detachJavascriptInterface()
        }
        if (::bleBridge.isInitialized) {
            bleBridge.detachJavascriptInterface()
        }
        oauthService?.dispose()
        bleService?.dispose()
        oauthService = null
        bleService = null
    }

    override fun onDestroy() {
        // If the user dismissed Connect (back press / swipe-away) the activity is finishing
        // and no redirect was intercepted, so deliver a user_exited response. isFinishing is
        // false for configuration changes (rotation) and while backgrounded for OEM
        // app-to-app handoff, so this does not false-fire mid-flow.
        if (isFinishing) {
            SmartcarAuth.dispatchUserExitedIfNoResponse()
        }
        super.onDestroy()
    }

    override fun onDestroyWebView(webView: WebView) {
        destroyServices()
        super.onDestroyWebView(webView)
    }

    override fun onInterceptUri(uri: Uri, interceptPrefix: String) {
        SmartcarAuth.receiveResponse(uri, interceptPrefix)
        super.onInterceptUri(uri, interceptPrefix)
    }
}

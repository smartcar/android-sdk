package com.smartcar.sdk.activity

import com.smartcar.sdk.bridge.ContextBridgeImpl
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri
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

        // Connect uses a target="_blank" link for certain operations and expects it to open in
        // a new tab so that Connect remains active underneath. WebView ignores that by default and
        // navigates this WebView away instead — send it to an external browser so Connect
        // isn't replaced. We deliberately don't set javaScriptCanOpenWindowsAutomatically:
        // Connect has no legitimate reason to call window.open() itself, so leaving it at
        // its default (false) means only a real user tap can reach onCreateWindow at all.
        webView.settings.setSupportMultipleWindows(true)
        webView.webChromeClient = NewWindowChromeClient()

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

    /**
     * Handles target="_blank" links, which WebView otherwise silently drops. There's no
     * second screen to show a real new window in here, so this catches the URL the page
     * wanted to open in one and hands it to an external browser, leaving Connect's current
     * page untouched.
     */
    inner class NewWindowChromeClient : WebChromeClient() {
        override fun onCreateWindow(
            view: WebView,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message
        ): Boolean {
            val popup = WebView(view.context)
            popup.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    popupView: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    openExternally(request.url)
                    return true
                }

                override fun onPageStarted(popupView: WebView, url: String?, favicon: android.graphics.Bitmap?) {
                    url?.let { openExternally(it.toUri()) }
                    popup.stopLoading()
                    popup.destroy()
                }
            }

            @Suppress("UNCHECKED_CAST")
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = popup
            resultMsg.sendToTarget()
            return true
        }

        private fun openExternally(url: Uri) {
            Log.d("OAuthCapture", "onCreateWindow: opening new-tab URL externally: $url")
            try {
                startActivity(Intent(Intent.ACTION_VIEW, url))
            } catch (error: Exception) {
                Log.w("OAuthCapture", "Failed to open new-tab URL: $url", error)
            }
        }
    }
}

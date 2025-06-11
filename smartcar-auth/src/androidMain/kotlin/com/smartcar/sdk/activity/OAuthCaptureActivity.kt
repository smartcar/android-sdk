package com.smartcar.sdk.activity

import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView

/**
 * This activity runs in a dedicated process so we can isolate
 * the WebView and clear its cookies without affecting the state of
 * the host app or Connect WebViews.
 */
class OAuthCaptureActivity : WebViewActivity() {

    /**
     * Set the WebView data directory suffix once per process lifetime,
     * before any WebViews are initialized. This only runs in the
     * dedicated OAuth capture process.
     */
    companion object {
        init {
            WebView.setDataDirectorySuffix("smartcar_oauth")
        }
    }

    override fun initWebView(webView: WebView) {
        clearWebViewData(webView)
        super.initWebView(webView)
    }

    override fun onDestroyWebView(webView: WebView) {
        clearWebViewData(webView)
        super.onDestroyWebView(webView)
    }

    /**
     * Make sure we clear any lingering OEM login sessions.
     */
    private fun clearWebViewData(webView: WebView) {
        webView.clearCache(true)
        CookieManager.getInstance().removeAllCookies(null)
        WebStorage.getInstance().deleteAllData()
    }
}

package com.smartcar.sdk.activity

import android.os.Build
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import androidx.annotation.RequiresApi

/**
 * This activity runs in a dedicated process so we can isolate
 * the WebView and clear its cookies without affecting the state of
 * the host app or Connect WebViews.
 * If isolation is not available (API < 28), the base WebViewActivity
 * is used for OAuth capture instead.
 */
class WebViewActivityIsolated : WebViewActivity() {

    /**
     * Set the WebView data directory suffix once per process lifetime,
     * before any WebViews are initialized. This only runs in the
     * dedicated OAuth capture process.
     */
    @RequiresApi(Build.VERSION_CODES.P)
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

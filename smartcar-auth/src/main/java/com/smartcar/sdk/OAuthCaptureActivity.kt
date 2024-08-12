package com.smartcar.sdk

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings.LOAD_NO_CACHE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity

open class OAuthCaptureActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a WebView programmatically
        val webView = WebView(this)
        setContentView(webView)
        initWebView(webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    open fun initWebView(webView: WebView) {
        // Enable JavaScript and DOM storage
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.cacheMode = LOAD_NO_CACHE

        // Get the URL and optional hostname from the Intent
        val startUrl = intent.getStringExtra("start_url")
        val interceptPrefix = intent.getStringExtra("intercept_prefix")
        val allowedHost = intent.getStringExtra("allowed_host")

        // Set a custom WebViewClient
        webView.webViewClient = CustomWebViewClient(interceptPrefix, allowedHost)

        // Load the URL
        startUrl?.let { webView.loadUrl(it) }
    }

    open fun onInterceptUri(uri: Uri) {
        val resultIntent = Intent()
        resultIntent.putExtra("return_uri", uri.toString())
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    inner class CustomWebViewClient(
        private val interceptPrefix: String?,
        private val allowedHost: String?
    ) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            request?.url.let { url ->
                // Check if the URL should be intercepted
                if (interceptPrefix != null && url.toString().startsWith(interceptPrefix)) {
                    Log.d("OAuthCapture", "Intercepted URL: $url")
                    request?.let { onInterceptUri(it.url) }
                    return true
                }

                // Check if the URL matches the allowed hostname, if specified
                if (allowedHost != null && url?.host != allowedHost) {
                    Log.d("OAuthCapture", "Opening external URL: $url")
                    Intent(Intent.ACTION_VIEW, url).apply {
                        startActivity(this)
                    }
                    return true
                }
            }

            return false
        }
    }
}

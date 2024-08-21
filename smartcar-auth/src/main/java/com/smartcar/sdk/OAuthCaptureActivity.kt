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
    private var headerConfig: List<HeaderConfig>? = null

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
        val authorizeURL = intent.getStringExtra("authorize_url")
        val interceptPrefix = intent.getStringExtra("intercept_prefix")
        val allowedHost = intent.getStringExtra("allowed_host")
        val headerConfigJson = intent.getStringExtra("header_config")
        headerConfigJson?.let { headerConfig = json.decodeFromString(it) }

        // Set a custom WebViewClient
        webView.webViewClient = CustomWebViewClient(interceptPrefix, allowedHost)

        // Load the URL
        authorizeURL?.let {
            val headers = getAdditionalHeaders(it)
            // Set the user agent if specified
            getUserAgentFromHeaders(headers)?.let { userAgent ->
                webView.settings.userAgentString = userAgent
            }
            // Load the URL with all headers including User-Agent
            webView.loadUrl(it, headers)
        }
    }

    private fun getAdditionalHeaders(url: String): Map<String, String> {
        for (config in headerConfig ?: emptyList()) {
            val regex = Regex(config.pattern)
            if (regex.matches(url)) {
                return config.headers
            }
        }
        return emptyMap()
    }

    private fun getUserAgentFromHeaders(headers: Map<String, String>): String? {
        return headers.entries.firstOrNull {
            entry -> entry.key.equals("User-Agent", ignoreCase = true)
        }?.value
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

                // Load with custom headers if applicable
                val headers = getAdditionalHeaders(url.toString())
                if (headers.isNotEmpty()) {
                    // Set the user agent if specified
                    getUserAgentFromHeaders(headers)?.let { userAgent ->
                        view?.settings?.userAgentString = userAgent
                    }
                    // Load the URL with all headers including User-Agent
                    view?.loadUrl(url.toString(), headers)
                    return true
                }
            }

            return false
        }
    }
}
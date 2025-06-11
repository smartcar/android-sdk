package com.smartcar.sdk.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import com.smartcar.sdk.rpc.oauth.HeaderConfig
import kotlinx.serialization.json.Json

/**
 * Base class for ConnectActivity and OAuthCaptureActivity.
 * Displays a WebView and intercepts requests to a given callback URL.
 */
abstract class WebViewActivity : ComponentActivity() {
    private var headerConfig: List<HeaderConfig>? = null
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a WebView programmatically
        webView = WebView(this)
        setContentView(webView)
        initWebView(webView)
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyWebView(webView)
    }

    /**
     * Frees up resources from the WebView.
     */
    open fun onDestroyWebView(webView: WebView) {
        webView.stopLoading()
        webView.loadUrl("about:blank")

        // Remove the WebView from its parent if it has one
        val parent = webView.parent
        if (parent is ViewGroup) {
            parent.removeView(webView)
        }
        webView.destroy()
    }

    @SuppressLint("SetJavaScriptEnabled")
    open fun initWebView(webView: WebView) {
        // Enable JavaScript and DOM storage
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // Get the URL and optional hostname from the Intent
        val authorizeURL = intent.getStringExtra("authorize_url")
        val interceptPrefix = intent.getStringExtra("intercept_prefix")
        val allowedHost = intent.getStringExtra("allowed_host")
        val headerConfigJson = intent.getStringExtra("header_config")
        headerConfigJson?.let { headerConfig = Json.decodeFromString(it) }

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
            request?.url?.let { url ->
                // Check if the URL should be intercepted
                if (interceptPrefix != null && url.toString().startsWith(interceptPrefix)) {
                    Log.d("OAuthCapture", "Intercepted URL: $url")
                    onInterceptUri(url)
                    return true
                }

                // Check if the URL matches the allowed hostname, if specified
                if (allowedHost != null && url.host != allowedHost) {
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

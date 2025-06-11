package com.smartcar.sdk.bridge

import android.content.Intent
import androidx.activity.ComponentActivity
import com.smartcar.sdk.activity.OAuthCaptureActivity
import com.smartcar.sdk.activity.awaitActivityResult

class ContextBridgeImpl(
    private val activity: ComponentActivity
): ContextBridge {
    override suspend fun startOAuthCapture(
        authorizeUrl: String,
        interceptPrefix: String,
        headerConfig: String
    ): String? {
        val intent = Intent(activity, OAuthCaptureActivity::class.java).apply {
            putExtra("authorize_url", authorizeUrl)
            putExtra("intercept_prefix", interceptPrefix)
            putExtra("header_config", headerConfig)
        }

        // Await the result using our extension function
        val result = activity.awaitActivityResult(intent)
        val returnUri = result.data?.getStringExtra("return_uri")

        return returnUri
    }
}

package com.smartcar.sdk.rpc.oauth

import android.content.Intent
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import com.smartcar.sdk.OAuthCaptureActivity
import com.smartcar.sdk.util.awaitActivityResult
import com.smartcar.sdk.rpc.JsonRpcRequest
import com.smartcar.sdk.rpc.JsonRpcResult
import com.smartcar.sdk.rpc.RPCInterface
import com.smartcar.sdk.rpc.RpcException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class OAuthService(
    private val activity: ComponentActivity,
    webView: WebView,
) : RPCInterface(
    "SmartcarSDK",
    webView,
    SerializersModule {
        // Register request classes
        polymorphic(JsonRpcRequest::class) {
            subclass(OAuthRequest::class)
        }
        // Register result classes
        polymorphic(JsonRpcResult::class) {
            subclass(OAuthResult::class)
        }
    }
) {
    override suspend fun handleRequest(request: JsonRpcRequest): JsonRpcResult {
        return when (request) {
            is OAuthRequest -> {
                Log.d("OAuthCapture", "Starting capture with URL " + request.params.authorizeURL)

                val intent = Intent(activity, OAuthCaptureActivity::class.java).apply {
                    putExtra("authorize_url", request.params.authorizeURL)
                    putExtra("intercept_prefix", request.params.interceptPrefix)
                    putExtra("header_config", Json.encodeToString(request.params.headerConfig))
                }

                // Await the result using our extension function
                val result = activity.awaitActivityResult(intent)
                val returnUri = result.data?.getStringExtra("return_uri")

                if (returnUri == null) {
                    Log.e("OAuthCapture", "OAuth capture cancelled")
                    throw RpcException(-32000, "OAuth capture cancelled")
                } else {
                    Log.d("OAuthCapture", "OAuth capture successful, returnUri: $returnUri")
                    OAuthResult(returnUri = returnUri)
                }
            }

            else -> throw IllegalArgumentException("Unsupported request type")
        }
    }
}

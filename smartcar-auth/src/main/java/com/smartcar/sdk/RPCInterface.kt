package com.smartcar.sdk

import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

const val RESPONSE_CHANNEL = "SmartcarSDKResponse"

@Serializable
data class OAuthParams(
    val authorizeURL: String,
    val interceptPrefix: String,
    val headerConfig: List<HeaderConfig>? = null
)

@Serializable
data class HeaderConfig(
    val pattern: String,
    val headers: Map<String, String>
)

@Serializable
data class OAuthResult(val returnUri: String)

@Serializable
sealed class JsonRpcRequest {
    abstract val jsonrpc: String
    abstract val id: Int

    @Serializable
    @SerialName("oauth")
    data class OAuthRequest(
        override val jsonrpc: String,
        override val id: Int,
        val params: OAuthParams
    ) : JsonRpcRequest()
}

@Serializable
data class JsonRpcResponse<T>(
    val jsonrpc: String = "2.0",
    val result: T,
    val id: Int
)

@Serializable
data class JsonRpcErrorResponse(
    val jsonrpc: String = "2.0",
    val error: JsonRpcError,
    val id: Int?
)

@Serializable
data class JsonRpcError(
    val code: Int,
    val message: String
)

val json = Json {
    classDiscriminator = "method"
    encodeDefaults = true
}

class RPCInterface(
    private val context : Context,
    private val registry : ActivityResultRegistry,
    private val webView: WebView
) : DefaultLifecycleObserver {
    private lateinit var getResult : ActivityResultLauncher<Intent>
    private var callback : ActivityResultCallback<ActivityResult>? = null

    override fun onCreate(owner: LifecycleOwner) {
        getResult = registry.register("key", owner, ActivityResultContracts.StartActivityForResult()) { result ->
            callback?.onActivityResult(result)
        }
    }

    private fun handleJsonRpcRequest(request: JsonRpcRequest) {
        return when (request) {
            is JsonRpcRequest.OAuthRequest -> {
                // Start OAuthCaptureActivity
                val intent = Intent(context, OAuthCaptureActivity::class.java)
                intent.putExtra("authorize_url", request.params.authorizeURL)
                intent.putExtra("intercept_prefix", request.params.interceptPrefix)
                intent.putExtra("header_config", json.encodeToString(request.params.headerConfig))
                getResult.launch(intent)

                // Set callback to handle activity result
                callback = ActivityResultCallback { result ->
                    val returnUri = result.data?.getStringExtra("return_uri")
                    if (returnUri == null) {
                        val error = JsonRpcError(code = -32000, message = "OAuth capture cancelled")
                        val response = JsonRpcErrorResponse(error = error, id = request.id)
                        sendErrorResponse(response)
                    } else {
                        val oAuthResult = OAuthResult(returnUri = returnUri)
                        val response = JsonRpcResponse(result = oAuthResult, id = request.id)
                        sendResponse(JsonRpcResponse.serializer(OAuthResult.serializer()), response)
                    }
                }
            }
        }
    }

    private fun <T> sendResponse(serializer: SerializationStrategy<JsonRpcResponse<T>>, response: JsonRpcResponse<T>) {
        val jsonStr = json.encodeToString(serializer, response)
        Log.d("RPCInterface", "RPC response: $jsonStr")
        sendJSONResponse(jsonStr)
    }

    private fun sendErrorResponse(response: JsonRpcErrorResponse) {
        val jsonStr = json.encodeToString(response)
        Log.d("RPCInterface", "RPC error response: $jsonStr")
        sendJSONResponse(jsonStr)
    }

    private fun sendJSONResponse(jsonStr: String) {
        val escaped = json.encodeToString(jsonStr)
        val js = "dispatchEvent(new CustomEvent('$RESPONSE_CHANNEL', { detail: JSON.parse($escaped) }))"
        webView.evaluateJavascript(js, null)
    }

    @JavascriptInterface
    fun sendMessage(jsonStr: String) {
        Log.d("RPCInterface", "RPC request: $jsonStr")
        val request = json.decodeFromString(JsonRpcRequest.serializer(), jsonStr)
        handleJsonRpcRequest(request)
    }
}

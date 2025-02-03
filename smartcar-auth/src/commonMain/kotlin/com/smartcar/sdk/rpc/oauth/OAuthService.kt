package com.smartcar.sdk.rpc.oauth

import co.touchlab.kermit.Logger
import com.smartcar.sdk.rpc.JsonRpcRequest
import com.smartcar.sdk.rpc.JsonRpcResult
import com.smartcar.sdk.rpc.RPCInterface
import com.smartcar.sdk.rpc.RpcException
import com.smartcar.sdk.bridge.WebViewBridge
import com.smartcar.sdk.bridge.OAuthCaptureBridge
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class OAuthService(
    private val captureBridge: OAuthCaptureBridge,
    webView: WebViewBridge,
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
                Logger.d("OAuthCapture") { "Starting capture with URL " + request.params.authorizeURL }

                val returnUri = captureBridge.start(
                    request.params.authorizeURL,
                    request.params.interceptPrefix,
                    Json.encodeToString(request.params.headerConfig)
                )

                if (returnUri == null) {
                    Logger.d("OAuthCapture") { "OAuth capture cancelled" }
                    throw RpcException(-32000, "OAuth capture cancelled")
                } else {
                    Logger.d("OAuthCapture") { "OAuth capture successful, returnUri: $returnUri" }
                    OAuthResult(returnUri = returnUri)
                }
            }

            else -> throw IllegalArgumentException("Unsupported request type")
        }
    }
}

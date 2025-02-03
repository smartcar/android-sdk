package com.smartcar.sdk.rpc

import co.touchlab.kermit.Logger
import com.smartcar.sdk.bridge.WebViewBridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.modules.SerializersModule

@Serializable
abstract class JsonRpcRequest {
    val jsonrpc: String = "2.0"
    val id: JsonPrimitive? = null
}

@Serializable
data class JsonRpcResponse(
    val jsonrpc: String = "2.0",
    val result: JsonRpcResult,
    val id: JsonPrimitive?
)

@Serializable
abstract class JsonRpcResult

@Serializable
data class JsonRpcErrorResponse(
    val jsonrpc: String = "2.0",
    val error: JsonRpcError,
    val id: JsonPrimitive?
) {
    @Serializable
    data class JsonRpcError(
        val code: Int,
        val message: String
    )
}

class RpcException(
    val errorCode: Int,
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    constructor(errorCode: Int, message: String?) : this(errorCode, message, null)
    constructor(errorCode: Int, cause: Throwable?) : this(errorCode, null, cause)
}

abstract class RPCInterface(
    private val channelName: String,
    private val webView: WebViewBridge,
    private val serializersModule: SerializersModule
) {
    init {
        webView.onMessageFromJS = ::acceptMessage
    }

    private val json = Json {
        classDiscriminator = "method"
        encodeDefaults = true
        serializersModule = this@RPCInterface.serializersModule
    }
    private val responseSerializer = Json {
        classDiscriminator = "type"
        encodeDefaults = true
        serializersModule = this@RPCInterface.serializersModule
    }
    protected val scope = CoroutineScope(Dispatchers.Main)

    abstract suspend fun handleRequest(request: JsonRpcRequest): JsonRpcResult

    private fun sendResponse(
        serializer: SerializationStrategy<JsonRpcResponse>,
        response: JsonRpcResponse
    ) {
        val jsonStr = responseSerializer.encodeToString(serializer, response)
        Logger.d("RPCInterface") { "RPC response: $jsonStr" }
        sendToWebView(jsonStr)
    }

    private fun sendErrorResponse(code: Int, message: String, id: JsonPrimitive?) {
        val error = JsonRpcErrorResponse.JsonRpcError(code, message)
        val response = JsonRpcErrorResponse(error = error, id = id)
        val jsonStr = json.encodeToString(response)
        Logger.d("RPCInterface") { "RPC error response: $jsonStr" }
        sendToWebView(jsonStr)
    }

    fun sendToWebView(jsonStr: String) {
        val escaped = json.encodeToString(jsonStr)
        val js = "dispatchEvent(new CustomEvent('${channelName}Response', { detail: JSON.parse($escaped) }))"
        scope.launch {
            webView.evaluateJavaScript(js) {}
        }
    }

    /**
     * Function exposed to the WebView through JavaScript interface
     * which accepts JSON-RPC requests
     */
    private fun acceptMessage(jsonStr: String) {
        Logger.d("RPCInterface") { "RPC request: $jsonStr" }
        val request = json.decodeFromString(JsonRpcRequest.serializer(), jsonStr)

        scope.launch {
            try {
                val result = handleRequest(request)
                val response = JsonRpcResponse(id = request.id, result = result)
                val serializer = JsonRpcResponse.serializer()
                sendResponse(serializer, response)
            } catch (e: RpcException) {
                sendErrorResponse(e.errorCode, e.message.orEmpty(), request.id)
            } catch (e: Exception) {
                Logger.d("RPCInterface", e) { "Unhandled exception" }
                sendErrorResponse(-32099, e.message.orEmpty(), request.id)
            }
        }
    }

    open fun dispose() {
        scope.cancel()
    }
}

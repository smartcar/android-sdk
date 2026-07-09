package com.smartcar.sdk.rpc.oauth

import com.smartcar.sdk.rpc.JsonRpcRequest
import com.smartcar.sdk.rpc.JsonRpcResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("oauth")
data class OAuthRequest(
    val params: OAuthParams
) : JsonRpcRequest() {
    @Serializable
    data class OAuthParams(
        val authorizeURL: String,
        val interceptPrefix: String,
        val headerConfig: List<HeaderConfig>? = null
    )
}

@Serializable
data class HeaderConfig(
    val pattern: String,
    val headers: Map<String, String>
)

@Serializable
data class OAuthResult(val returnUri: String) : JsonRpcResult()

@Serializable
@SerialName("complete")
data class CompleteRequest(
    val params: CompleteParams
) : JsonRpcRequest() {
    @Serializable
    data class CompleteParams(
        val code: String? = null,
        val userId: String? = null,
        val externalId: String? = null,
        val error: String? = null,
        val errorDescription: String? = null,
        val state: String? = null,
        val vin: String? = null,
        val make: String? = null,
        val virtualKeyUrl: String? = null
    )
}

@Serializable
data class CompleteResult(val acknowledged: Boolean = true) : JsonRpcResult()

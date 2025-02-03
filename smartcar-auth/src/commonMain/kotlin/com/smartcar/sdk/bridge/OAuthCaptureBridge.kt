package com.smartcar.sdk.bridge

interface OAuthCaptureBridge {
    suspend fun start(
        authorizeUrl: String, interceptPrefix: String,
        headerConfig: String
    ) : String?
}

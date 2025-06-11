package com.smartcar.sdk.bridge

/**
 * Interface providing actions that require a context i.e. Activity on android
 */
interface ContextBridge {
    suspend fun startOAuthCapture(
        authorizeUrl: String, interceptPrefix: String,
        headerConfig: String
    ) : String?
}

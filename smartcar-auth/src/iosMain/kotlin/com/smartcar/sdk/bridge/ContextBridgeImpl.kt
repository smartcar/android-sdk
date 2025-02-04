package com.smartcar.sdk.bridge

class ContextBridgeImpl: ContextBridge {
    override suspend fun startOAuthCapture(
        authorizeUrl: String,
        interceptPrefix: String,
        headerConfig: String
    ): String? {
        TODO("Not yet implemented")
    }

    override suspend fun checkBLEPermissions(): Boolean {
        // Not necessary on iOS
        return true
    }
}

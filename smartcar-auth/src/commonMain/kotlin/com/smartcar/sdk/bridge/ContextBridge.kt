package com.smartcar.sdk.bridge

import com.smartcar.sdk.rpc.ble.Availability

/**
 * Interface providing actions that require a context i.e. Activity on android
 */
interface ContextBridge {
    suspend fun startOAuthCapture(
        authorizeUrl: String, interceptPrefix: String,
        headerConfig: String
    ) : String?

    suspend fun getBLEAvailability(): Availability

    fun openSystemPage(page: String)
}

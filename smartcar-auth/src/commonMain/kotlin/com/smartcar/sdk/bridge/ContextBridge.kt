package com.smartcar.sdk.bridge

import com.smartcar.sdk.rpc.ble.Availability
import com.smartcar.sdk.rpc.oauth.CompleteRequest

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

    /**
     * Delivers a Connect result received directly over the RPC bridge (no redirect)
     * to the host app via SmartcarCallback.
     */
    fun deliverCompleteResult(params: CompleteRequest.CompleteParams)

    /**
     * Closes the current Connect screen once the flow has finished.
     */
    fun finishConnect()
}

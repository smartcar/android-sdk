package com.smartcar.sdk.bridge

import com.smartcar.sdk.rpc.ble.Availability
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOff
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBManagerStateUnauthorized
import platform.darwin.NSObject
import kotlin.coroutines.resume

class ContextBridgeImpl: ContextBridge {
    override suspend fun startOAuthCapture(
        authorizeUrl: String,
        interceptPrefix: String,
        headerConfig: String
    ): String? {
        TODO("Not yet implemented")
    }

    // Keep strong refs while the check is in flight
    private var managerRef: NSObject? = null
    private var delegateRef: NSObject? = null

    override suspend fun getBLEAvailability(): Availability = suspendCancellableCoroutine { cont ->
        // Delegate that captures the centralManager callback
        val delegate = object : NSObject(), CBCentralManagerDelegateProtocol {
            override fun centralManagerDidUpdateState(central: CBCentralManager) {
                if (cont.isActive) {
                    cont.resume(when (central.state) {
                        CBManagerStatePoweredOn -> Availability.Available
                        CBManagerStatePoweredOff -> Availability.BluetoothOff
                        CBManagerStateUnauthorized -> Availability.PermissionDenied
                        else -> Availability.Unknown
                    })
                }
            }
        }

        // Hold refs so delegate stays alive
        managerRef = CBCentralManager(delegate, null, null)
        delegateRef = delegate

        // Clean up refs if the coroutine is cancelled
        cont.invokeOnCancellation {
            managerRef = null
            delegateRef = null
        }
    }
}

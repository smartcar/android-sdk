package com.smartcar.sdk.bridge

import com.juul.kable.Peripheral

/**
 * Requests maximum MTU on Android. No-op on iOS
 */
expect suspend fun Peripheral.requestMtuIfAvailable()

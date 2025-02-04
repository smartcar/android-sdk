package com.smartcar.sdk.bridge

import com.juul.kable.AndroidPeripheral
import com.juul.kable.Peripheral

const val MAX_MTU = 517;

actual suspend fun Peripheral.requestMtuIfAvailable() {
    this as AndroidPeripheral
    this.requestMtu(MAX_MTU)
}

package com.smartcar.sdk.bridge

import com.juul.kable.Peripheral

actual suspend fun Peripheral.requestMtuIfAvailable() {
    // not supported on iOS
}

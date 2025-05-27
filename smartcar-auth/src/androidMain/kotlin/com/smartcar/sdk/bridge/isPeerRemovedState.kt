package com.smartcar.sdk.bridge

import com.juul.kable.State

// this is an iOS-specific state
actual fun State.isPeerRemovedState(): Boolean = false

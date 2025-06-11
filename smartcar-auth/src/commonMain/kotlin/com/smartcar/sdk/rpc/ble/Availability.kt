package com.smartcar.sdk.rpc.ble

enum class Availability {
    Available,
    PermissionDenied,
    BluetoothOff,
    LocationServicesDisabled, // only for Android 30 or below
    Unknown,
}

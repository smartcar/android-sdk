package com.smartcar.sdk.bridge

import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.smartcar.sdk.activity.OAuthCaptureActivity
import com.smartcar.sdk.activity.awaitActivityResult
import com.smartcar.sdk.activity.awaitMultiplePermissionsResult
import com.smartcar.sdk.rpc.ble.Availability

class ContextBridgeImpl(
    private val activity: ComponentActivity
): ContextBridge {
    override suspend fun startOAuthCapture(
        authorizeUrl: String,
        interceptPrefix: String,
        headerConfig: String
    ): String? {
        val intent = Intent(activity, OAuthCaptureActivity::class.java).apply {
            putExtra("authorize_url", authorizeUrl)
            putExtra("intercept_prefix", interceptPrefix)
            putExtra("header_config", headerConfig)
        }

        // Await the result using our extension function
        val result = activity.awaitActivityResult(intent)
        val returnUri = result.data?.getStringExtra("return_uri")

        return returnUri
    }

    override suspend fun getBLEAvailability(): Availability {
        if (!hasBluetoothPermissions()) {
            return Availability.PermissionDenied
        }
        if (!isBluetoothOn()) {
            return Availability.BluetoothOff
        }
        if (SDK_INT < S && !areLocationServicesEnabled()) {
            return Availability.LocationServicesDisabled
        }
        return Availability.Available
    }

    private suspend fun hasBluetoothPermissions(): Boolean {
        // Define the required permissions based on SDK version
        val requiredPermissions = mutableListOf<String>().apply {
            if (SDK_INT >= S) {
                add(android.Manifest.permission.BLUETOOTH_SCAN)
                add(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                add(android.Manifest.permission.BLUETOOTH)
                add(android.Manifest.permission.ACCESS_FINE_LOCATION)
                add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

        // Filter out the permissions that are not yet granted
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isEmpty()) return true

        // Request permissions if any are missing
        return activity.awaitMultiplePermissionsResult(
            permissionsToRequest.toTypedArray()).all { it.value }
    }

    private fun isBluetoothOn(): Boolean {
        val manager = ContextCompat.getSystemService(activity, BluetoothManager::class.java)
        val adapter = manager?.adapter
        return adapter?.isEnabled ?: false
    }

    private fun areLocationServicesEnabled(): Boolean {
        val manager = ContextCompat.getSystemService(activity, LocationManager::class.java)
        return manager?.let { LocationManagerCompat.isLocationEnabled(it) } ?: false
    }

    override fun openSystemPage(page: String) {
        when (page) {
            "bluetooth_settings" ->
                activity.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
            "location_settings" ->
                activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
}

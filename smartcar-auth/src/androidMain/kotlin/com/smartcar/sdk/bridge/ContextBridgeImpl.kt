package com.smartcar.sdk.bridge

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.smartcar.sdk.activity.OAuthCaptureActivity
import com.smartcar.sdk.activity.awaitActivityResult
import com.smartcar.sdk.activity.awaitMultiplePermissionsResult

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

    override suspend fun checkBLEPermissions(): Boolean {
        // Define the required permissions based on SDK version
        val requiredPermissions = mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

        if (permissionsToRequest.isEmpty()) return true;

        // Request permissions if any are missing
        return activity.awaitMultiplePermissionsResult(
            permissionsToRequest.toTypedArray()).all { it.value }
    }
}

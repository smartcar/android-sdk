package com.smartcar.sdk.util

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume


private val keyIncrement = AtomicInteger(0)

/**
 * Suspends the current coroutine and launches an activity with the provided [intent],
 * then resumes with the resulting [ActivityResult] once the activity completes.
 *
 * This function registers an activity result launcher using a unique key, initiates the
 * activity launch, and waits for the result asynchronously. If the coroutine is cancelled
 * before a result is received, the launcher is unregistered to prevent memory leaks.
 *
 * @receiver [ComponentActivity] the activity from which to launch the intent.
 * @param intent The [Intent] to launch the target activity.
 * @return The [ActivityResult] returned by the launched activity.
 */
suspend fun ComponentActivity.awaitActivityResult(intent: Intent): ActivityResult {
    return suspendCancellableCoroutine { continuation ->
        // Register the activity launcher
        val launcher = activityResultRegistry.register(
            "activityResult_${keyIncrement.getAndIncrement()}",
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // When the result returns, resume the coroutine
            if (!continuation.isCompleted) {
                continuation.resume(result)
            }
        }

        //  Launch the activity
        launcher.launch(intent)

        // Handle coroutine cancellation
        continuation.invokeOnCancellation {
            launcher.unregister()
        }
    }
}

/**
 * Suspends the coroutine and requests multiple permissions.
 *
 * @param permissions Array of permissions to request
 * @return Map of permissions and their grant results
 */
suspend fun ComponentActivity.awaitMultiplePermissionsResult(permissions: Array<String>): Map<String, Boolean> {
    return suspendCancellableCoroutine { continuation ->
        // Register the permissions request launcher
        val launcher = activityResultRegistry.register(
            "permissionResult_${keyIncrement.getAndIncrement()}",
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (!continuation.isCompleted) {
                continuation.resume(results)
            }
        }

        // Launch the permission requests
        launcher.launch(permissions)

        // Handle coroutine cancellation
        continuation.invokeOnCancellation {
            launcher.unregister()
        }
    }
}

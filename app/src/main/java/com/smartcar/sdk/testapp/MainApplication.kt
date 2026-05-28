package com.smartcar.sdk.testapp

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val processName = getProcessName()
        if (processName != packageName) {
            // Simulate what Firebase does: crash if initialized in a secondary process
            // without a process check. This reproduces the EV Energy bug where
            // MainApplication.onCreate() runs in the :smartcar_oauth process and throws.
            // throw IllegalStateException(
            //     "Default FirebaseApp is not initialized in this process " +
            //     "$processName. Make sure to call FirebaseApp.initializeApp(Context) first."
            // )
        }
    }
}

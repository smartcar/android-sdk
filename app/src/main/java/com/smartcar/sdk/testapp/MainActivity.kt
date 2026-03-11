package com.smartcar.sdk.testapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import com.smartcar.sdk.SmartcarAuth
import com.smartcar.sdk.SmartcarCallback
import com.smartcar.sdk.SmartcarResponse

class MainActivity : ComponentActivity() {
    private companion object {
        const val PROD_AUTH_URL = "https://connect.smartcar.com/oauth/authorize"
        const val QA_AUTH_URL = "https://connect.qa.smartcar.com/oauth/authorize"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clientIdInput = findViewById<EditText>(R.id.clientIdInput)
        val redirectUriInput = findViewById<EditText>(R.id.redirectUriInput)
        val environmentGroup = findViewById<RadioGroup>(R.id.environmentGroup)
        val modeGroup = findViewById<RadioGroup>(R.id.modeGroup)

        clientIdInput.setText("bbccf263-6fa2-4f28-ac25-102950ef00e0")
        redirectUriInput.setText("https://smartcar.com/")

        findViewById<Button>(R.id.connectButton).setOnClickListener {
            val clientId = clientIdInput.text.toString().trim()
            val redirectUri = redirectUriInput.text.toString().trim()

            if (clientId.isEmpty() || redirectUri.isEmpty()) {
                Toast.makeText(this, R.string.missing_fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isTestMode = modeGroup.checkedRadioButtonId == R.id.modeTest

            val smartcarAuth = SmartcarAuth(
                clientId,
                redirectUri,
                arrayOf("read_vehicle_info"),
                isTestMode,
                object : SmartcarCallback {
                    override fun handleResponse(smartcarResponse: SmartcarResponse?) {
                        val code = smartcarResponse?.code ?: "(missing)"
                        val userId = smartcarResponse?.userId ?: "(missing)"
                        val message = "code=$code, user_id=$userId"

                        Log.d("SmartcarTestApp", "Response: $message")
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
            )

            val baseAuthorizationUrl = if (environmentGroup.checkedRadioButtonId == R.id.environmentQa) {
                QA_AUTH_URL
            } else {
                PROD_AUTH_URL
            }

            val builtUri = smartcarAuth.authUrlBuilder().build().toUri()
            val selectedBaseUri = baseAuthorizationUrl.toUri()
            val selectedAuthUrl = builtUri.buildUpon()
                .scheme(selectedBaseUri.scheme)
                .authority(selectedBaseUri.authority)
                .path(selectedBaseUri.path)
                .build()
                .toString()
            // log the selected auth url for debugging purposes
            Log.d("SmartcarTestApp", "Selected Auth URL: $selectedAuthUrl")
            smartcarAuth.launchAuthFlow(applicationContext, selectedAuthUrl)
        }
    }
}

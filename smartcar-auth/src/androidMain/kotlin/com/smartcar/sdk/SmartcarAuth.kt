package com.smartcar.sdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import com.smartcar.sdk.activity.ConnectActivity
import androidx.core.net.toUri

/**
 * Copyright (c) 2017-present, Smartcar, Inc. All rights reserved.
 *
 * You are hereby granted a limited, non-exclusive, worldwide, royalty-free
 * license to use, copy, modify, and distribute this software in source code or
 * binary form, for the limited purpose of this software's use in connection
 * with the web services and APIs provided by Smartcar.
 *
 * As with any software that integrates with the Smartcar platform, your use of
 * this software is subject to the Smartcar Developer Agreement. This copyright
 * notice shall be included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
class SmartcarAuth {

    companion object {
        private const val BASE_AUTHORIZATION_URL = "https://connect.smartcar.com/oauth/authorize"
        private const val AUTHORIZATION_HOST = "connect.smartcar.com"

        private lateinit var clientId: String
        private lateinit var redirectUri: String
        private lateinit var scope: Array<String>
        private var testMode: Boolean = false
        private lateinit var callback: SmartcarCallback

        /**
         * Receives the response from Connect and sends it back to the calling function
         * via the callback method. The code is packed in a Bundle with the key "code".
         *
         * @param uri The response data as a Uri
         */
        fun receiveResponse(uri: Uri?) {
            if (uri != null && uri.toString().startsWith(redirectUri)) {
                val queryState = uri.getQueryParameter("state")
                val queryErrorDescription = uri.getQueryParameter("error_description")
                val queryCode = uri.getQueryParameter("code")
                val queryError = uri.getQueryParameter("error")
                val queryVin = uri.getQueryParameter("vin")
                val queryVirtualKeyUrl = uri.getQueryParameter("virtual_key_url")

                val receivedCode = queryCode != null
                val receivedError = queryError != null && queryVin == null
                val receivedErrorWithVehicle = queryError != null && queryVin != null

                val responseBuilder = SmartcarResponse.Builder()

                if (receivedCode) {

                    val smartcarResponse = responseBuilder
                            .code(queryCode)
                            .errorDescription(queryErrorDescription)
                            .state(queryState)
                            .virtualKeyUrl(queryVirtualKeyUrl)
                            .build()
                    callback.handleResponse(smartcarResponse)

                } else if (receivedError) {

                    val smartcarResponse = responseBuilder
                            .error(queryError)
                            .errorDescription(queryErrorDescription)
                            .state(queryState)
                            .build()
                    callback.handleResponse(smartcarResponse)

                } else if (receivedErrorWithVehicle) {

                    val make = uri.getQueryParameter("make")
                    val responseVehicle = VehicleInfo.Builder()
                            .vin(queryVin)
                            .make(make)
                            .build()

                    val smartcarResponse = responseBuilder
                            .error(queryError)
                            .errorDescription(queryErrorDescription)
                            .state(queryState)
                            .vehicleInfo(responseVehicle)
                            .build()
                    callback.handleResponse(smartcarResponse)

                } else {

                    val smartcarResponse = responseBuilder
                            .errorDescription("Unable to fetch code. Please try again")
                            .state(queryState)
                            .build()
                    callback.handleResponse(smartcarResponse)
                }
            }
        }
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authorization scopes
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    constructor(clientId: String, redirectUri: String, scope: Array<String>, callback: SmartcarCallback) : this(clientId, redirectUri, scope, false, callback)

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authorization scopes
     * @param testMode    Set to true to run Smartcar Connect in test mode
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    constructor(clientId: String, redirectUri: String, scope: Array<String>, testMode: Boolean, callback: SmartcarCallback) {
        Companion.clientId = clientId
        Companion.redirectUri = redirectUri
        Companion.scope = scope
        Companion.testMode = testMode
        Companion.callback = callback
    }

    /**
     * Build a Smartcar Connect authorization URL.
     *
     * Use the built string with {@link SmartcarAuth#launchAuthFlow(Context, String)} or {@link SmartcarAuth#addClickHandler(Context, View, String)}.
     */
    fun authUrlBuilder(): AuthUrlBuilder {
        return AuthUrlBuilder()
    }

    /**
     * A builder used for generating Smartcar Connect authorization URLs.
     */
    inner class AuthUrlBuilder {
        private val uriBuilder = BASE_AUTHORIZATION_URL.toUri().buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("sdk_platform", "android")
                .appendQueryParameter("sdk_version", BuildConfig.VERSION_NAME)
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("mode", if (testMode) "test" else "live")
                .appendQueryParameter("scope", TextUtils.join(" ", scope))

        /**
         * Set an optional state parameter.
         *
         * @param state An optional value included on the {@link SmartcarResponse} object returned
         *              to the {@link SmartcarCallback}
         * @return a reference to this object
         */
        fun setState(state: String): AuthUrlBuilder {
            if (state != "") {
                uriBuilder.appendQueryParameter("state", state)
            }
            return this
        }

        /**
         * Force display of the grant approval dialog in Smartcar Connect.
         *
         * Defaults to false and will only display the approval dialog if the user has not
         * previously approved the scope. Set this to true to ensure the approval dialog is always
         * shown to the user even if they have previously approved the same scope.
         *
         * @param forcePrompt Set to true to ensure the grant approval dialog is always shown
         * @return a reference to this object
         */
        fun setForcePrompt(forcePrompt: Boolean): AuthUrlBuilder {
            uriBuilder.appendQueryParameter("approval_prompt", if (forcePrompt) "force" else "auto")
            return this
        }

        /**
         * Bypass the brand selector screen to a specified make.
         *
         * See the available makes on the <a href="https://smartcar.com/docs/api#connect-direct">Smartcar API Reference</a>.
         *
         * @see <a href="https://smartcar.com/docs/api#connect-direct">Smartcar Connect Direct</a>
         * @param make The selected make
         * @return a reference to this object
         */
        fun setMakeBypass(make: String): AuthUrlBuilder {
            uriBuilder.appendQueryParameter("make", make)
            return this
        }

        /**
         * Ensure the user only authorizes a single vehicle.
         *
         * A user's connected service account can be connected to multiple vehicles. Setting this
         * parameter to true forces the user to select only a single vehicle.
         *
         * @see <a href="https://smartcar.com/docs/api#connect-match">Smartcar Connect Match</a>
         * @param singleSelect Set to true to ensure only a single vehicle is authorized
         * @return a reference to this object
         */
        fun setSingleSelect(singleSelect: Boolean): AuthUrlBuilder {
            uriBuilder.appendQueryParameter("single_select", singleSelect.toString())
            return this
        }

        /**
         * Specify the vin a user can authorize in Smartcar Connect.
         *
         * When the {@link AuthUrlBuilder#setSingleSelect(boolean)} is set to true, this parameter
         * can be used to ensure that Smartcar Connect will allow the user to authorize only the
         * vehicle with a specific VIN.
         *
         * @see <a href="https://smartcar.com/docs/api#connect-match">Smartcar Connect Match</a>
         * @param vin The specific VIN to authorize
         * @return a reference to this object
         */
        fun setSingleSelectVin(vin: String): AuthUrlBuilder {
            uriBuilder.appendQueryParameter("single_select_vin", vin)
            return this
        }

        /**
         * Enable early access features.
         *
         * @param flags List of feature flags that your application has early access to.
         * @return a reference to this object
         */
        fun setFlags(flags: Array<String>): AuthUrlBuilder {
            uriBuilder.appendQueryParameter("flags", TextUtils.join(" ", flags))
            return this
        }

        /**
         * Specify a unique identifier for the vehicle owner to track their progress through
         * Smartcar Connect.
         *
         * @param user An optional unique identifier for a vehicle owner. This identifier is used to aggregate
         * analytics across Connect sessions for each vehicle owner.
         * @return a reference to this object
         */
        fun setUser(user: String): AuthUrlBuilder {
            uriBuilder.appendQueryParameter("user", user)
            return this
        }

        /**
         * Build a Smartcar Connect authorization url.
         *
         * @return A built url which can be used in {@link SmartcarAuth#launchAuthFlow(Context, String)} or {@link SmartcarAuth#addClickHandler(Context, View, String)}
         */
        fun build(): String {
            return uriBuilder.build().toString()
        }
    }

    /**
     * Attaches a click listener to a view to launch Smartcar Connect.
     *
     * @param context The client application's context
     * @param view The view to attach the click listener
     */
    fun addClickHandler(context: Context, view: View) {
        addClickHandler(context, view, AuthUrlBuilder().build())
    }

    /**
     * Attaches a click listener to a view to launch Smartcar Connect.
     *
     * @param context The client application's context
     * @param view The view to attach the click listener
     * @param authUrl Use {@link AuthUrlBuilder} to generate the authorization url
     */
    fun addClickHandler(context: Context, view: View, authUrl: String) {
        val listener = View.OnClickListener { launchAuthFlow(context, authUrl) }
        view.setOnClickListener(listener)
    }

    /**
     * Starts the launch of Smartcar Connect. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     */
    fun launchAuthFlow(context: Context) {
        launchAuthFlow(context, AuthUrlBuilder().build())
    }

    /**
     * Starts the launch of Smartcar Connect. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     * @param authUrl Use {@link AuthUrlBuilder} to generate the authorization url
     */
    fun launchAuthFlow(context: Context, authUrl: String) {
        val intent = Intent(context, ConnectActivity::class.java)
        intent.putExtra("authorize_url", authUrl)
        intent.putExtra("intercept_prefix", redirectUri)
        if (authUrl.startsWith(BASE_AUTHORIZATION_URL))
            intent.putExtra("allowed_host", AUTHORIZATION_HOST)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

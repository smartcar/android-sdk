package com.smartcar.sdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.smartcar.sdk.activity.ConnectActivity
import com.smartcar.sdk.rpc.oauth.CompleteRequest
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

    internal companion object {
        private const val BASE_AUTHORIZATION_URL = "https://connect.smartcar.com/oauth/authorize"
        private val VALID_RESPONSE_TYPES = arrayOf("code", "none")

        private lateinit var applicationId: String
        private var redirectUri: String? = null
        private var scope: Array<String> = emptyArray()
        private var testMode: Boolean = false
        private lateinit var callback: SmartcarCallback
        private var responseType: String = "code"

        /**
         * Turns raw result fields into a [SmartcarResponse] and hands it to [callback]. Shared
         * by the redirect path ([receiveResponse]) and the direct RPC path
         * ([receiveDirectResult]) so both produce identical responses for equivalent input.
         */
        private fun buildAndDeliverResponse(
            code: String?,
            userId: String?,
            externalId: String?,
            error: String?,
            errorDescription: String?,
            state: String?,
            vin: String?,
            make: String?,
            virtualKeyUrl: String?
        ) {
            /**
             * If the process was killed while Connect was open, the callback will not have been
             * re-initialized when Android recreates the activity. We return silently to avoid a
             * crash, but the auth result is lost and the user must restart the flow.
             * Known limitation of the SmartcarCallback API — will be addressed in the next
             * major version by migrating to the ContextBridge/Activity Results pattern.
             */
            if (!::callback.isInitialized) return

            val receivedCode = code != null
            val receivedError = error != null && vin == null
            val receivedErrorWithVehicle = error != null && vin != null
            // response_type=none succeeds without a code; only treat "no code, no
            // error" as success when the flow was configured for it, so a broken
            // code-flow redirect still surfaces the "unable to fetch code" error below
            val receivedSuccessWithoutCode = responseType == "none" &&
                    !receivedCode && !receivedError && !receivedErrorWithVehicle

            val responseBuilder = SmartcarResponse.Builder()

            if (receivedCode || receivedSuccessWithoutCode) {
            // for now, userId is returned alongside code
            // in the future, userId may be returned without code, so we want to make sure to include it in the response if it's present in a success auth response
                val smartcarResponse = responseBuilder
                        .code(code)
                        .userId(userId)
                        .externalId(externalId)
                        .errorDescription(errorDescription)
                        .state(state)
                        .virtualKeyUrl(virtualKeyUrl)
                        .build()
                callback.handleResponse(smartcarResponse)

            } else if (receivedError) {

                val smartcarResponse = responseBuilder
                        .error(error)
                        .errorDescription(errorDescription)
                        .state(state)
                        .externalId(externalId)
                        .build()
                callback.handleResponse(smartcarResponse)

            } else if (receivedErrorWithVehicle) {

                val responseVehicle = VehicleInfo.Builder()
                        .vin(vin)
                        .make(make)
                        .build()

                val smartcarResponse = responseBuilder
                        .error(error)
                        .errorDescription(errorDescription)
                        .state(state)
                        .vehicleInfo(responseVehicle)
                        .externalId(externalId)
                        .build()
                callback.handleResponse(smartcarResponse)

            } else {

                val smartcarResponse = responseBuilder
                        .errorDescription("Unable to fetch code. Please try again")
                        .state(state)
                        .build()
                callback.handleResponse(smartcarResponse)
            }
        }

        /**
         * Receives the response from Connect and sends it back to the calling function
         * via the callback method. The code is packed in a Bundle with the key "code".
         *
         * @param uri The response data as a Uri
         */
        fun receiveResponse(uri: Uri?, redirectUri: String) {
            if (uri != null && uri.toString().startsWith(redirectUri)) {
                buildAndDeliverResponse(
                    code = uri.getQueryParameter("code"),
                    userId = uri.getQueryParameter("user_id"),
                    externalId = uri.getQueryParameter("external_id"),
                    error = uri.getQueryParameter("error"),
                    errorDescription = uri.getQueryParameter("error_description"),
                    state = uri.getQueryParameter("state"),
                    vin = uri.getQueryParameter("vin"),
                    make = uri.getQueryParameter("make"),
                    virtualKeyUrl = uri.getQueryParameter("virtual_key_url")
                )
            }
        }

        /**
         * Receives the Connect result directly over the RPC bridge (no redirect involved) and
         * sends it back to the calling function via the callback method.
         *
         * @param params The result fields Connect would otherwise have encoded into a redirect URI
         */
        fun receiveDirectResult(params: CompleteRequest.CompleteParams) {
            buildAndDeliverResponse(
                code = params.code,
                userId = params.userId,
                externalId = params.externalId,
                error = params.error,
                errorDescription = params.errorDescription,
                state = params.state,
                vin = params.vin,
                make = params.make,
                virtualKeyUrl = params.virtualKeyUrl
            )
        }
    }

    /**
     * Constructs an instance with the given parameters.
     *
        * @param applicationId The application's ID
        * @param redirectUri The application's redirect URI
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    constructor(applicationId: String, redirectUri: String, callback: SmartcarCallback) : this(applicationId, redirectUri, emptyArray(), false, callback)

    /**
     * Constructs an instance with the given parameters.
     *
        * @param applicationId The application's ID
        * @param redirectUri The application's redirect URI
     * @param testMode    Set to true to run Smartcar Connect in test mode
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    constructor(applicationId: String, redirectUri: String, testMode: Boolean, callback: SmartcarCallback) : this(applicationId, redirectUri, emptyArray(), testMode, callback)

    /**
     * Constructs an instance with the given parameters.
     *
        * @param applicationId The application's ID
        * @param redirectUri The application's redirect URI
     * @param scope       An array of authorization scopes
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    constructor(applicationId: String, redirectUri: String, scope: Array<String>, callback: SmartcarCallback) : this(applicationId, redirectUri, scope, false, callback)

    /**
     * Constructs an instance with the given parameters.
     *
     * @param applicationId The application's ID
    * @param redirectUri The application's redirect URI
     * @param scope       An array of authorization scopes
     * @param testMode    Set to true to run Smartcar Connect in test mode
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    constructor(applicationId: String, redirectUri: String, scope: Array<String>, testMode: Boolean, callback: SmartcarCallback) :
            this(applicationId, redirectUri, scope, testMode, "code", callback)

    /**
     * Constructs an instance with the given parameters.
     *
     * @param applicationId The application's ID
     * @param redirectUri The application's redirect URI. Required unless [responseType] is
     *                     "none". When provided together with `responseType = "none"`, Connect
     *                     still redirects here, but the redirect omits `code`. When omitted
     *                     (`null`) with `responseType = "none"`, there is no redirect at all and
     *                     [callback] will never be invoked — correlate via `externalId` instead.
     * @param scope       An array of authorization scopes
     * @param testMode    Set to true to run Smartcar Connect in test mode
     * @param responseType OAuth response type. Use "none" for redirect-less (no authorization
     *                      code exchange) M2M flows; defaults to "code". Must be one of "code"
     *                      or "none".
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    constructor(applicationId: String, redirectUri: String?, scope: Array<String>, testMode: Boolean, responseType: String, callback: SmartcarCallback) {
        if (!VALID_RESPONSE_TYPES.contains(responseType)) {
            throw IllegalArgumentException(
                "The \"responseType\" parameter must be one of: ${VALID_RESPONSE_TYPES.joinToString(", ")}"
            )
        }
        if (responseType == "code" && redirectUri.isNullOrBlank()) {
            throw IllegalArgumentException("\"redirectUri\" is required when responseType is \"code\"")
        }
        Companion.applicationId = applicationId
        Companion.redirectUri = redirectUri
        Companion.scope = scope
        Companion.testMode = testMode
        Companion.responseType = responseType
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
                .appendQueryParameter("response_type", responseType)
                .appendQueryParameter("application_id", applicationId)
                .apply {
                    if (redirectUri != null) {
                        appendQueryParameter("redirect_uri", redirectUri)
                    }
                }
                .appendQueryParameter("mode", if (testMode) "test" else "live")
                .apply {
                    if (scope.isNotEmpty()) {
                        appendQueryParameter("scope", TextUtils.join(" ", scope))
                    }
                }

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
         * Specify an external identifier that will be echoed back on the
         * {@link SmartcarResponse} object passed to {@link SmartcarCallback}. Primarily
         * useful with `responseType = "none"` flows, where no `code` is
         * returned, to correlate the connection.
         *
         * @param externalId An optional external identifier passed through to Connect
         * @return a reference to this object
         */
        fun setExternalId(externalId: String): AuthUrlBuilder {
            uriBuilder.appendQueryParameter("external_id", externalId)
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
        if (redirectUri == null) {
            Log.w("SmartcarAuth", "launchAuthFlow was called without a redirectUri; " +
                    "SmartcarCallback.handleResponse will not be invoked because Connect will " +
                    "not redirect back into the app. Use externalId to correlate the connection " +
                    "via webhook or the /connections endpoint.")
        }

        // Append sdk version query parameters if they don't already exist

        val uri = authUrl.toUri()
        val uriBuilder = uri.buildUpon()

        // Check if sdk_platform parameter already exists
        if (uri.getQueryParameter("sdk_platform") == null) {
            uriBuilder.appendQueryParameter("sdk_platform", "android")
        }

        // Check if sdk_version parameter already exists
        if (uri.getQueryParameter("sdk_version") == null) {
            uriBuilder.appendQueryParameter("sdk_version", BuildConfig.VERSION_NAME)
        }

        val newAuthUrl = uriBuilder.build()

        val intent = Intent(context, ConnectActivity::class.java)
        intent.putExtra("authorize_url", newAuthUrl.toString())
        intent.putExtra("intercept_prefix", redirectUri)
        intent.putExtra("allowed_host", newAuthUrl.host)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

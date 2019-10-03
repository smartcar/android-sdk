/**
 * Copyright (c) 2017-present, Smartcar, Inc. All rights reserved.

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

package com.smartcar.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import okhttp3.HttpUrl;

/**
 * Main class that provides SDK access methods.
 */
public class SmartcarAuth {

    private static final String BASE_AUTHORIZATION_URL = "https://connect.smartcar.com/oauth/authorize";

    protected static String clientId;
    protected static String redirectUri;
    protected static String[] scope;
    protected static Boolean testMode;
    private static SmartcarCallback callback;

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authorization scopes
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    public SmartcarAuth(String clientId, String redirectUri, String[] scope,
                        SmartcarCallback callback) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.testMode = false;
        this.callback = callback;
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authorization scopes
     * @param testMode    Set to true to run Smartcar Connect in test mode
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    public SmartcarAuth(String clientId, String redirectUri, String[] scope, boolean testMode,
                        SmartcarCallback callback) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.testMode = testMode;
        this.callback = callback;
    }

    /**
     * A builder used for generating Smartcar Connect authorization URLs.
     */
    public class AuthUrlBuilder {
        private HttpUrl.Builder urlBuilder;

        public AuthUrlBuilder() {
            urlBuilder = HttpUrl.parse(BASE_AUTHORIZATION_URL).newBuilder()
                    .addQueryParameter("response_type", "code")
                    .addQueryParameter("client_id", clientId)
                    .addQueryParameter("redirect_uri", redirectUri)
                    .addQueryParameter("mode", testMode ? "test" : "live")
                    .addQueryParameter("scope", TextUtils.join(" ", scope));
        }

        /**
         * Set an optional state parameter.
         *
         * @param state An optional value included on the {@link SmartcarResponse} object returned
         *              to the {@link SmartcarCallback}
         * @return a reference to this object
         */
        public AuthUrlBuilder setState(String state) {
            if (!state.equals("")) {
                urlBuilder.addQueryParameter("state", state);
            }
            return this;
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
        public AuthUrlBuilder setForcePrompt(boolean forcePrompt) {
            urlBuilder.addQueryParameter("approval_prompt", forcePrompt ? "force" : "auto");
            return this;
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
        public AuthUrlBuilder setMakeBypass(String make) {
            urlBuilder.addQueryParameter("make", make);
            return this;
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
        public AuthUrlBuilder setSingleSelect(boolean singleSelect) {
            urlBuilder.addQueryParameter("single_select", Boolean.toString(singleSelect));
            return this;
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
        public AuthUrlBuilder setSingleSelectVin(String vin) {
            urlBuilder.addQueryParameter("single_select_vin", vin);
            return this;
        }

        /**
         * Build a Smartcar Connect authorization url.
         *
         * @return A built url which can be used in {@link SmartcarAuth#launchAuthFlow(Context, String)} or {@link SmartcarAuth#addClickHandler(Context, View, String)}
         */
        public String build() {
            return urlBuilder.build().toString();
        }
    }

    /**
     * Attaches a click listener to a view to launch Smartcar Connect.
     *
     * @param context The client application's context
     * @param view The view to attach the click listener
     */
    public void addClickHandler(final Context context, final View view) {
        addClickHandler(context, view, (new AuthUrlBuilder()).build());
    }

    /**
     * Attaches a click listener to a view to launch Smartcar Connect.
     *
     * @param context The client application's context
     * @param view The view to attach the click listener
     * @param authUrl Use {@link AuthUrlBuilder} to generate the authorization url
     */
    public void addClickHandler(final Context context, final View view, final String authUrl) {
        final View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                launchAuthFlow(context, authUrl);
            }
        };
        view.setOnClickListener(listener);
    }

    /**
     * Starts the launch of Smartcar Connect. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     */
    public void launchAuthFlow(final Context context) {
        launchAuthFlow(context, (new AuthUrlBuilder()).build());
    }

    /**
     * Starts the launch of Smartcar Connect. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     * @param authUrl Use {@link AuthUrlBuilder} to generate the authorization url
     */
    public void launchAuthFlow(final Context context, final String authUrl) {
        Intent intent = new Intent(context, com.smartcar.sdk.WebViewActivity.class);
        intent.putExtra("URI", authUrl);
        // The new activity (web view) will not be in the history stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Receives the response from Connect and sends it back to the calling function
     * via the callback method. The code is packed in a Bundle with the key "code".
     *
     * @param uri The response data as a Uri
     */
    protected static void receiveResponse(Uri uri) {
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            String queryState = uri.getQueryParameter("state");
            String queryErrorDescription = uri.getQueryParameter("error_description");
            String queryCode = uri.getQueryParameter("code");
            String queryError = uri.getQueryParameter("error");
            String queryVin = uri.getQueryParameter("vin");

            boolean receivedCode = queryCode != null;
            boolean receivedError = queryError != null && queryVin == null;
            boolean receivedErrorWithVehicle = queryError != null && queryVin != null;

            SmartcarResponse.Builder responseBuilder = new SmartcarResponse.Builder();

            if (receivedCode) {
                SmartcarResponse smartcarResponse = responseBuilder
                        .code(queryCode)
                        .errorDescription(queryErrorDescription)
                        .state(queryState)
                        .build();
                callback.handleResponse(smartcarResponse);
            }

            else if (receivedError) {
                SmartcarResponse smartcarResponse = responseBuilder
                        .error(queryError)
                        .errorDescription(queryErrorDescription)
                        .state(queryState)
                        .build();
                callback.handleResponse(smartcarResponse);
            }

            else if (receivedErrorWithVehicle) {
                String make = uri.getQueryParameter("make");
                String model = uri.getQueryParameter("model");
                int year = Integer.parseInt(uri.getQueryParameter("year"));
                VehicleInfo responseVehicle = new VehicleInfo.Builder()
                    .vin(queryVin)
                    .make(make)
                    .model(model)
                    .year(year)
                    .build();

                SmartcarResponse smartcarResponse = responseBuilder
                        .error(queryError)
                        .errorDescription(queryErrorDescription)
                        .state(queryState)
                        .vehicleInfo(responseVehicle)
                        .build();
                callback.handleResponse(smartcarResponse);
            }

            else {
                SmartcarResponse smartcarResponse = responseBuilder
                        .errorDescription("Unable to fetch code. Please try again")
                        .state(queryState)
                        .build();
                callback.handleResponse(smartcarResponse);
            }

        }
    }
}

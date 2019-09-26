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
import android.net.Uri;
import android.view.View;
import okhttp3.HttpUrl;

/**
 * Main class that provides SDK access methods.
 */
public class SmartcarAuth {
    protected static SmartcarAuthRequest smartcarAuthRequest;
    private static SmartcarCallback callback;
    private static final String URL_AUTHORIZE = "https://connect.smartcar.com/oauth/authorize";

    public String urlAuthorize = SmartcarAuth.URL_AUTHORIZE;

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       A space-separated list of authorization scopes
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    public SmartcarAuth(String clientId, String redirectUri, String scope,
                        SmartcarCallback callback) {
        smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope);
        this.callback = callback;
    }

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
        String scopeStr = Helper.arrayToString(scope);
        smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr);
        this.callback = callback;
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authorization scopes
     * @param testMode    Whether to display the MOCK vehicle brand or not
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    public SmartcarAuth(String clientId, String redirectUri, String scope, boolean testMode,
                        SmartcarCallback callback) {
        smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, testMode);
        this.callback = callback;
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authorization scopes
     * @param testMode    Whether to display the MOCK vehicle brand or not
     * @param callback    Handler to a Callback for receiving the Smartcar Connect response
     */
    public SmartcarAuth(String clientId, String redirectUri, String[] scope, boolean testMode,
                        SmartcarCallback callback) {
        String scopeStr = Helper.arrayToString(scope);
        smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, testMode);
        this.callback = callback;
    }

    /**
     * A class that creates a custom AuthUrlBuilder object, used
     * for generating authentication URLs.
     */
    public class AuthUrlBuilder {
        private HttpUrl.Builder urlBuilder;

        public AuthUrlBuilder() {
            urlBuilder = HttpUrl.parse(urlAuthorize).newBuilder()
                    .addQueryParameter("response_type", "code")
                    .addQueryParameter("client_id", SmartcarAuth.smartcarAuthRequest.getClientId())
                    .addQueryParameter("redirect_uri", SmartcarAuth.smartcarAuthRequest.getRedirectURI())
                    .addQueryParameter("mode", SmartcarAuth.smartcarAuthRequest.getTestMode() ? "test" : "live")
                    .addQueryParameter("scope", SmartcarAuth.smartcarAuthRequest.getScope());
        }

        public AuthUrlBuilder setState(String state) {
            if (!state.equals("")) {
                urlBuilder.addQueryParameter("state", state);
            }
            return this;
        }

        public AuthUrlBuilder setApprovalPrompt(boolean approvalPrompt) {
            urlBuilder.addQueryParameter("approval_prompt", approvalPrompt ? "force" : "auto");
            return this;
        }

        public AuthUrlBuilder setMakeBypass(String make) {
            urlBuilder.addQueryParameter("make", make);
            return this;
        }

        public AuthUrlBuilder setSingleSelect(boolean singleSelect) {
            urlBuilder.addQueryParameter("single_select", Boolean.toString(singleSelect));
            return this;
        }

        public AuthUrlBuilder setSingleSelectVin(String vin) {
            urlBuilder.addQueryParameter("single_select_vin", vin);
            return this;
        }

        public String build() {
            return urlBuilder.build().toString();
        }
    }

    /**
     * Generates a click event listener for managing Smartcar Connect, and attaches
     *  it to the input View.
     *
     * @param context The client application's context
     * @param view The View to attach Smartcar Connect launch to
     */
    public void addClickHandler(final Context context, final View view) {
        final View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                launchAuthFlow(context);
            }
        };
        view.setOnClickListener(listener);
    }

    /**
     * Generates a click event listener for managing Smartcar Connect, and attaches
     *  it to the input View.
     *
     * @param context The client application's context
     * @param view The View to attach Smartcar Connect launch to
     * @param authUrl Use @AuthUrlBuilder to generate the authorization url
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
        Helper.startActivity(context, (new AuthUrlBuilder()).build());
    }

    /**
     * Starts the launch of Smartcar Connect. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     * @param authUrl Use @AuthUrlBuilder to generate the authorization url
     */
    public void launchAuthFlow(final Context context, final String authUrl) {
        Helper.startActivity(context, authUrl);
    }

    /**
     * Receives the response from Connect and sends it back to the calling function
     * via the callback method. The code is packed in a Bundle with the key "code".
     *
     * @param uri The response data as a Uri
     */
    protected static void receiveResponse(Uri uri) {
        if (uri != null && Helper.matchesRedirectUri(uri.toString())) {
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

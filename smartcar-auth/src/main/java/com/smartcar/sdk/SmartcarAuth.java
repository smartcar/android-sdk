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

/**
 * Main class that provides SDK access methods.
 */
public class SmartcarAuth {
    protected static SmartcarAuthRequest smartcarAuthRequest;
    private static SmartcarCallback callback;

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       A space-separated list of authentication scopes
     * @param callback    Handler to a Callback for receiving the authentication response
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
     * @param scope       An array of authentication scopes
     * @param callback    Handler to a Callback for receiving the authentication response
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
     * @param scope       An array of authentication scopes
     * @param testMode    Whether to display the MOCK vehicle brand or not
     * @param callback    Handler to a Callback for receiving the authentication response
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
     * @param scope       An array of authentication scopes
     * @param testMode    Whether to display the MOCK vehicle brand or not
     * @param callback    Handler to a Callback for receiving the authentication response
     */
    public SmartcarAuth(String clientId, String redirectUri, String[] scope, boolean testMode,
                        SmartcarCallback callback) {
        String scopeStr = Helper.arrayToString(scope);
        smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, testMode);
        this.callback = callback;
    }

    /**
     * A class that creates a custom AuthVehicleInfo object, which can be used
     * when generating an authentication URL.
     */
    public static class AuthVehicleInfo {
        private String make;

        /**
         * Assigns optional and required properties on the AuthVehicleInfo object.
         * 
         * @param builder the builder to obtain the properties from
         */
        private AuthVehicleInfo (Builder builder) {
            this.make = builder.make;
        }

        /**
         * Returns the make assigned to AuthVehicleInfo
         * 
         * @return the make of the vehicle
         */
        public String getMake() {
            return this.make;
        }

        /**
         * Builder class that allows for optional properties on AuthVehicleInfo 
         */
        public static class Builder {
            private String make;

            /**
             * Sets the make on the Builder. Including a make allows the user to bypass the car brand
             * selection screen.
             * 
             * @param make name of the make of a vehicle. For a list of supported makes, please see 
             * <a href="https://smartcar.com/docs/api#request-authorization">our API Reference</a>
             *
             * @return the builder with a `make` property added
             */
            public Builder setMake(String make) {
                this.make = make;
                return this;
            }

            /**
             * Instantiates a new AuthVehicleInfo object, which will also have any optional properties
             * that are already set on the Builder object that is calling this method. 
             *
             * @return a new instantiation of the AuthVehicleInfo class
             */
            public AuthVehicleInfo build() {
                return new AuthVehicleInfo(this);
            }
        }
    }

    /**
     * Generates the authorization request URI.
     *
     * @param state optional OAuth state to be returned on redirect
     * @param forcePrompt force permissions prompt to display on redirect (default: false)
     * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
     * `make` property causes the car brand selection screen to be bypassed.
     *
     * @return The authorization request URI
     */
   public String generateUrl(String state, boolean forcePrompt, AuthVehicleInfo authVehicleInfo) {

        String stateQuery = "";
        if (state != null) {
            stateQuery = "&state=" + state;
        }

        String approvalPrompt= ApprovalPrompt.auto.toString();
        if (forcePrompt) {
            approvalPrompt= ApprovalPrompt.force.toString();
        }

        String vehicleInfoQuery = "";
        if (authVehicleInfo != null) {
            String make = authVehicleInfo.getMake();

            if (make != null) {
                vehicleInfoQuery += "&make=" + make;
            }
        }

        String requestUri = "https://connect.smartcar.com/oauth/authorize?response_type="
                + smartcarAuthRequest.getResponseType().toString()
                + "&client_id=" + smartcarAuthRequest.getClientId()
                + "&redirect_uri=" + smartcarAuthRequest.getRedirectURI()
                + "&scope=" + smartcarAuthRequest.getScope()
                + stateQuery
                + "&approval_prompt=" + approvalPrompt
                + "&mode=" + (smartcarAuthRequest.getTestMode() ? "test" : "live")
                + vehicleInfoQuery;

        return requestUri;
    }

    /**
     * Generates the authorization URI.
     *
     * @param state optional OAuth state to be returned on redirect
     * @param forcePrompt force permissions prompt to display on redirect (default false)
     * @return The authorization request URI
     */
    public String generateUrl(String state, boolean forcePrompt) {
        return generateUrl(state, forcePrompt, null);
    }
    
    /**
     * Generates the authorization URI.
     *
     * @param state optional OAuth state to be returned on redirect
     * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
     * `make` property causes the car brand selection screen to be bypassed.
     * @return The authorization request URI
     */
    public String generateUrl(String state, AuthVehicleInfo authVehicleInfo) {
        return generateUrl(state, false, authVehicleInfo);
    }

    /**
     * Generates the authorization URI.
     *
     * @param forcePrompt force permissions prompt to display on redirect (default false)
     * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
     * `make` property causes the car brand selection screen to be bypassed.
     * @return The authorization request URI
     */
    public String generateUrl(boolean forcePrompt, AuthVehicleInfo authVehicleInfo) {
        return generateUrl(null, forcePrompt, authVehicleInfo);
    } 

    /**
     * Generates the authorization URI.
     *
     * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
     * `make` property causes the car brand selection screen to be bypassed.
     * 
     * @return The authorization request URI
     */
    public String generateUrl(AuthVehicleInfo authVehicleInfo) {
        return generateUrl(null, false, authVehicleInfo);
    }

    /**
     * Generates the authorization URI.
     *
     * @param forcePrompt force permissions prompt to display on redirect (default false)
     * @return The authorization request URI
     */
    public String generateUrl(boolean forcePrompt) {
        return generateUrl(null, forcePrompt, null);
    }


    /**
     * Generates the authorization URI.
     *
     * @param state optional OAuth state to be returned on redirect
     * @return The authorization request URI
     */
    public String generateUrl(String state) {
        return generateUrl(state, false, null);
    }

    /**
     * Generates the authorization URI
     *
     * @return The authorization request URI
     */
    public String generateUrl() {
        return generateUrl(null, false, null);
    }

    /**
     * Generates a click event listener for managing the Smartcar Auth flow, and attaches
     *  it to the input View.
     *
     * @param context The client application's context
     * @param view The View to attach the Smartcar Auth flow launch to
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
     * Generates a click event listener for managing the Smartcar Auth flow, and attaches
     *  it to the input View.
     *
     * @param context The client application's context
     * @param view The View to attach the Smartcar Auth flow launch to
     * @param state optional OAuth state to be returned on redirect
     * @param forcePrompt force permissions prompt to display on redirect (default: false)
     */
    public void addClickHandler(final Context context, final View view, final String state,
                                final boolean forcePrompt) {
        final View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                launchAuthFlow(context, state, forcePrompt);
            }
        };
        view.setOnClickListener(listener);
    }

    /**
     * Generates a click event listener for managing the Smartcar Auth flow, and attaches
     *  it to the input View.
     *
     * @param context The client application's context
     * @param view The View to attach the Smartcar Auth flow launch to
     * @param state optional OAuth state to be returned on redirect
     */
    public void addClickHandler(final Context context, final View view, final String state){
        final View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                launchAuthFlow(context, state);
            }
        };
        view.setOnClickListener(listener);
    }

    /**
     * Generates a click event listener for managing the Smartcar Auth flow, and attaches
     *  it to the input View.
     *
     * @param context The client application's context
     * @param view The View to attach the Smartcar Auth flow launch to
     * @param forcePrompt force permissions prompt to display on redirect (default: false)
     */
    public void addClickHandler(final Context context, final View view, final boolean forcePrompt) {
        final View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                launchAuthFlow(context, forcePrompt);
            }
        };
        view.setOnClickListener(listener);
    }

    /**
     * Starts the launch of the Smartcar auth flow. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     */
    public void launchAuthFlow(final Context context) {
        Helper.startActivity(context, generateUrl());
    }

    /**
     * Starts the launch of the Smartcar auth flow. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     * @param state optional OAuth state to be returned on redirect
     * @param forcePrompt force permissions prompt to display on redirect (default: false)
     */
    public void launchAuthFlow(final Context context, String state, boolean forcePrompt) {
        Helper.startActivity(context, generateUrl(state, forcePrompt));
    }

    /**
     * Starts the launch of the Smartcar auth flow. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     * @param state optional OAuth state to be returned on redirect
     */
    public void launchAuthFlow(final Context context, String state) {
        Helper.startActivity(context, generateUrl(state));
    }

    /**
     * Starts the launch of the Smartcar auth flow. Use this to attach to any event
     * trigger like a swipe or touch event on the client application.
     *
     * @param context The client application's context
     * @param forcePrompt force permissions prompt to display on redirect (default: false)
     */
    public void launchAuthFlow(final Context context, boolean forcePrompt) {
        Helper.startActivity(context, generateUrl(forcePrompt));
    }

    /**
     * Receives the response for the authorization request and sends it back to the calling function
     * via the callback method. The code is packed in a Bundle with the key "code".
     *
     * @param uri The response data as a Uri
     */
    protected static void receiveResponse(Uri uri) {
        String code;
        String error;
        String message;
        String state;

        if (uri != null && Helper.matchesRedirectUri(uri.toString())) {
            state = uri.getQueryParameter("state");
            message = uri.getQueryParameter("error_description");
            String queryCode = uri.getQueryParameter("code");
            String queryError = uri.getQueryParameter("error");
            String queryVin = uri.getQueryParameter("vin");

            if (queryCode != null) {
                SmartcarResponse smartcarResponse = new SmartcarResponse(message, state);
                smartcarResponse.setCode(queryCode);
                callback.handleResponse(smartcarResponse);
            }

            else if (queryError != null && queryVin == null) {
                SmartcarResponse smartcarResponse = new SmartcarResponse(message, state);
                smartcarResponse.setError(queryError);
                callback.handleResponse(smartcarResponse);
            }

            else if (queryError != null && queryVin != null) {
                String vin = queryVin;
                String make = uri.getQueryParameter("make");
                String model = uri.getQueryParameter("model");
                String year = uri.getQueryParameter("year");
                VehicleResponse vehicle = new VehicleResponse(vin, make, model, year);
                SmartcarResponse smartcarResponse = new SmartcarResponse(queryError, message, state, vehicle);
                callback.handleResponse(smartcarResponse);
            }

            else if (queryCode == null && message == null) {
                message = "Unable to fetch code. Please try again";
                SmartcarResponse smartcarResponse = new SmartcarResponse(message, state);
                callback.handleResponse(smartcarResponse);
            }

        }
    }
}

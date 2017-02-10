/**
 * Copyright (c) 2017-present, Smartcar, Inc. All rights reserved.

 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Smartcar.
 *
 * As with any software that integrates with the Smartcar platform, your use of
 * this software is subject to the Smartcar Developer Agreement
 * [https://developer.smartcar.com/agreement/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
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
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Main class that provides SDK access methods.
 */
public class SmartcarAuth {
    private static Context context;
    protected static SmartcarAuthRequest smartcarAuthRequest;
    private static SmartcarCallback callback;

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context     The application's context
     * @param callback    Handler to a Callback for receiving the authentication response
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       A space-separated list of authentication scopes
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String scope) {
        this.context = context;
        this.callback = callback;
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context     The application's context
     * @param callback    Handler to a Callback for receiving the authentication response
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authentication scopes
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String[] scope) {
        this.context = context;
        this.callback = callback;
        String scopeStr = arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context      The application's context
     * @param callback     Handler to a Callback for receiving the authentication response
     * @param clientId     The client's ID
     * @param redirectUri  The client's redirect URI
     * @param scope        A space-separated list of authentication scopes
     * @param responseType The required response type. One of ResponseType.code or ResponseType.token
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId,
                        String redirectUri, String scope, SmartcarAuthRequest.ResponseType responseType) {
        this.context = context;
        this.callback = callback;
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, responseType);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context      The application's context
     * @param callback     Handler to a Callback for receiving the authentication response
     * @param clientId     The client's ID
     * @param redirectUri  The client's redirect URI
     * @param scope        An array of authentication scopes
     * @param responseType The required response type. One of ResponseType.code or ResponseType.token
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId,
                        String redirectUri, String[] scope, SmartcarAuthRequest.ResponseType responseType) {
        this.context = context;
        this.callback = callback;
        String scopeStr = arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, responseType);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context        The application's context
     * @param callback       Handler to a Callback for receiving the authentication response
     * @param clientId       The client's ID
     * @param redirectUri    The client's redirect URI
     * @param scope          A space-separated list of authentication scopes
     * @param approvalPrompt ApprovalPrompt type. ApprovalPrompt.auto to request auto-approval.
     *                       ApprovalPrompt.force to force the approval UI to show
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String scope, SmartcarAuthRequest.ApprovalPrompt approvalPrompt) {
        this.context = context;
        this.callback = callback;
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, approvalPrompt);
    }
    /**
     * Constructs an instance with the given parameters.
     *
     * @param context        The application's context
     * @param callback       Handler to a Callback for receiving the authentication response
     * @param clientId       The client's ID
     * @param redirectUri    The client's redirect URI
     * @param scope          An array of authentication scopes
     * @param approvalPrompt ApprovalPrompt type. ApprovalPrompt.auto to request auto-approval.
     *                       ApprovalPrompt.force to force the approval UI to show
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String[] scope, SmartcarAuthRequest.ApprovalPrompt approvalPrompt) {
        this.context = context;
        this.callback = callback;
        String scopeStr = arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, approvalPrompt);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context        The application's context
     * @param callback       Handler to a Callback for receiving the authentication response
     * @param clientId       The client's ID
     * @param redirectUri    The client's redirect URI
     * @param scope          A space-separated list of authentication scopes
     * @param responseType   The required response type. One of ResponseType.code or ResponseType.token
     * @param approvalPrompt ApprovalPrompt type. ApprovalPrompt.auto to request auto-approval.
     *                       ApprovalPrompt.force to force the approval UI to show
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String scope, SmartcarAuthRequest.ResponseType responseType,
                        SmartcarAuthRequest.ApprovalPrompt approvalPrompt) {
        this.context = context;
        this.callback = callback;
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, responseType, approvalPrompt);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context        The application's context
     * @param callback       Handler to a Callback for receiving the authentication response
     * @param clientId       The client's ID
     * @param redirectUri    The client's redirect URI
     * @param scope          An array of authentication scopes
     * @param responseType   The required response type. One of ResponseType.code or ResponseType.token
     * @param approvalPrompt ApprovalPrompt type. ApprovalPrompt.auto to request auto-approval.
     *                       ApprovalPrompt.force to force the approval UI to show
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String[] scope, SmartcarAuthRequest.ResponseType responseType,
                        SmartcarAuthRequest.ApprovalPrompt approvalPrompt) {
        this.context = context;
        this.callback = callback;
        String scopeStr = arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, responseType, approvalPrompt);
    }

    /**
     * Generates a button with the appropriate background color and padding, and attaches a click
     * listener, for a given OEM.
     *
     * @param oem          The OEM to generate the button for
     * @param layoutParams The Layout Params for the button
     * @return             The generated Button
     */
    public Button generateButton(OEM oem, LinearLayout.LayoutParams layoutParams) {
        Button button = SmartcarAuthButtonGenerator.generateButton(context, smartcarAuthRequest, oem, layoutParams);
        return button;
    }

    /**
     * Receives the response for the authorization request and sends it back to the calling function
     * via the callback method. The code is packed in a Bundle with the key "code".
     *
     * @param uri The response data as a Uri
     */
    protected static void receiveResponse(Uri uri) {
        String code = null;
        String message = null;

        if (uri != null && matchesRedirectUri(uri.toString())) {
            String stateReturned = uri.getQueryParameter("state");

            if (stateReturned.equals(SmartcarAuth.smartcarAuthRequest.getState())) {
                code = uri.getQueryParameter("code");
                if (code == null) {
                    message = "Unable to fetch code. Please try again";
                }

                SmartcarResponse smartcarResponse = new SmartcarResponse(code, message);
                callback.handleResponse(smartcarResponse);
            }
        }
    }

    /**
     * Helper method to check if a String starts with the intended redirect URI.
     *
     * @param response The String to check against the redirect URI
     * @return         True if the String matched. False otherwise
     */
    protected static Boolean matchesRedirectUri(String response) {
        return (response.startsWith(SmartcarAuth.smartcarAuthRequest.getRedirectURI())) ? true : false;
    }

    /**
     * Helper method to convert an array of Strings to a space-separated string
     *
     * @param array The array of Strings to flatten
     * @return      The flattened string
     */
    protected static String arrayToString(String[] array) {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < array.length; i++) {
            if (i > 0) stringBuffer.append(" ");
            stringBuffer.append(array[i]);
        }
        String retString = stringBuffer.toString();
        return retString;
    }
}
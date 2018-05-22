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
    private Context context;
    protected static SmartcarAuthRequest smartcarAuthRequest;
    private static SmartcarCallback callback;
    private View.OnClickListener listener;

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
        this(context, callback);
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
        this(context, callback);
        String scopeStr = Helper.arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr);
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
                        String scope, ApprovalPrompt approvalPrompt) {
        this(context, callback);
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
                        String[] scope, ApprovalPrompt approvalPrompt) {
        this(context, callback);
        String scopeStr = Helper.arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, approvalPrompt);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context     The application's context
     * @param callback    Handler to a Callback for receiving the authentication response
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authentication scopes
     * @param development Whether to display the MOCK vehicle brand or not
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String scope, Boolean development) {
        this(context, callback);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, development);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param context     The application's context
     * @param callback    Handler to a Callback for receiving the authentication response
     * @param clientId    The client's ID
     * @param redirectUri The client's redirect URI
     * @param scope       An array of authentication scopes
     * @param development Whether to display the MOCK vehicle brand or not
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String[] scope, Boolean development) {
        this(context, callback);
        String scopeStr = Helper.arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, development);
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
     * @param development    Whether to display the MOCK vehicle brand or not
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String scope, ApprovalPrompt approvalPrompt, Boolean development) {
        this(context, callback);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, approvalPrompt, development);
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
     * @param development    Whether to display the MOCK vehicle brand or not
     */
    public SmartcarAuth(Context context, SmartcarCallback callback, String clientId, String redirectUri,
                        String[] scope, ApprovalPrompt approvalPrompt, Boolean development) {
        this(context, callback);
        String scopeStr = Helper.arrayToString(scope);
        this.smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scopeStr, approvalPrompt, development);
    }

    private SmartcarAuth(final Context context, SmartcarCallback callback) {
        this.context = context;
        this.callback = callback;
        this.listener = new View.OnClickListener() {
            public void onClick(View v) {
                Helper.startActivity(context, smartcarAuthRequest);
            }
        };
    }
    /**
     * Generates a click event listener for managing the Smartcar Auth flow, and attaches
     *  it to the input View.
     *
     * @param view The View to attach the Smartcar Auth flow launch to
     */
    public void addClickHandler(View view) {
        view.setOnClickListener(listener);
    }

    /**
     * Receives the response for the authorization request and sends it back to the calling function
     * via the callback method. The code is packed in a Bundle with the key "code".
     *
     * @param uri The response data as a Uri
     */
    protected static void receiveResponse(Uri uri) {
        String code;
        String message;

        if (uri != null && Helper.matchesRedirectUri(uri.toString())) {
            String stateReturned = uri.getQueryParameter("state");

            if (stateReturned.equals(SmartcarAuth.smartcarAuthRequest.getState())) {
                code = uri.getQueryParameter("code");
                message = uri.getQueryParameter("error_description");
                if (code == null && message == null) {
                    message = "Unable to fetch code. Please try again";
                }

                SmartcarResponse smartcarResponse = new SmartcarResponse(code, message);
                callback.handleResponse(smartcarResponse);
            }
        }
    }
}
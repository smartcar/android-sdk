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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class that provides SDK access methods.
 */
public class SmartcarAuth {
    private Context context;
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
        String scopeStr = Helper.arrayToString(scope);
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
        String scopeStr = Helper.arrayToString(scope);
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
        String scopeStr = Helper.arrayToString(scope);
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
        String scopeStr = Helper.arrayToString(scope);
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
     * Generates a click event listener for a given SmartcarAuth object.
     *
     * @return The OnClickListener object
     */
    public View.OnClickListener getOnClickListener() {
        return SmartcarAuthButtonGenerator.handleOnClick(this);
    }

    /**
     * Generates a spinner containing the entire list of OEMs as items.
     * Will not include MOCK as a vehicle option.
     *
     * @return             The generated Spinner
     */
    public Spinner generateSpinner() {
        return generateSpinner(false);
    }

    /**
     * Generates a spinner containing the entire list of OEMs as items.
     *
     * @param devMode      Boolean used to indicate whether to list MOCK as an option or not
     * @return             The generated Spinner
     */
    public Spinner generateSpinner(Boolean devMode) {
        ArrayList<OEM> list = new ArrayList<OEM>(Arrays.asList(OEM.values()));
        if (!devMode) {
            list.remove(OEM.MOCK);
        }
        OEM[] oemList = list.toArray(new OEM[list.size()]);
        return generateSpinner(oemList);
    }

    /**
     * Generates a spinner containing the given list of OEMs as items.
     *
     * @param oemList      The list of OEMs to include as items in the spinner
     * @return             The generated Spinner
     */
    public Spinner generateSpinner(OEM[] oemList) {
        Spinner spinner = SmartcarAuthSpinnerGenerator.generateSpinner(context, smartcarAuthRequest, oemList);
        return spinner;
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

    /**
     * Getter method for Context.
     *
     * @return The Context member variable
     */
    protected Context getContext() {
        return context;
    }
}
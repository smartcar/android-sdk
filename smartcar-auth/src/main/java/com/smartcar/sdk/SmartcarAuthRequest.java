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

import java.util.UUID;

/**
 * Class to maintain the authentication parameters.
 */
class SmartcarAuthRequest {

    private String clientId;
    private String redirectURI;
    private String scope;
    private String state;
    private ResponseType responseType;
    private ApprovalPrompt approvalPrompt;
    private Boolean development;

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId       The client's ID
     * @param redirectURI    The client's redirect URI
     * @param scope          A space-separated list of authentication scopes
     * @param approvalPrompt ApprovalPrompt type. ApprovalPrompt.auto to request auto-approval.
     *                       ApprovalPrompt.force to force the approval UI to show
     * @param development    Whether to display the MOCK vehicle brand or not
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI,
                        String scope, ApprovalPrompt approvalPrompt, Boolean development) {
        this.clientId = clientId;
        this.redirectURI = redirectURI;
        this.scope = scope;
        this.approvalPrompt = approvalPrompt;
        this.development = development;
        this.state = UUID.randomUUID().toString();
        this.responseType = ResponseType.code;
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId       The client's ID
     * @param redirectURI    The client's redirect URI
     * @param scope          A space-separated list of authentication scopes
     * @param approvalPrompt ApprovalPrompt type. ApprovalPrompt.auto to request auto-approval.
     *                       ApprovalPrompt.force to force the approval UI to show
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI,
                                  String scope, ApprovalPrompt approvalPrompt) {
        this(clientId, redirectURI, scope, approvalPrompt, false);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId     The client's ID
     * @param redirectURI  The client's redirect URI
     * @param scope        A space-separated list of authentication scopes
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI,
                        String scope, Boolean development) {
        this(clientId, redirectURI, scope, ApprovalPrompt.auto, development);
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectURI The client's redirect URI
     * @param scope       A space-separated list of authentication scopes
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI,
                               String scope) {
        this(clientId, redirectURI, scope, ApprovalPrompt.auto, false);
    }

    /**
     * Gets the client's ID.
     *
     * @return The client's ID
     */
    protected String getClientId() {
        return clientId;
    }

    /**
     * Gets the client's redirect URI.
     *
     * @return The client's redirect URI
     */
    protected String getRedirectURI() {
        return redirectURI;
    }

    /**
     * Gets the requested scopes.
     *
     * @return A space-separated list of the requested scopes
     */
    protected String getScope() {
        return scope;
    }

    /**
     * Gets the state parameter's value. The state is set to a random UUID value.
     *
     * @return The state parameter's value
     */
    protected String getState() {
        return state;
    }

    /**
     * Gets the requested response type.
     *
     * @return One of ResponseType.code or ResponseType.token. Default setting is ResponseType.code
     */
    protected ResponseType getResponseType() {
        return responseType;
    }

    /**
     * Gets the requested approval type.
     *
     * @return One of ApprovalPrompt.auto or ApprovalPrompt.force. Default setting is ApprovalPrompt.auto
     */
    protected ApprovalPrompt getApprovalPrompt() {
        return approvalPrompt;
    }

    /**
     * Gets the setting that indicates whether MOCK vehicle brand should be displayed or not.
     *
     * @return The Boolean value
     */
    protected Boolean getDevelopment() {
        return development;
    }

    /**
     * Sets the state to a random UUID value.
     *
     * @return String The state
     */
    protected String setState() {
        this.state = UUID.randomUUID().toString();
        return state;
    }

    /**
     * Generates the authorization request URI.
     *
     * @return The authorization request URI
     */
    protected String generateAuthRequestUri() {

        String requestUri = "https://connect.smartcar.com/oauth/authorize?response_type="
                + getResponseType().toString()
                + "&client_id=" + getClientId()
                + "&redirect_uri=" + getRedirectURI()
                + "&scope=" + getScope()
                + "&state=" + setState()
                + "&approval_prompt=" + getApprovalPrompt().toString()
                +"&mock=" + getDevelopment();

        return requestUri;
    }
}

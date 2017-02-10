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

    /**
     * Enum that defines the approval prompt types.
     */
    protected enum ApprovalPrompt {
        force, auto
    }

    /**
     * Enum that defines the response types.
     */
    protected enum ResponseType {
        code, token
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId       The client's ID
     * @param redirectURI    The client's redirect URI
     * @param scope          A space-separated list of authentication scopes
     * @param responseType   The required response type. One of ResponseType.code or ResponseType.token
     * @param approvalPrompt ApprovalPrompt type. ApprovalPrompt.auto to request auto-approval.
     *                       ApprovalPrompt.force to force the approval UI to show
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI,
                        String scope, ResponseType responseType,
                               ApprovalPrompt approvalPrompt) {
        this.clientId = clientId;
        this.redirectURI = redirectURI;
        this.scope = scope;
        this.state = UUID.randomUUID().toString();
        this.responseType = responseType;
        this.approvalPrompt = approvalPrompt;
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId     The client's ID
     * @param redirectURI  The client's redirect URI
     * @param scope        A space-separated list of authentication scopes
     * @param responseType The required response type. One of ResponseType.code or ResponseType.token
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI,
                        String scope, ResponseType responseType) {
        this(clientId, redirectURI, scope, responseType, ApprovalPrompt.auto);
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
        this(clientId, redirectURI, scope, ResponseType.code, approvalPrompt);
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
        this(clientId, redirectURI, scope, ResponseType.code, ApprovalPrompt.auto);
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
     * Gets teh client's redirect URI.
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
     * Sets the state to a random UUID value.
     *
     * @return String The state
     */
    protected String setState() {
        this.state = UUID.randomUUID().toString();
        return state;
    }

    /**
     * Generates the authorization request URI for a given OEM.
     *
     * @param oem The OEM to generate the authorization request URI for
     * @return The authorization request URI
     */
    protected String generateAuthRequestUri(OEM oem) {

        String requestUri = "https://" + oem.getAuthName()
                + ".smartcar.com/oauth/authorize?response_type=" + getResponseType().toString()
                + "&client_id=" + getClientId()
                + "&redirect_uri=" + getRedirectURI() + "&scope=" + getScope()
                + "&state=" + setState()
                + "&approval_prompt=" + getApprovalPrompt().toString();

        return requestUri;
    }
}

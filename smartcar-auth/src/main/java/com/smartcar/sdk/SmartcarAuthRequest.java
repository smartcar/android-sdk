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

/**
/**
 * Class to maintain the authentication parameters.
 */
class SmartcarAuthRequest {

    private String clientId;
    private String redirectURI;
    private String scope;
    private ResponseType responseType;
    private Boolean testMode;

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId       The client's ID
     * @param redirectURI    The client's redirect URI
     * @param scope          A space-separated list of authentication scopes
     * @param testMode       Whether to display the MOCK vehicle brand or not
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI,
                        String scope, boolean testMode) {
        this.clientId = clientId;
        this.redirectURI = redirectURI;
        this.scope = scope;
        this.testMode = testMode;
        this.responseType = ResponseType.code;
    }

    /**
     * Constructs an instance with the given parameters.
     *
     * @param clientId    The client's ID
     * @param redirectURI The client's redirect URI
     * @param scope       A space-separated list of authentication scopes
     */
    protected SmartcarAuthRequest(String clientId, String redirectURI, String scope) {
        this(clientId, redirectURI, scope, false);
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
     * Gets the requested response type.
     *
     * @return One of ResponseType.code or ResponseType.token. Default setting is ResponseType.code
     */
    protected ResponseType getResponseType() {
        return responseType;
    }

    /**
     * Gets the setting that indicates whether MOCK vehicle brand should be displayed or not.
     *
     * @return The Boolean value
     */
    protected Boolean getTestMode() {
        return testMode;
    }

}

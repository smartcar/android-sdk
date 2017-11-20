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

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SmartcarAuthTest {
    
    @Test
    public void smartcarAuth_default() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), scope);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.code);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuth_default_scopeArray() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), "read_odometer read_vin");
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.code);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuth_includeResponseType() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest.ResponseType responseType = SmartcarAuthRequest.ResponseType.token;
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope, responseType);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), scope);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.token);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuth_includeResponseType_scopeArray() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};
        SmartcarAuthRequest.ResponseType responseType = SmartcarAuthRequest.ResponseType.token;
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope, responseType);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), "read_odometer read_vin");
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.token);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuth_includeApprovalPrompt() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest.ApprovalPrompt approvalPrompt = SmartcarAuthRequest.ApprovalPrompt.force;
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope, approvalPrompt);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), scope);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.code);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.force);
    }

    @Test
    public void smartcarAuth_includeApprovalPrompt_scopeArray() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};
        SmartcarAuthRequest.ApprovalPrompt approvalPrompt = SmartcarAuthRequest.ApprovalPrompt.force;
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope, approvalPrompt);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), "read_odometer read_vin");
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.code);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.force);
    }

    @Test
    public void smartcarAuth_noDefaults() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest.ResponseType responseType = SmartcarAuthRequest.ResponseType.token;
        SmartcarAuthRequest.ApprovalPrompt approvalPrompt = SmartcarAuthRequest.ApprovalPrompt.force;
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope, responseType, approvalPrompt);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), scope);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.token);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.force);
    }

    @Test
    public void smartcarAuth_noDefaults_scopeArray() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};
        SmartcarAuthRequest.ResponseType responseType = SmartcarAuthRequest.ResponseType.token;
        SmartcarAuthRequest.ApprovalPrompt approvalPrompt = SmartcarAuthRequest.ApprovalPrompt.force;
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, null, clientId, redirectUri, scope, responseType, approvalPrompt);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getClientId(), clientId);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getRedirectURI(), redirectUri);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getScope(), "read_odometer read_vin");
        assertEquals(SmartcarAuth.smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.token);
        assertEquals(SmartcarAuth.smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.force);
    }

    @Test
    public void smartcarAuth_receiveResponse() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        class MyCallback implements SmartcarCallback {
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getCode(), "testcode123");
            }
        }
        MyCallback callback = new MyCallback();

        SmartcarAuth smartcarAuth = new SmartcarAuth(null, callback, clientId, redirectUri, scope);
        String state = smartcarAuth.smartcarAuthRequest.getState();
        Uri uri = Uri.parse(redirectUri + "?code=testcode123&state=" + state);

        SmartcarAuth.receiveResponse(uri);
    }

    @Test
    public void smartcarAuth_receiveResponse_mismatchRedirectUri() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        class MyCallback implements SmartcarCallback {
            public void handleResponse(SmartcarResponse smartcarResponse) {
                throw new AssertionError("Response should not be received.");
            }
        }
        MyCallback callback = new MyCallback();

        String wrongRedirectUri = "wrongscheme://test";
        SmartcarAuth smartcarAuth = new SmartcarAuth(null, callback, clientId, redirectUri, scope);
        String state = smartcarAuth.smartcarAuthRequest.getState();
        Uri uri = Uri.parse(wrongRedirectUri + "?code=testcode123&state=" + state);

        SmartcarAuth.receiveResponse(uri);
    }

    @Test
    public void smartcarAuth_receiveResponse_nullUri() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        class MyCallback implements SmartcarCallback {
            public void handleResponse(SmartcarResponse smartcarResponse) {
                throw new AssertionError("Response should not be received.");
            }
        }
        MyCallback callback = new MyCallback();

        SmartcarAuth smartcarAuth = new SmartcarAuth(null, callback, clientId, redirectUri, scope);
        Uri uri = null;

        SmartcarAuth.receiveResponse(uri);
    }

    @Test
    public void smartcarAuth_receiveResponse_mismatchedState() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        class MyCallback implements SmartcarCallback {
            public void handleResponse(SmartcarResponse smartcarResponse) {
                throw new AssertionError("Response should not be received.");
            }
        }
        MyCallback callback = new MyCallback();

        SmartcarAuth smartcarAuth = new SmartcarAuth(null, callback, clientId, redirectUri, scope);
        String state = "wrongstate";
        Uri uri = Uri.parse(redirectUri + "?code=testcode123&state=" + state);

        SmartcarAuth.receiveResponse(uri);
    }

    @Test
    public void smartcarAuth_receiveResponse_nullCode() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        class MyCallback implements SmartcarCallback {
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getMessage(), "Unable to fetch code. Please try again");
            }
        }
        MyCallback callback = new MyCallback();

        SmartcarAuth smartcarAuth = new SmartcarAuth(null, callback, clientId, redirectUri, scope);
        String state = smartcarAuth.smartcarAuthRequest.getState();
        Uri uri = Uri.parse(redirectUri + "?state=" + state);

        SmartcarAuth.receiveResponse(uri);
    }
}

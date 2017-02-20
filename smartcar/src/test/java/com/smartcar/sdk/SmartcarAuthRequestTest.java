/**
 * Copyright (c) 2017-present, Smartcar, Inc. All rights reserved.

 * You are hereby granted a limted, non-exclusive, worldwide, royalty-free
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SmartcarAuthRequestTest {

    @Test
    public void smartcarAuthRequest_ApprovalPrompt() throws Exception {
        assertEquals(SmartcarAuthRequest.ApprovalPrompt.values().length, 2);
        assertEquals(SmartcarAuthRequest.ApprovalPrompt.valueOf("auto"), SmartcarAuthRequest.ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuthRequest_ResponseType() throws Exception {
        assertEquals(SmartcarAuthRequest.ResponseType.values().length, 2);
        assertEquals(SmartcarAuthRequest.ResponseType.valueOf("token"), SmartcarAuthRequest.ResponseType.token);
    }

    @Test
    public void smartcarAuthRequest_default() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope);
        assertEquals(smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.code);
        assertEquals(smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuthRequest_includeResponseType() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest.ResponseType responseType = SmartcarAuthRequest.ResponseType.token;
        SmartcarAuthRequest smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, responseType);
        assertEquals(smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.token);
        assertEquals(smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuthRequest_includeApprovalPrompt() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest.ApprovalPrompt approvalPrompt = SmartcarAuthRequest.ApprovalPrompt.force;
        SmartcarAuthRequest smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, approvalPrompt);
        assertEquals(smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.code);
        assertEquals(smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.force);
    }

    @Test
    public void smartcarAuthRequest_noDefaults() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest.ResponseType responseType = SmartcarAuthRequest.ResponseType.token;
        SmartcarAuthRequest.ApprovalPrompt approvalPrompt = SmartcarAuthRequest.ApprovalPrompt.force;
        SmartcarAuthRequest smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, responseType, approvalPrompt);
        assertEquals(smartcarAuthRequest.getResponseType(), SmartcarAuthRequest.ResponseType.token);
        assertEquals(smartcarAuthRequest.getApprovalPrompt(), SmartcarAuthRequest.ApprovalPrompt.force);
    }

    @Test
    public void smartcarAuthRequest_generateAuthRequestUri() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope);

        String requestUri = smartcarAuthRequest.generateAuthRequestUri(OEM.BMW_CONNECTED);
        String expectedUri = "https://bmw-connected.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&state=" + smartcarAuthRequest.getState() + "&approval_prompt=auto";

        assertEquals(expectedUri, requestUri);
    }
}

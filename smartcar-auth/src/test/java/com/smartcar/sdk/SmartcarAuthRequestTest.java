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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartcarAuthRequestTest {

    @Test
    public void smartcarAuthRequest_ApprovalPrompt() throws Exception {
        assertEquals(ApprovalPrompt.values().length, 2);
        assertEquals(ApprovalPrompt.valueOf("auto"), ApprovalPrompt.auto);
    }

    @Test
    public void smartcarAuthRequest_ResponseType() throws Exception {
        assertEquals(ResponseType.values().length, 2);
        assertEquals(ResponseType.valueOf("token"), ResponseType.token);
    }

    @Test
    public void smartcarAuthRequest_default() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope);
        assertEquals(smartcarAuthRequest.getResponseType(), ResponseType.code);
    }

    @Test
    public void smartcarAuthRequest_includeDevelopment() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuthRequest smartcarAuthRequest = new SmartcarAuthRequest(clientId, redirectUri, scope, true);
        assertTrue(smartcarAuthRequest.getDevelopment());
        assertEquals(smartcarAuthRequest.getResponseType(), ResponseType.code);
    }
}

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
    public void smartcarAuth_generateUrl() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);

        String requestUri = smartcarAuth.generateUrl();
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&approval_prompt=auto&mode=live";

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_generateUrl_forcePrompt() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);

        String requestUri = smartcarAuth.generateUrl(true);
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&approval_prompt=force&mode=live";

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_generateUrl_state() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);

        String requestUri = smartcarAuth.generateUrl("somestring");
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&state=somestring&approval_prompt=auto&mode=live";

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_generateUrl_authVehicleInfo() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String make = "TESLA";
        SmartcarAuth.AuthVehicleInfo authVehicleInfo = new SmartcarAuth.AuthVehicleInfo.Builder().setMake(make).build();

        String requestUri = smartcarAuth.generateUrl(authVehicleInfo);
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&approval_prompt=auto&mode=live&make=" + make;

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_generateUrl_stateAndForcePrompt() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);

        String requestUri = smartcarAuth.generateUrl("somestring", true);
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&state=somestring&approval_prompt=force&mode=live";

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_generateUrl_stateAndAuthVehicleInfo() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String make = "TESLA"

        SmartcarAuth.AuthVehicleInfo.Builder builder = new SmartcarAuth.AuthVehicleInfo.Builder();
        SmartcarAuth.AuthVehicleInfo vehicleInfo = builder.setMake(make).build();

        String requestUri = smartcarAuth.generateUrl("somestring", vehicleInfo);
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&state=somestring&approval_prompt=auto&mode=live&make=" + make;

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_generateUrl_forcePromptAndAuthVehicleInfo() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String make = "TESLA"

        SmartcarAuth.AuthVehicleInfo.Builder builder = new SmartcarAuth.AuthVehicleInfo.Builder();
        SmartcarAuth.AuthVehicleInfo vehicleInfo = builder.setMake("TESLA").build();

        String requestUri = smartcarAuth.generateUrl(true, vehicleInfo);
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&approval_prompt=force&mode=live&make=" + make;

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_generateUrl_stateAndforcePromptAndAuthVehicleInfo() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String make = "TESLA"

        SmartcarAuth.AuthVehicleInfo.Builder builder = new SmartcarAuth.AuthVehicleInfo.Builder();
        SmartcarAuth.AuthVehicleInfo vehicleInfo = builder.setMake("TESLA").build();

        String requestUri = smartcarAuth.generateUrl("somestring", true, vehicleInfo);
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope +
                "&state=somestring&approval_prompt=force&mode=live&make=" + make;

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_receiveResponse() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getCode(), "testcode123");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?code=testcode123"));
    }

    @Test
    public void smartcarAuth_receiveResponse_mismatchRedirectUri() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        String wrongRedirectUri = "wrongscheme://test";

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                throw new AssertionError("Response should not be received.");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(wrongRedirectUri));
    }

    @Test
    public void smartcarAuth_receiveResponse_nullUri() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                throw new AssertionError("Response should not be received.");
            }
        });

        SmartcarAuth.receiveResponse(null);
    }

    @Test
    public void smartcarAuth_receiveResponse_nullCode() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getMessage(), "Unable to fetch code. Please try again");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri));
    }

    @Test
    public void smartcarAuth_receiveResponse_nullCodeWithMessage() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getMessage(), "error");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?error_description=error"));
    }

    @Test
    public void smartcarAuth_receiveResponse_codeWithState() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getCode(), "testCode");
                assertEquals(smartcarResponse.getState(), "testState");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?code=testCode&state=testState"));
    }
}

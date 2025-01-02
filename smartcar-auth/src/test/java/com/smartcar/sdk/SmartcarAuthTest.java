package com.smartcar.sdk;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class SmartcarAuthTest {

    @Test
    public void smartcarAuth_authUrlBuilder() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String redirectUriEncoded = "scclient123%3A%2F%2Ftest";
        String[] scope = {"read_odometer", "read_vin"};
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUriEncoded +
                "&mode=live&scope=read_odometer%20read_vin";


        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String requestUri = smartcarAuth.authUrlBuilder().build();

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_authUrlBuilder_testMode() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String redirectUriEncoded = "scclient123%3A%2F%2Ftest";
        String[] scope = {"read_odometer", "read_vin"};
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUriEncoded +
                "&mode=test&scope=read_odometer%20read_vin";


        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, true, null);
        String requestUri = smartcarAuth.authUrlBuilder().build();

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_authUrlBuilderWithSetters() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String redirectUriEncoded = "scclient123%3A%2F%2Ftest";
        String[] scope = {"read_odometer", "read_vin"};
        String vin = "1234567890ABCDEFG";
        String[] flags = {"flag:suboption", "feature3"};
        String user = "e9b24987-52e8-4d40-8417-bfa4402c9e16";
        String expectedUri = "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUriEncoded +
                "&mode=live&scope=read_odometer%20read_vin" +
                "&approval_prompt=force&make=BMW&state=some%20state" +
                "&single_select=true" +
                "&single_select_vin=" + vin +
                "&flags=flag%3Asuboption%20feature3" +
                "&user=e9b24987-52e8-4d40-8417-bfa4402c9e16";

        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String requestUri = smartcarAuth.authUrlBuilder()
                .setForcePrompt(true)
                .setMakeBypass("BMW")
                .setState("some state")
                .setSingleSelect(true)
                .setSingleSelectVin(vin)
                .setFlags(flags)
                .setUser(user)
                .build();

        assertEquals(expectedUri, requestUri);
    }

    @Test
    public void smartcarAuth_addClickHandler() {

        // Setup mocks
        Context context = mock(Context.class);
        View view = mock(View.class);

        // Execute methods
        SmartcarAuth smartcarAuth = new SmartcarAuth(
                "client123",
                "scclient123://test",
                new String[] {"read_odometer", "read_vin"},
                null
        );
        smartcarAuth.addClickHandler(context, view);

        // Verify mocks
        verify(view, times(1))
                .setOnClickListener(Mockito.any(View.OnClickListener.class));

    }

    @Test
    public void smartcarAuth_addClickHandler_withAuthUrl() {

        // Setup mocks
        Context context = mock(Context.class);
        View view = mock(View.class);

        // Execute methods
        SmartcarAuth smartcarAuth = new SmartcarAuth(
                "client123",
                "scclient123://test",
                new String[] {"read_odometer", "read_vin"},
                null
        );
        String authUrl = smartcarAuth.authUrlBuilder()
                .setState("foo")
                .setMakeBypass("TESLA")
                .setSingleSelect(false)
                .build();
        smartcarAuth.addClickHandler(context, view, authUrl);

        // Verify mocks
        verify(view, times(1))
                .setOnClickListener(Mockito.any(View.OnClickListener.class));

    }

    @Test
    public void smartcarAuth_launchAuthFlow() {

        // Setup mocks
        Context context = mock(Context.class);

        // Execute methods
        SmartcarAuth smartcarAuth = new SmartcarAuth(
                "client123",
                "scclient123://test",
                new String[] {"read_odometer", "read_vin"},
                null
        );

        smartcarAuth.launchAuthFlow(context);

    }

    @Test
    public void smartcarAuth_launchAuthFlow_withAuthUrl() {

        // Setup mocks
        Context context = mock(Context.class);

        // Execute methods
        SmartcarAuth smartcarAuth = new SmartcarAuth(
                "client123",
                "scclient123://test",
                new String[] {"read_odometer", "read_vin"},
                null
        );
        String authUrl = smartcarAuth.authUrlBuilder()
                .setState("foo")
                .setMakeBypass("TESLA")
                .setSingleSelect(false)
                .build();
        smartcarAuth.launchAuthFlow(context, authUrl);


    }

    @Test
    public void smartcarAuth_receiveResponse() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};

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
        String[] scope = {"read_odometer", "read_vin"};
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
        String[] scope = {"read_odometer", "read_vin"};

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
        String[] scope = {"read_odometer", "read_vin"};

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getErrorDescription(), "Unable to fetch code. Please try again");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri));
    }

    @Test
    public void smartcarAuth_receiveResponse_accessDenied() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};
        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getError(), "access_denied");
                assertEquals(smartcarResponse.getErrorDescription(), "User denied access to the requested scope of permissions.");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?error=access_denied&error_description=User%20denied%20access%20to%20the%20requested%20scope%20of%20permissions."));
    }

    @Test
    public void smartcarAuth_receiveResponse_vehicleIncompatible() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};
        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getError(), "vehicle_incompatible");
                assertEquals(smartcarResponse.getErrorDescription(), "The user's vehicle is not compatible.");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?error=vehicle_incompatible&error_description=The%20user%27s%20vehicle%20is%20not%20compatible."));
    }

    @Test
    public void smartcarAuth_receiveResponse_vehicleIncompatibleWithVehicle() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};
        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                VehicleInfo responseVehicle = smartcarResponse.getVehicleInfo();
                assertEquals(smartcarResponse.getError(), "vehicle_incompatible");
                assertEquals(smartcarResponse.getErrorDescription(), "The user's vehicle is not compatible.");
                assertEquals(responseVehicle.getVin(), "1FDKE30G4JHA04964");
                assertEquals(responseVehicle.getMake(), "FORD");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?error=vehicle_incompatible" +
                "&error_description=The%20user%27s%20vehicle%20is%20not%20compatible." +
                "&vin=1FDKE30G4JHA04964&make=FORD"));
    }

    @Test
    public void smartcarAuth_receiveResponse_nullCodeWithMessage() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getErrorDescription(), "Unable to fetch code. Please try again");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?error_description=error"));
    }

    @Test
    public void smartcarAuth_receiveResponse_codeWithState() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getCode(), "testCode");
                assertEquals(smartcarResponse.getState(), "testState");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?code=testCode&state=testState"));
    }

    @Test
    public void smartcarAuth_receiveResponse_codeWithVirtualKeyUrl() {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String[] scope = {"read_odometer", "read_vin"};

        new SmartcarAuth(clientId, redirectUri, scope, new SmartcarCallback() {
            @Override
            public void handleResponse(SmartcarResponse smartcarResponse) {
                assertEquals(smartcarResponse.getCode(), "testCode");
                assertEquals(smartcarResponse.getState(), null);
                assertEquals(smartcarResponse.getVirtualKeyUrl(), "https://www.tesla.com/_ak/smartcar.com");
            }
        });

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri + "?code=testCode&virtual_key_url=https://www.tesla.com/_ak/smartcar.com"));
    }
}

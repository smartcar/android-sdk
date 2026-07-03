package com.smartcar.sdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SmartcarAuthTest {
    @Test
    fun smartcarAuth_authUrlBuilder() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val redirectUriEncoded = "scclient123%3A%2F%2Ftest"
        val scope = arrayOf("read_odometer", "read_vin")
        val expectedUri =
            "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                    "&application_id=" + applicationId +
                    "&redirect_uri=" + redirectUriEncoded +
                    "&mode=live&scope=read_odometer%20read_vin"


        val smartcarAuth = SmartcarAuth(applicationId, redirectUri, scope) {}
        val requestUri = smartcarAuth.authUrlBuilder().build()

        Assert.assertEquals(expectedUri, requestUri)
    }

    @Test
    fun smartcarAuth_authUrlBuilder_testMode() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val redirectUriEncoded = "scclient123%3A%2F%2Ftest"
        val scope = arrayOf("read_odometer", "read_vin")
        val expectedUri =
            "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                    "&application_id=" + applicationId +
                    "&redirect_uri=" + redirectUriEncoded +
                    "&mode=test&scope=read_odometer%20read_vin"


        val smartcarAuth = SmartcarAuth(applicationId, redirectUri, scope, true) {}
        val requestUri = smartcarAuth.authUrlBuilder().build()

        Assert.assertEquals(expectedUri, requestUri)
    }

    @Test
    fun smartcarAuth_authUrlBuilder_noScopeOrTestMode() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val redirectUriEncoded = "scclient123%3A%2F%2Ftest"
        val expectedUri =
            "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                    "&application_id=" + applicationId +
                    "&redirect_uri=" + redirectUriEncoded +
                    "&mode=live"

        val smartcarAuth = SmartcarAuth(applicationId, redirectUri) {}
        val requestUri = smartcarAuth.authUrlBuilder().build()

        Assert.assertEquals(expectedUri, requestUri)
    }

    @Test
    fun smartcarAuth_authUrlBuilder_noScope() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val redirectUriEncoded = "scclient123%3A%2F%2Ftest"
        val expectedUri =
            "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                    "&application_id=" + applicationId +
                    "&redirect_uri=" + redirectUriEncoded +
                    "&mode=test"

        val smartcarAuth = SmartcarAuth(applicationId, redirectUri, true) {}
        val requestUri = smartcarAuth.authUrlBuilder().build()

        Assert.assertEquals(expectedUri, requestUri)
    }

    @Test
    fun smartcarAuth_authUrlBuilderWithSetters() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val redirectUriEncoded = "scclient123%3A%2F%2Ftest"
        val scope = arrayOf("read_odometer", "read_vin")
        val vin = "1234567890ABCDEFG"
        val flags = arrayOf("flag:suboption", "feature3")
        val user = "e9b24987-52e8-4d40-8417-bfa4402c9e16"
        val expectedUri =
            "https://connect.smartcar.com/oauth/authorize?response_type=code" +
                    "&application_id=" + applicationId +
                    "&redirect_uri=" + redirectUriEncoded +
                    "&mode=live&scope=read_odometer%20read_vin" +
                    "&approval_prompt=force&make=BMW&state=some%20state" +
                    "&single_select=true" +
                    "&single_select_vin=" + vin +
                    "&flags=flag%3Asuboption%20feature3" +
                    "&user=e9b24987-52e8-4d40-8417-bfa4402c9e16"

        val smartcarAuth = SmartcarAuth(applicationId, redirectUri, scope) {}
        val requestUri = smartcarAuth.authUrlBuilder()
            .setForcePrompt(true)
            .setMakeBypass("BMW")
            .setState("some state")
            .setSingleSelect(true)
            .setSingleSelectVin(vin)
            .setFlags(flags)
            .setUser(user)
            .build()

        Assert.assertEquals(expectedUri, requestUri)
    }

    @Test
    fun smartcarAuth_addClickHandler() {
        // Setup mocks

        val context: Context = mock(Context::class.java)
        val view: View = mock(View::class.java)

        // Execute methods
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}
        smartcarAuth.addClickHandler(context, view)

        // Verify mocks
        verify(view, times(1))
            .setOnClickListener(Mockito.any(View.OnClickListener::class.java))
    }

    @Test
    fun smartcarAuth_addClickHandler_withAuthUrl() {
        // Setup mocks

        val context: Context = mock(Context::class.java)
        val view: View = mock(View::class.java)

        // Execute methods
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}
        val authUrl = smartcarAuth.authUrlBuilder()
            .setState("foo")
            .setMakeBypass("TESLA")
            .setSingleSelect(false)
            .build()
        smartcarAuth.addClickHandler(context, view, authUrl)

        // Verify mocks
        verify(view, times(1))
            .setOnClickListener(Mockito.any(View.OnClickListener::class.java))
    }

    @Test
    fun smartcarAuth_launchAuthFlow() {
        // Setup mocks

        val context: Context = mock(Context::class.java)

        // Execute methods
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}

        smartcarAuth.launchAuthFlow(context)
    }

    @Test
    fun smartcarAuth_launchAuthFlow_withAuthUrl() {
        // Setup mocks

        val context: Context = mock(Context::class.java)

        // Execute methods
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}
        val authUrl = smartcarAuth.authUrlBuilder()
            .setState("foo")
            .setMakeBypass("TESLA")
            .setSingleSelect(false)
            .build()
        smartcarAuth.launchAuthFlow(context, authUrl)
    }

    @Test
    fun smartcarAuth_launchAuthFlow_addsSdkParametersWhenMissing() {
        // Setup mocks
        val context: Context = mock(Context::class.java)

        // Create auth URL without sdk parameters
        val baseUrl = "https://connect.smartcar.com/oauth/authorize?response_type=code&application_id=test123&redirect_uri=test%3A%2F%2Fredirect"
        
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}

        // Execute method
        smartcarAuth.launchAuthFlow(context, baseUrl)

        // Verify that context.startActivity was called (this indirectly verifies the method completed successfully)
        verify(context, times(1)).startActivity(Mockito.any())
    }

    @Test
    fun smartcarAuth_launchAuthFlow_preservesExistingSdkParameters() {
        // Setup mocks
        val context: Context = mock(Context::class.java)

        // Create auth URL with existing sdk parameters
        val urlWithSdkParams = "https://connect.smartcar.com/oauth/authorize?response_type=code&application_id=test123&sdk_platform=ios&sdk_version=1.0.0&redirect_uri=test%3A%2F%2Fredirect"
        
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}

        // Execute method
        smartcarAuth.launchAuthFlow(context, urlWithSdkParams)

        // Verify that context.startActivity was called (this indirectly verifies the method completed successfully)
        verify(context, times(1)).startActivity(Mockito.any())
    }

    @Test
    fun smartcarAuth_launchAuthFlow_addsMissingParametersOnly() {
        // Setup mocks
        val context: Context = mock(Context::class.java)

        // Create auth URL with only sdk_platform but missing sdk_version
        val urlWithPartialSdkParams = "https://connect.smartcar.com/oauth/authorize?response_type=code&application_id=test123&sdk_platform=ios&redirect_uri=test%3A%2F%2Fredirect"
        
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}

        // Execute method
        smartcarAuth.launchAuthFlow(context, urlWithPartialSdkParams)

        // Verify that context.startActivity was called
        verify(context, times(1)).startActivity(Mockito.any())
    }

    @Test
    fun smartcarAuth_launchAuthFlow_verifiesUrlParameterHandling() {
        // Setup mocks
        val context: Context = mock(Context::class.java)
        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)

        // Create auth URL without sdk parameters
        val baseUrl = "https://connect.smartcar.com/oauth/authorize?response_type=code&application_id=test123&redirect_uri=test%3A%2F%2Fredirect"
        
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}

        // Execute method
        smartcarAuth.launchAuthFlow(context, baseUrl)

        // Capture the intent that was passed to startActivity
        verify(context, times(1)).startActivity(intentCaptor.capture())
        
        val capturedIntent = intentCaptor.value
        val authorizeUrl = capturedIntent.getStringExtra("authorize_url")
        
        // Verify that sdk parameters were added
        Assert.assertNotNull("authorize_url should not be null", authorizeUrl)
        Assert.assertTrue("URL should contain sdk_platform=android", 
            authorizeUrl!!.contains("sdk_platform=android"))
        Assert.assertTrue("URL should contain sdk_version=" + BuildConfig.VERSION_NAME, 
            authorizeUrl.contains("sdk_version=" + BuildConfig.VERSION_NAME))
    }

    @Test
    fun smartcarAuth_launchAuthFlow_preservesExistingSdkParametersVerification() {
        // Setup mocks
        val context: Context = mock(Context::class.java)
        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)

        // Create auth URL with existing sdk parameters
        val urlWithSdkParams = "https://connect.smartcar.com/oauth/authorize?response_type=code&application_id=test123&sdk_platform=blah&sdk_version=1.0.0&redirect_uri=test%3A%2F%2Fredirect"
        
        val smartcarAuth = SmartcarAuth(
            "client123",
            "scclient123://test",
            arrayOf("read_odometer", "read_vin"),
        ) {}

        // Execute method
        smartcarAuth.launchAuthFlow(context, urlWithSdkParams)

        // Capture the intent that was passed to startActivity
        verify(context, times(1)).startActivity(intentCaptor.capture())
        
        val capturedIntent = intentCaptor.value
        val authorizeUrl = capturedIntent.getStringExtra("authorize_url")
        
        // Verify that original sdk parameters were preserved
        Assert.assertNotNull("authorize_url should not be null", authorizeUrl)
        Assert.assertTrue("URL should preserve original sdk_platform=blah", 
            authorizeUrl!!.contains("sdk_platform=blah"))
        Assert.assertTrue("URL should preserve original sdk_version=1.0.0", 
            authorizeUrl.contains("sdk_version=1.0.0"))
        // Should not contain android or current SDK version
        Assert.assertFalse("URL should not contain duplicate sdk_platform=android", 
            authorizeUrl.contains("sdk_platform=android"))
        Assert.assertFalse("URL should not contain duplicate sdk_version=" + BuildConfig.VERSION_NAME, 
            authorizeUrl.contains("sdk_version=" + BuildConfig.VERSION_NAME))
    }

    @Test
    fun smartcarAuth_receiveResponse() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(
                smartcarResponse!!.code,
                "testcode123"
            )
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?code=testcode123"), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_mismatchRedirectUri() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")
        val wrongRedirectUri = "wrongscheme://test"

        SmartcarAuth(
            applicationId,
            redirectUri,
            scope
        ) { throw AssertionError("Response should not be received.") }

        SmartcarAuth.receiveResponse(Uri.parse(wrongRedirectUri), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_nullUri() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(
            applicationId,
            redirectUri,
            scope
        ) { throw AssertionError("Response should not be received.") }

        SmartcarAuth.receiveResponse(null, redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_nullCode() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(
                smartcarResponse!!.errorDescription,
                "Unable to fetch code. Please try again"
            )
        }

        SmartcarAuth.receiveResponse(Uri.parse(redirectUri), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_accessDenied() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")
        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(smartcarResponse!!.error, "access_denied")
            Assert.assertEquals(
                smartcarResponse.errorDescription,
                "User denied access to the requested scope of permissions."
            )
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?error=access_denied&error_description=User%20denied%20access%20to%20the%20requested%20scope%20of%20permissions."), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_vehicleIncompatible() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")
        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(smartcarResponse!!.error, "vehicle_incompatible")
            Assert.assertEquals(
                smartcarResponse.errorDescription,
                "The user's vehicle is not compatible."
            )
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?error=vehicle_incompatible&error_description=The%20user%27s%20vehicle%20is%20not%20compatible."), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_vehicleIncompatibleWithVehicle() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")
        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            val responseVehicle = smartcarResponse!!.vehicleInfo
            Assert.assertEquals(smartcarResponse.error, "vehicle_incompatible")
            Assert.assertEquals(
                smartcarResponse.errorDescription,
                "The user's vehicle is not compatible."
            )
            Assert.assertEquals(responseVehicle!!.vin, "1FDKE30G4JHA04964")
            Assert.assertEquals(responseVehicle.make, "FORD")
        }

        SmartcarAuth.receiveResponse(
            Uri.parse(
                redirectUri + "?error=vehicle_incompatible" +
                        "&error_description=The%20user%27s%20vehicle%20is%20not%20compatible." +
                        "&vin=1FDKE30G4JHA04964&make=FORD"
            ),
            redirectUri
        )
    }

    @Test
    fun smartcarAuth_receiveResponse_nullCodeWithMessage() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(
                smartcarResponse!!.errorDescription,
                "Unable to fetch code. Please try again"
            )
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?error_description=error"), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_codeWithState() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(smartcarResponse!!.code, "testCode")
            Assert.assertEquals(smartcarResponse.state, "testState")
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?code=testCode&state=testState"), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_codeWithUserId() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(smartcarResponse!!.code, "testCode")
            Assert.assertEquals(smartcarResponse.userId, "user-123")
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?code=testCode&user_id=user-123"), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_codeWithVirtualKeyUrl() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(smartcarResponse!!.code, "testCode")
            Assert.assertEquals(smartcarResponse.state, null)
            Assert.assertEquals(
                smartcarResponse.virtualKeyUrl,
                "https://www.tesla.com/_ak/smartcar.com"
            )
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?code=testCode&virtual_key_url=https://www.tesla.com/_ak/smartcar.com"), redirectUri)
    }

    @Test
    fun smartcarAuth_authUrlBuilder_responseTypeNoneWithRedirectUri() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val redirectUriEncoded = "scclient123%3A%2F%2Ftest"
        val scope = arrayOf("read_odometer", "read_vin")
        val expectedUri =
            "https://connect.smartcar.com/oauth/authorize?response_type=none" +
                    "&application_id=" + applicationId +
                    "&redirect_uri=" + redirectUriEncoded +
                    "&mode=live" +
                    "&scope=read_odometer%20read_vin" +
                    "&external_id=abc123"

        val smartcarAuth = SmartcarAuth(applicationId, redirectUri, scope, false, "none") {}
        val requestUri = smartcarAuth.authUrlBuilder()
            .setExternalId("abc123")
            .build()

        Assert.assertEquals(expectedUri, requestUri)
    }

    @Test
    fun smartcarAuth_authUrlBuilder_responseTypeNoneWithoutRedirectUri() {
        val applicationId = "client123"
        val scope = arrayOf("read_odometer", "read_vin")
        val expectedUri =
            "https://connect.smartcar.com/oauth/authorize?response_type=none" +
                    "&application_id=" + applicationId +
                    "&mode=live" +
                    "&scope=read_odometer%20read_vin" +
                    "&external_id=abc123"

        val smartcarAuth = SmartcarAuth(applicationId, null, scope, false, "none") {}
        val requestUri = smartcarAuth.authUrlBuilder()
            .setExternalId("abc123")
            .build()

        Assert.assertEquals(expectedUri, requestUri)
    }

    @Test(expected = IllegalArgumentException::class)
    fun smartcarAuth_invalidResponseType_throws() {
        SmartcarAuth("client123", "scclient123://test", emptyArray(), false, "bogus") {}
    }

    @Test(expected = IllegalArgumentException::class)
    fun smartcarAuth_codeResponseTypeWithoutRedirectUri_throws() {
        SmartcarAuth("client123", null, emptyArray(), false, "code") {}
    }

    @Test
    fun smartcarAuth_receiveResponse_noneFlow_successWithoutCode() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope, false, "none") { smartcarResponse ->
            Assert.assertEquals(smartcarResponse!!.code, null)
            Assert.assertEquals(smartcarResponse.error, null)
            Assert.assertEquals(smartcarResponse.userId, "user-123")
            Assert.assertEquals(smartcarResponse.externalId, "abc123")
        }

        SmartcarAuth.receiveResponse(
            Uri.parse("$redirectUri?user_id=user-123&external_id=abc123"),
            redirectUri
        )
    }

    @Test
    fun smartcarAuth_receiveResponse_codeFlow_noCodeNoError_stillReportsExistingError() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope) { smartcarResponse ->
            Assert.assertEquals(
                smartcarResponse!!.errorDescription,
                "Unable to fetch code. Please try again"
            )
        }

        SmartcarAuth.receiveResponse(Uri.parse("$redirectUri?external_id=abc123"), redirectUri)
    }

    @Test
    fun smartcarAuth_receiveResponse_externalIdOnError() {
        val applicationId = "client123"
        val redirectUri = "scclient123://test"
        val scope = arrayOf("read_odometer", "read_vin")

        SmartcarAuth(applicationId, redirectUri, scope, false, "none") { smartcarResponse ->
            Assert.assertEquals(smartcarResponse!!.error, "access_denied")
            Assert.assertEquals(smartcarResponse.externalId, "abc123")
        }

        SmartcarAuth.receiveResponse(
            Uri.parse("$redirectUri?error=access_denied&external_id=abc123"),
            redirectUri
        )
    }
}

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
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Helper.class})
public class SmartcarAuthMockedTest {

    @Test
    public void smartcarAuth_launchAuthFlow() {

        // Setup Mocks
        mockStatic(Helper.class);
        Context context = mock(Context.class);

        // Execute Method
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String authUrl = smartcarAuth.new AuthUrlBuilder().build();

        smartcarAuth.launchAuthFlow(context);

        // Verify Mocks
        verifyStatic(Helper.class, times(1));
        Helper.startActivity(context, authUrl);

    }

    @Test
    public void smartcarAuth_launchAuthFlow_withAuthUrl() {

        // Setup Mocks
        mockStatic(Helper.class);
        Context context = mock(Context.class);

        // Execute Method
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String authUrl = smartcarAuth.new AuthUrlBuilder()
            .setState("foo")
            .setMakeBypass("TESLA")
            .setSingleSelect(false)
            .build();

        smartcarAuth.launchAuthFlow(context, authUrl);

        // Verify Mocks
        verifyStatic(Helper.class, times(1));
        Helper.startActivity(context, authUrl);

    }

    @Test
    public void smartcarAuth_addClickHandler() {

        Context context = mock(Context.class);
        View view = mock(View.class);

        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);

        smartcarAuth.addClickHandler(context, view);

        Mockito.verify(view, times(1))
            .setOnClickListener(Mockito.any(View.OnClickListener.class));

    }

    @Test
    public void smartcarAuth_addClickHandler_withAuthUrl() {

        Context context = mock(Context.class);
        View view = mock(View.class);

        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String authUrl = smartcarAuth.new AuthUrlBuilder()
            .setState("foo")
            .setMakeBypass("TESLA")
            .setSingleSelect(false)
            .build();

        smartcarAuth.addClickHandler(context, view, authUrl);

        Mockito.verify(view, times(1))
                .setOnClickListener(Mockito.any(View.OnClickListener.class));

    }

}

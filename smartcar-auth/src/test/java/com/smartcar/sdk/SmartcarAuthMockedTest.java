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
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SmartcarAuth.class, TextUtils.class})
public class SmartcarAuthMockedTest {

    private final String clientId = "client123";
    private final String redirectUri = "scclient123://test";
    private final String[] scope = {"read_odometer", "read_vin"};

    @Before
    public void setUp() {
        mockStatic(TextUtils.class);
        when(TextUtils.join(" ", scope)).thenReturn("read_odometer read_vin");
    }

    @Test
    public void smartcarAuth_launchAuthFlow() throws Exception {

        // Setup mocks
        Context context = mock(Context.class);
        Intent intent = mock(Intent.class);
        whenNew(Intent.class)
                .withArguments(context, WebViewActivity.class)
                .thenReturn(intent);

        // Execute method
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        smartcarAuth.launchAuthFlow(context);

        // Verify mocks
        String expectedUrl = smartcarAuth.new AuthUrlBuilder().build();
        verify(intent, times(1))
                .putExtra("URI", expectedUrl);
        verify(intent, times(1))
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        verify(intent, times(1))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        verify(context, times(1))
                .startActivity(Mockito.any(Intent.class));

    }

    @Test
    public void smartcarAuth_launchAuthFlow_withAuthUrl() throws Exception {

        // Setup mocks
        Context context = mock(Context.class);
        Intent intent = mock(Intent.class);
        whenNew(Intent.class)
                .withArguments(context, WebViewActivity.class)
                .thenReturn(intent);

        // Execute method
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String authUrl = smartcarAuth.new AuthUrlBuilder()
                .setState("foo")
                .setMakeBypass("TESLA")
                .setSingleSelect(false)
                .build();
        smartcarAuth.launchAuthFlow(context, authUrl);

        // Verify mocks
        verify(intent, times(1))
                .putExtra("URI", authUrl);
        verify(intent, times(1))
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        verify(intent, times(1))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        verify(context, times(1))
                .startActivity(Mockito.any(Intent.class));

    }

    @Test
    public void smartcarAuth_addClickHandler() {

        // Setup mocks
        Context context = mock(Context.class);
        View view = mock(View.class);

        // Execute methods
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
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
        SmartcarAuth smartcarAuth = new SmartcarAuth(clientId, redirectUri, scope, null);
        String authUrl = smartcarAuth.new AuthUrlBuilder()
            .setState("foo")
            .setMakeBypass("TESLA")
            .setSingleSelect(false)
            .build();
        smartcarAuth.addClickHandler(context, view, authUrl);

        // Verify mocks
        verify(view, times(1))
                .setOnClickListener(Mockito.any(View.OnClickListener.class));

    }

}

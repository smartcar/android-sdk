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

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.LinearLayout;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation tests, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SmartcarAuthInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.smartcar.sdk.test", appContext.getPackageName());
    }

    @Test
    public void smartcarAuth_button() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        Context context = InstrumentationRegistry.getTargetContext();
        SmartcarAuth smartcarAuth = new SmartcarAuth(context, null, clientId, redirectUri, scope);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        OEM oem = OEM.AUDI;

        Button b = smartcarAuth.generateButton(oem, layoutParams);
        String buttonText = "Login with " + oem.getDisplayName();
        int buttonBgColor = Color.parseColor(oem.getColor());
        int buttonTextColor = Color.parseColor("#FFFFFF");
        int actualButtonTextColor = ((ColorDrawable) b.getBackground()).getColor();

        String imageName = oem.name().toLowerCase() + "_logo";
        int image = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        assertEquals(b.getText(), buttonText);
        assertEquals(b.getCurrentTextColor(), buttonTextColor);
        assertEquals(actualButtonTextColor, buttonBgColor);
        assertEquals(b.getLayoutParams(), layoutParams);
        assertEquals(b.getTag(), image);
    }
}

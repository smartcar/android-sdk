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
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

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
        int actualButtonBgColor = ((PaintDrawable) b.getBackground()).getPaint().getColor();

        String imageName = oem.getImageName();
        int image = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        assertEquals(b.getText(), buttonText);
        assertEquals(b.getCurrentTextColor(), buttonTextColor);
        assertEquals(actualButtonBgColor, buttonBgColor);
        assertEquals(b.getLayoutParams(), layoutParams);
        assertEquals(b.getTag(), image);
    }

    @Test
    public void smartcarAuth_spinner() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        Context context = InstrumentationRegistry.getTargetContext();
        SmartcarAuth smartcarAuth = new SmartcarAuth(context, null, clientId, redirectUri, scope);

        OEM[] oemList = {OEM.AUDI, OEM.BUICK, OEM.DODGE, OEM.GMC, OEM.FIAT, OEM.NISSANEV};

        Looper.prepare(); // Create the message queue
        Spinner s = smartcarAuth.generateSpinner(oemList);
        assertEquals(s.getAdapter().getCount(), oemList.length+1);
        assertEquals(s.getAdapter().getItem(1).toString(), OEM.AUDI.getDisplayName());
        assertEquals(s.getAdapter().getItem(2).toString(), OEM.BUICK.getDisplayName());
        assertEquals(s.getAdapter().getItem(3).toString(), OEM.DODGE.getDisplayName());
        assertEquals(s.getAdapter().getItem(4).toString(), OEM.GMC.getDisplayName());
        assertEquals(s.getAdapter().getItem(5).toString(), OEM.FIAT.getDisplayName());
        assertEquals(s.getAdapter().getItem(6).toString(), OEM.NISSANEV.getDisplayName());
    }

    @Test
    public void smartcarAuth_spinnerAll() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        Context context = InstrumentationRegistry.getTargetContext();
        SmartcarAuth smartcarAuth = new SmartcarAuth(context, null, clientId, redirectUri, scope);

        Spinner s = smartcarAuth.generateSpinner();
        int listSize = OEM.values().length;
        assertEquals(s.getAdapter().getCount(), listSize);
        assertEquals(s.getAdapter().getItem(1).toString(), OEM.ACURA.getDisplayName());
        assertEquals(s.getAdapter().getItem(listSize-1).toString(), OEM.VOLVO.getDisplayName());
    }

    @Test
    public void smartcarAuth_spinnerAllMock() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        Context context = InstrumentationRegistry.getTargetContext();
        SmartcarAuth smartcarAuth = new SmartcarAuth(context, null, clientId, redirectUri, scope);

        Spinner s = smartcarAuth.generateSpinner(true);
        int listSize = OEM.values().length+1;
        assertEquals(s.getAdapter().getCount(), listSize);
        assertEquals(s.getAdapter().getItem(1).toString(), OEM.ACURA.getDisplayName());
        assertEquals(s.getAdapter().getItem(listSize-1).toString(), OEM.VOLVO.getDisplayName());
    }
}

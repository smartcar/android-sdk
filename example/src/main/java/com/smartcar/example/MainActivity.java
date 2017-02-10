/**
 * Copyright (c) 2017-present, Smartcar, Inc. All rights reserved.

 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Smartcar.
 *
 * As with any software that integrates with the Smartcar platform, your use of
 * this software is subject to the Smartcar Developer Principles and Policies
 * [http://developer.smartcar.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.smartcar.example;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.smartcar.sdk.OEM;
import com.smartcar.sdk.SmartcarAuth;
import com.smartcar.sdk.SmartcarCallback;
import com.smartcar.sdk.SmartcarResponse;

public class MainActivity extends AppCompatActivity {

    private static String CLIENT_ID;
    private static String REDIRECT_URI;
    private static String SCOPE;
    private Context appContext;
    private SmartcarAuth smartcarAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appContext = getApplicationContext();
        CLIENT_ID = getString(R.string.client_id);
        REDIRECT_URI = getString(R.string.redirect_uri);
        SCOPE = getString(R.string.scope);

        MyCallback callback = new MyCallback();

        smartcarAuth = new SmartcarAuth(appContext, callback, CLIENT_ID, REDIRECT_URI, SCOPE);

        // Add buttons
        LinearLayout linear = (LinearLayout) findViewById(R.id.activity_main);
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);

        Button mockButton = smartcarAuth.generateButton(OEM.MOCK, param);
        linear.addView(mockButton);

        Button audiButton = smartcarAuth.generateButton(OEM.AUDI, param);
        linear.addView(audiButton);

        Button acuraButton = smartcarAuth.generateButton(OEM.ACURA, param);
        linear.addView(acuraButton);

        Button volvoButton = smartcarAuth.generateButton(OEM.VOLVO, param);
        linear.addView(volvoButton);
    }

    /**
     * Process the callback from Smartcar SDK
     */
    class MyCallback implements SmartcarCallback {
        public void handleResponse(SmartcarResponse smartcarResponse) {
            Log.d("Response code ", smartcarResponse.getCode());
            if (smartcarResponse.getMessage() != null)
                Log.d("Response message ", smartcarResponse.getMessage());
        }
    }
}
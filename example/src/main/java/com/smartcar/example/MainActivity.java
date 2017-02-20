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

package com.smartcar.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;

import com.smartcar.sdk.OEM;
import com.smartcar.sdk.SmartcarAuth;
import com.smartcar.sdk.SmartcarCallback;
import com.smartcar.sdk.SmartcarResponse;

public class MainActivity extends AppCompatActivity {

    private static String LOGTAG = "ExampleApp";
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

        LinearLayout linear = (LinearLayout) findViewById(R.id.activity_main);
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);

        smartcarAuth = new SmartcarAuth(appContext, callback, CLIENT_ID, REDIRECT_URI, SCOPE);

        // Add buttons
        Button mockButton = smartcarAuth.generateButton(OEM.MOCK, param);
        linear.addView(mockButton);

        Button audiButton = smartcarAuth.generateButton(OEM.AUDI, param);
        linear.addView(audiButton);

        Button acuraButton = smartcarAuth.generateButton(OEM.ACURA, param);
        linear.addView(acuraButton);

        Button bmwConnected = smartcarAuth.generateButton(OEM.BMW_CONNECTED, param);
        linear.addView(bmwConnected);

        Button volvoButton = smartcarAuth.generateButton(OEM.VOLVO, param);
        linear.addView(volvoButton);

        //OEM[] oemList = {OEM.BMW, OEM.MOCK, OEM.VOLVO, OEM.VOLKSWAGEN};
        //Spinner oemSpinner = smartcarAuth.generateSpinner(oemList);
        Spinner oemSpinner = smartcarAuth.generateSpinner();
        linear.addView(oemSpinner);
    }

    /**
     * Process the callback from Smartcar SDK
     */
    class MyCallback implements SmartcarCallback {
        public void handleResponse(SmartcarResponse smartcarResponse) {
            String code = smartcarResponse.getCode();
            String message = smartcarResponse.getMessage();
            String response = (code == null) ? message : code;
            Log.d(LOGTAG, "Response code " + code);
            Log.d(LOGTAG, "Response message " + message);
            Intent intent = new Intent(appContext, DisplayCodeActivity.class);
            intent.putExtra("RESPONSE", response);
            startActivity(intent);
        }
    }
}
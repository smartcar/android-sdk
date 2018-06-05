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

package com.smartcar.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.smartcar.sdk.ApprovalPrompt;
import com.smartcar.sdk.SmartcarAuth;
import com.smartcar.sdk.SmartcarCallback;
import com.smartcar.sdk.SmartcarResponse;

/** A simple Activity to showcase the Smartcar Auth SDK.
 */
public class MainActivity extends AppCompatActivity {

    private static String LOGTAG = "SmartDeliveryService";
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

        // Step 1. Always create a SmartcarCallback object, and a SmartcarAuth object
        MyCallback callback = new MyCallback();
        smartcarAuth = new SmartcarAuth(callback, CLIENT_ID, REDIRECT_URI, SCOPE,
                ApprovalPrompt.force,true);

        // Step 2. Add a button - A click event on this button will launch the Smartcar Auth flow
        Button connectButton = (Button) findViewById(R.id.connect_button);

        // Step 3. Attach the click handler provided by the SDK, as shown below
        smartcarAuth.addClickHandler(appContext, connectButton);

        // Alternate to Steps 2 & 3
        // Attach a gesture listener to a button and use the Smartcar launchAuthFlow() method
        Button altButton = (Button) findViewById(R.id.alt_button);

        altButton.setOnTouchListener(new OnSwipeTouchListener(appContext) {
            public void onSwipeTop() {
                smartcarAuth.launchAuthFlow(appContext);
            }
            public void onSwipeRight() {
                smartcarAuth.launchAuthFlow(appContext);
            }
            public void onSwipeLeft() {
                smartcarAuth.launchAuthFlow(appContext);
            }
            public void onSwipeBottom() {
                smartcarAuth.launchAuthFlow(appContext);
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    /**
     * Process the callback from the Smartcar SDK
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
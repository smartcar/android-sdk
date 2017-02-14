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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Provides methods to create a button, and attach a click handler to it.
 */
class SmartcarAuthButtonGenerator {
    /**
     * Generates a button with the appropriate background color and padding, and attaches a click
     * listener, for a given OEM.
     *
     * @param context             The application's context
     * @param smartcarAuthRequest The SmartcarAuthRequest object
     * @param oem                 The OEM to generate the button for
     * @param layoutParams        The Layout Params for the button
     * @return                    The generated Button
     */
    protected static Button generateButton(Context context,
                                           SmartcarAuthRequest smartcarAuthRequest,
                                           OEM oem,
                                           LinearLayout.LayoutParams layoutParams) {
        String buttonText = String.format(context.getResources().getString(R.string.button_prefix), oem.getDisplayName());
        Button b = new Button(context);
        b.setTransformationMethod(null);
        b.setTextColor(Color.parseColor(context.getResources().getString(R.string.text_color)));
        b.setText(buttonText);
        b.setBackgroundColor(Color.parseColor(oem.getColor()));
        b.setTextSize(30);
        int image = context.getResources().getIdentifier(oem.getImageName(), "drawable", context.getPackageName());
        Drawable logo = ContextCompat.getDrawable(context, image);
        logo.setBounds(0, 0, 120, 120);
        b.setCompoundDrawables(logo, null, null, null);
        b.setTag(image);
        b.setLayoutParams(layoutParams);
        b.setPadding(16, 10, 36, 16);

        b.setOnClickListener(handleOnClick(context, smartcarAuthRequest, oem));
        return b;
    }

    /**
     * OnClick event handler for a given button.
     *
     * @param context             The application's context
     * @param smartcarAuthRequest The SmartcarAuthRequest object
     * @param oem                 The OEM linked to the Button
     * @return                    The callback to be invoked when the Button is clicked
     */
    protected static View.OnClickListener handleOnClick(final Context context,
                                                        final SmartcarAuthRequest smartcarAuthRequest,
                                                        final OEM oem) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String requestUri = smartcarAuthRequest.generateAuthRequestUri(oem);

                Log.d("Auth request URI ", requestUri);
                Intent loginIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestUri));
                context.startActivity(loginIntent);
            }
        };
    }
}

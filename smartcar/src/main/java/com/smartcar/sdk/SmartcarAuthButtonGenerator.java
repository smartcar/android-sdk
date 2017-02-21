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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
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
    static Button generateButton(Context context,
                                 SmartcarAuthRequest smartcarAuthRequest,
                                 OEM oem,
                                 LinearLayout.LayoutParams layoutParams) {
        String buttonText = String.format(context.getResources().getString(R.string.button_prefix), oem.getDisplayName());
        Button b = new Button(context);
        // To turn off upper case text
        b.setTransformationMethod(null);
        b.setTextColor(Color.parseColor(context.getResources().getString(R.string.button_text_color)));
        b.setText(buttonText);
        b.setBackgroundColor(Color.parseColor(oem.getColor()));
        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(context.getResources().getString(R.string.button_text_size)));
        int image = context.getResources().getIdentifier(oem.getImageName(), "drawable", context.getPackageName());
        Drawable logo = ContextCompat.getDrawable(context, image);
        logo.setBounds(0, 0, logo.getIntrinsicWidth(), logo.getIntrinsicHeight());
        b.setCompoundDrawables(logo, null, null, null);
        // Setting tag as a means for testing the image
        b.setTag(image);
        b.setLayoutParams(layoutParams);

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
    private static View.OnClickListener handleOnClick(final Context context,
                                                      final SmartcarAuthRequest smartcarAuthRequest,
                                                      final OEM oem) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String requestUri = smartcarAuthRequest.generateAuthRequestUri(oem);

                Log.d("Auth request URI ", requestUri);
                Intent intent = new Intent(context, com.smartcar.sdk.WebViewActivity.class);
                intent.putExtra("URI", requestUri);
                // The new activity (web view) will not be in the history stack
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
    }
}
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v4.content.ContextCompat;
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
        Button button = new Button(context);
        setButtonParameters(context, button, oem);
        button.setLayoutParams(layoutParams);
        button.setOnClickListener(handleOnClick(context, smartcarAuthRequest, oem));
        return button;
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
                Helper.startActivity(context, smartcarAuthRequest, oem);
            }
        };
    }

    /**
     * OnClick event handler.
     *
     * @param smartcarAuth The SmartcarAuth object
     * @return             The callback to be invoked when the event occurs
     */
    protected static View.OnClickListener handleOnClick(final SmartcarAuth smartcarAuth) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                SmartcarAuthRequest smartcarAuthRequest = smartcarAuth.smartcarAuthRequest;
                OemLoginButton button = (OemLoginButton)v;
                OEM oem = button.getOem();
                Context context = smartcarAuth.getContext();

                Helper.startActivity(context, smartcarAuthRequest, oem);
            }
        };
    }

    /**
     * Method to abstract out the Button image, text and background color settings.
     *
     * @param context The application's context
     * @param button  The Button object
     * @param oem     The OEM linked to the Button
     */
    protected static void setButtonParameters(Context context, Button button, OEM oem) {
        String buttonText = String.format(context.getResources().getString(R.string.button_prefix), oem.getDisplayName());
        // To turn off upper case text
        button.setTransformationMethod(null);
        button.setTextColor(Color.parseColor(context.getResources().getString(R.string.button_text_color)));
        button.setText(buttonText);
        Float textSize = Float.parseFloat(context.getResources().getString(R.string.button_text_size));
        PaintDrawable shape = new PaintDrawable(Color.parseColor(oem.getColor()));
        shape.setCornerRadius((float) (textSize));
        button.setBackground(shape);
        button.setPadding(10, 10, 10, 10);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int image = context.getResources().getIdentifier(oem.getImageName(), "drawable", context.getPackageName());
        Drawable logo = ContextCompat.getDrawable(context, image);
        logo.setBounds(0, 0, logo.getIntrinsicWidth(), logo.getIntrinsicHeight());
        button.setCompoundDrawablePadding(5);
        button.setCompoundDrawables(logo, null, null, null);
        // Setting tag as a means for testing the image
        button.setTag(image);
    }
}
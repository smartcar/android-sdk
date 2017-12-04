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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Class that provides a Button class to be used via XML.
 */
public class OemLoginButton extends Button {

    private OEM oem;

    public void setOem(OEM oem) {
        this.oem = oem;
    }

    public OEM getOem() {
        return oem;
    }

    public OemLoginButton(Context context) {
        super(context);
        init(context, null);
    }

    public OemLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OemLoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OemLoginButton, 0, 0);
            try {
                // Setting default OEM value to 20 ==> OEM.MOCK
                OEM oem = OEM.values()[a.getInt(R.styleable.OemLoginButton_OEM, 20)];
                this.oem = oem;
                SmartcarAuthButtonGenerator.setButtonParameters(context, this, oem);
            } finally {
                a.recycle();
            }
        }
    }
}
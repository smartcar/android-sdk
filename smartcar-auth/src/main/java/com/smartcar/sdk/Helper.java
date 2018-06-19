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
import android.util.Log;

class Helper {
    /**
     * Helper method that starts the WebView as a new Activity.
     *
     * @param context The application's context
     * @param url     The Smartcar Auth url
     */
    protected static void startActivity(Context context, String url) {
        Log.d("Auth request URI ", url);
        Intent intent = new Intent(context, com.smartcar.sdk.WebViewActivity.class);
        intent.putExtra("URI", url);
        // The new activity (web view) will not be in the history stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Helper method to check if a String starts with the intended redirect URI.
     *
     * @param response The String to check against the redirect URI
     * @return         True if the String matched. False otherwise
     */
    protected static Boolean matchesRedirectUri(String response) {
        return (response.startsWith(SmartcarAuth.smartcarAuthRequest.getRedirectURI()));
    }

    /**
     * Helper method to convert an array of Strings to a space-separated string
     *
     * @param array The array of Strings to flatten
     * @return      The flattened string
     */
    protected static String arrayToString(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            if (i > 0) stringBuilder.append(" ");
            stringBuilder.append(array[i]);
        }
        return stringBuilder.toString();
    }
}

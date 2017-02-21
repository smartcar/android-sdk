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
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

class SmartcarAuthSpinnerGenerator {

    private static SparseArray<String> itemMap;

    static Spinner generateSpinner(
            Context context,
            SmartcarAuthRequest smartcarAuthRequest,
            OEM[] oemList) {
        // Using a SparseArray to maintain a mapping between OEM displayName and name so its easy
        //  to identify the OEM for setting up the request URI
        itemMap = new SparseArray<>();
        String[] itemList = new String[oemList.length+1];
        itemMap.put(0, null);
        // Create a placeholder text for the first item in the spinner
        itemList[0] = context.getResources().getString(R.string.spinner_item_0);
        for(int i=0; i<oemList.length; i++) {
            itemMap.put(i+1, oemList[i].name());
            itemList[i+1] = oemList[i].getDisplayName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(handleOnItemSelected(context, smartcarAuthRequest));
        return spinner;
    }

    private static AdapterView.OnItemSelectedListener handleOnItemSelected(
            final Context context,
            final SmartcarAuthRequest smartcarAuthRequest) {
        return new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) return;
                String requestUri = smartcarAuthRequest.generateAuthRequestUri(OEM.valueOf(itemMap.get(pos)));

                Log.d("Auth request URI ", requestUri);
                Intent intent = new Intent(context, com.smartcar.sdk.WebViewActivity.class);
                intent.putExtra("URI", requestUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            public void onNothingSelected(AdapterView<?> arg) {
            }
        };
    }
}
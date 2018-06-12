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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HelperTest {

    @Test
    public void helper_matchesRedirectUri() throws Exception {
        String clientId = "client123";
        String redirectUri = "scclient123://test";
        String scope = "read_odometer read_vin";
        String matchedResponse = "scclient123://test?code=democode456&state=demostate789";
        String unmatchedResponse = "sctestclient123://test?code=democode456&state=demostate789";
        new SmartcarAuth(clientId, redirectUri, scope, null);
        assertTrue(Helper.matchesRedirectUri(matchedResponse));
        assertFalse(Helper.matchesRedirectUri(unmatchedResponse));
    }

    @Test
    public void helper_arrayToString_success() throws Exception {
        String[] inputArray = {"string1", "string2", "string3"};
        String expectedString = "string1 string2 string3";
        String retString = Helper.arrayToString(inputArray);
        assertEquals(retString, expectedString);
    }

    @Test
    public void helper_arrayToString_noData() throws Exception {
        String[] inputArray = {};
        String expectedString = "";
        String retString = Helper.arrayToString(inputArray);
        assertEquals(retString, expectedString);
    }
}

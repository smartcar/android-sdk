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

public class SmartcarResponseTest {

    @Test
    public void SmartcarResponseTest_code() {
        SmartcarResponse smartcarResponse = new SmartcarResponse("Just a testmessage", "teststate");
        smartcarResponse.setCode("testcode");

        assertEquals(smartcarResponse.getCode(), "testcode");
        assertEquals(smartcarResponse.getMessage(), "Just a testmessage");
        assertEquals(smartcarResponse.getState(), "teststate");
    }

    @Test
    public void SmartcarResponseTest_error() {
        SmartcarResponse smartcarResponse = new SmartcarResponse("Error message", "errorstate");
        smartcarResponse.setError("error");

        assertEquals(smartcarResponse.getError(), "error");
        assertEquals(smartcarResponse.getMessage(), "Error message");
        assertEquals(smartcarResponse.getState(), "errorstate");
    }

    @Test
    public void SmartcarResponseTest_errorWithVehicle() {
        VehicleResponse vehicle = new VehicleResponse("00000", "TESLA", "Model S", "2019");
        SmartcarResponse smartcarResponse = new SmartcarResponse("error", "Error message", "errorstate", vehicle);

        assertEquals(smartcarResponse.getError(), "error");
        assertEquals(smartcarResponse.getMessage(), "Error message");
        assertEquals(smartcarResponse.getState(), "errorstate");
        assertEquals(smartcarResponse.getVehicle(), vehicle);
    }

}
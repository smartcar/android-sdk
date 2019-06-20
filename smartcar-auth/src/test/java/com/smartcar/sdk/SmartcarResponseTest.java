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
        SmartcarResponse smartcarResponse = new SmartcarResponse.Builder()
                .code("testcode")
                .message("Just a testmessage")
                .state("teststate")
                .build();

        assertEquals(smartcarResponse.getCode(), "testcode");
        assertEquals(smartcarResponse.getMessage(), "Just a testmessage");
        assertEquals(smartcarResponse.getState(), "teststate");
    }

    @Test
    public void SmartcarResponseTest_error() {
        SmartcarResponse smartcarResponse = new SmartcarResponse.Builder()
                .error("error")
                .message("Error message")
                .state("errorstate")
                .build();

        assertEquals(smartcarResponse.getError(), "error");
        assertEquals(smartcarResponse.getMessage(), "Error message");
        assertEquals(smartcarResponse.getState(), "errorstate");
    }

    @Test
    public void SmartcarResponseTest_errorWithVehicle() {
        VehicleInfo vehicle = new VehicleInfo.Builder()
                .setVin("0000")
                .setMake("TESLA")
                .setModel("Model S")
                .setYear(2019)
                .build();

        SmartcarResponse smartcarResponse = new SmartcarResponse.Builder()
                .error("error")
                .message("Error message")
                .state("errorstate")
                .vehicleInfo(vehicle)
                .build();

        assertEquals(smartcarResponse.getError(), "error");
        assertEquals(smartcarResponse.getMessage(), "Error message");
        assertEquals(smartcarResponse.getState(), "errorstate");
        assertEquals(smartcarResponse.getVehicleInfo(), vehicle);
    }

}

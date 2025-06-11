/**
 * Copyright (c) 2017-present, Smartcar, Inc. All rights reserved.
 *
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
package com.smartcar.sdk

import org.junit.Assert
import org.junit.Test

class SmartcarResponseTest {
    @Test
    fun SmartcarResponseTest_code() {
        val smartcarResponse = SmartcarResponse.Builder()
            .code("testcode")
            .errorDescription("Just a testmessage")
            .state("teststate")
            .virtualKeyUrl("https://www.tesla.com/_ak/smartcar.com")
            .build()

        Assert.assertEquals(smartcarResponse.code, "testcode")
        Assert.assertEquals(smartcarResponse.errorDescription, "Just a testmessage")
        Assert.assertEquals(smartcarResponse.state, "teststate")
        Assert.assertEquals(
            smartcarResponse.virtualKeyUrl,
            "https://www.tesla.com/_ak/smartcar.com"
        )
    }

    @Test
    fun SmartcarResponseTest_error() {
        val smartcarResponse = SmartcarResponse.Builder()
            .error("error")
            .errorDescription("Error message")
            .state("errorstate")
            .build()

        Assert.assertEquals(smartcarResponse.error, "error")
        Assert.assertEquals(smartcarResponse.errorDescription, "Error message")
        Assert.assertEquals(smartcarResponse.state, "errorstate")
    }

    @Test
    fun SmartcarResponseTest_errorWithVehicle() {
        val vehicle = VehicleInfo.Builder()
            .vin("0000")
            .make("TESLA")
            .build()

        val smartcarResponse = SmartcarResponse.Builder()
            .error("error")
            .errorDescription("Error message")
            .state("errorstate")
            .vehicleInfo(vehicle)
            .build()

        Assert.assertEquals(smartcarResponse.error, "error")
        Assert.assertEquals(smartcarResponse.errorDescription, "Error message")
        Assert.assertEquals(smartcarResponse.state, "errorstate")
        Assert.assertEquals(smartcarResponse.vehicleInfo, vehicle)
    }
}

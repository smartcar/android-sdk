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

/**
 * Class that encompasses the response code and error message, if any.
 */
public class SmartcarResponse {
    private String code;
    private String error;
    private String message;
    private String state;
    private VehicleResponse vehicle;

    public SmartcarResponse(String message, String state) {
        this.message = message;
        this.state = state;
    }

    public SmartcarResponse(String error, String message, String state, VehicleResponse vehicle) {
        this.error = error;
        this.message = message;
        this.state = state;
        this.vehicle = vehicle;
    }

    public String getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }

    public VehicleResponse getVehicle() {
        return vehicle;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setError(String error) {
        this.error = error;
    }

}
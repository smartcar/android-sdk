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
 * A class that creates the response from Smartcar Connect.
 */
public class SmartcarResponse {
    private String code;
    private String error;
    private String message;
    private String state;
    private VehicleInfo vehicleInfo;

    /**
     * Assigns properties on the SmartcarResponse object.
     *
     * @param builder the builder to obtain the properties from
     */
    private SmartcarResponse(Builder builder) {
        this.code = builder.code;
        this.error = builder.error;
        this.message = builder.message;
        this.state = builder.state;
        this.vehicleInfo = builder.vehicleInfo;
    }

    public String getCode() {
        return this.code;
    }

    public String getError() {
        return this.error;
    }

    public String getMessage() {
        return this.message;
    }

    public String getState() {
        return this.state;
    }

    public VehicleInfo getVehicleInfo() { return this.vehicleInfo; }

    public static class Builder {
        private String code;
        private String error;
        private String message;
        private String state;
        private VehicleInfo vehicleInfo;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder vehicleInfo(VehicleInfo vehicle) {
            this.vehicleInfo = vehicle;
            return this;
        }

        /**
         * Instantiates a new SmartcarResponse object, which will also have any optional properties
         * that are already set on the Builder object that is calling this method.
         *
         * @return a new instantiation of the SmartcarResponse class
         */
        public SmartcarResponse build() {
            return new SmartcarResponse(this);
        }
    }
}

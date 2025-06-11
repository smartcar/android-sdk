package com.smartcar.sdk

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

/**
 * A class that creates the response from Smartcar Connect.
 */
class SmartcarResponse private constructor(
    val code: String?,
    val error: String?,
    val errorDescription: String?,
    val state: String?,
    val vehicleInfo: VehicleInfo?,
    val virtualKeyUrl: String?
) {

    override fun toString(): String {
        return "SmartcarResponse{" +
                "code='" + code + '\'' +
                ", error='" + error + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", state='" + state + '\'' +
                ", vehicleInfo=" + vehicleInfo + '\'' +
                ", virtualKeyUrl=" + virtualKeyUrl +
                '}'
    }

    class Builder {
        private var code: String? = null
        private var error: String? = null
        private var errorDescription: String? = null
        private var state: String? = null
        private var vehicleInfo: VehicleInfo? = null
        private var virtualKeyUrl: String? = null

        fun errorDescription(errorDescription: String?): Builder {
            this.errorDescription = errorDescription
            return this
        }

        fun state(state: String?): Builder {
            this.state = state
            return this
        }

        fun code(code: String?): Builder {
            this.code = code
            return this
        }

        fun error(error: String?): Builder {
            this.error = error
            return this
        }

        fun vehicleInfo(vehicle: VehicleInfo?): Builder {
            this.vehicleInfo = vehicle
            return this
        }

        fun virtualKeyUrl(virtualKeyUrl: String?): Builder {
            this.virtualKeyUrl = virtualKeyUrl
            return this
        }

        /**
         * Instantiates a new SmartcarResponse object, which will also have any optional properties
         * that are already set on the Builder object that is calling this method.
         *
         * @return a new instantiation of the SmartcarResponse class
         */
        fun build(): SmartcarResponse {
            return SmartcarResponse(code, error, errorDescription, state, vehicleInfo, virtualKeyUrl)
        }
    }
}

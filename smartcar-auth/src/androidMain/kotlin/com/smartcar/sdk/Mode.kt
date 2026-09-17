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
 * The mode Smartcar Connect is launched in.
 *
 * @property value The string sent as the `mode` query parameter on the Connect authorization URL
 */
enum class Mode(internal val value: String) {
    /** Launches Connect against real vehicles. This is the default. */
    LIVE("live"),

    /** Launches Connect against Smartcar's simulated vehicles, for testing without a real vehicle. */
    SIMULATED("simulated"),

    /**
     * Launches Connect in test mode.
     *
     * @deprecated Use [SIMULATED] instead.
     */
    @Deprecated("Use Mode.SIMULATED instead.", ReplaceWith("Mode.SIMULATED"))
    TEST("test")
}

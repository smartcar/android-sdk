/**
 * Copyright (c) 2017-present, Smartcar, Inc. All rights reserved.

 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Smartcar.
 *
 * As with any software that integrates with the Smartcar platform, your use of
 * this software is subject to the Smartcar Developer Agreement
 * [https://developer.smartcar.com/agreement/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
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
 * Enum that maintains the list of OEMs and their parameters.
 */
public enum OEM {

    ACURA("Acura", "acura","#020202"),
    AUDI("Audi", "audi", "#000000"),
    BMW("BMW", "bmw", "#2E9BDA"),
    BMW_CONNECTED("BMW Connected", "bmw-connected", "#2E9BDA"),
    BUICK("Buick", "buick", "#333333"),
    CADILLAC("cadillac", "cadillac", "#941711"),
    CHEVROLET("Chevrolet", "chevrolet", "#042F6B"),
    CHRYSLER("Chrysler", "chrysler", "#231F20"),
    DODGE("Dodge", "dodge", "#000000"),
    FORD("Ford", "ford", "#003399"),
    FIAT("Fiat", "fiat", "#B50536"),
    GMC("GMC", "gmc", "#CC0033"),
    HYUNDAI("Hyundai", "hyundai", "#00287A"),
    INFINITI("Infiniti", "infiniti", "#1F1F1F"),
    JEEP("Jeep", "jeep", "#374B00"),
    KIA("KIA", "kia", "#C4172C"),
    LANDROVER("Land Rover", "landrover", "#005A2B"),
    LEXUS("Lexus", "lexus", "#5B7F95"),
    NISSAN("Nissan", "nissan", "#C71444"),
    NISSANEV("Nissan EV", "nissanev", "#C71444"),
    RAM("RAM", "ram", "#000000"),
    TESLA("Tesla", "tesla", "#CC0000"),
    VOLKSWAGEN("Volkswagen", "volkswagen", "#000000"),
    VOLVO("Volvo", "volvo", "#000F60"),
    MERCEDES("Mercedes", "mercedes", "#222222"),
    MOCK("Mock", "mock", "#495F5D");

    private String displayName;
    private String authName;
    private String color;

    /**
     * Create a new OEM with the given parameters.
     *
     * @param displayName The name used for display purposes
     * @param authName    The name used in the authentication URI
     * @param color       The background color specific to the OEM
     */
    OEM(String displayName, String authName, String color) {
        this.displayName = displayName;
        this.authName = authName;
        this.color = color;
    }

    /**
     * Gets the OEM's name used for display purposes.
     *
     * @return The OEM's name used for display purposes
     */
    protected String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the OEM's name for use in the authentication URI.
     *
     * @return The OEM's name used in the authentication URI
     */
    protected String getAuthName() { return authName; }

    /**
     * Gets the OEM's defined color.
     *
     * @return The OEM's color specification, in hex code
     */
    protected String getColor() {
        return color;
    }
}


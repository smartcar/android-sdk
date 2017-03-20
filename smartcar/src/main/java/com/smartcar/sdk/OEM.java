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
 * Enum that maintains the list of OEMs and their parameters.
 */
public enum OEM {

    ACURA("Acura", "acura_logo", "https://acura.smartcar.com","#020202"),
    AUDI("Audi", "audi_logo", "https://audi.smartcar.com", "#000000"),
    BMW("BMW", "bmw_logo", "https://bmw.smartcar.com", "#2E9BDA"),
    BMW_CONNECTED("BMW Connected", "bmw_logo", "https://bmw-connected.smartcar.com", "#2E9BDA"),
    BUICK("Buick", "buick_logo", "https://buick.smartcar.com", "#333333"),
    CADILLAC("Cadillac", "cadillac_logo", "https://cadillac.smartcar.com", "#941711"),
    CHEVROLET("Chevrolet", "chevrolet_logo", "https://chevrolet.smartcar.com", "#042F6B"),
    CHRYSLER("Chrysler", "chrysler_logo", "https://chrysler.smartcar.com", "#231F20"),
    DODGE("Dodge", "dodge_logo", "https://dodge.smartcar.com", "#000000"),
    FIAT("Fiat", "fiat_logo", "https://fiat.smartcar.com", "#B50536"),
    FORD("Ford", "ford_logo", "https://ford.smartcar.com", "#003399"),
    GMC("GMC", "gmc_logo", "https://gmc.smartcar.com", "#CC0033"),
    HYUNDAI("Hyundai", "hyundai_logo", "https://hyundai.smartcar.com", "#00287A"),
    INFINITI("Infiniti", "infiniti_logo", "https://infiniti.smartcar.com", "#1F1F1F"),
    JEEP("Jeep", "jeep_logo", "https://jeep.smartcar.com", "#374B00"),
    KIA("KIA", "kia_logo", "https://kia.smartcar.com", "#C4172C"),
    LANDROVER("Land Rover", "landrover_logo", "https://landrover.smartcar.com", "#005A2B"),
    LEXUS("Lexus", "lexus_logo", "https://lexus.smartcar.com", "#5B7F95"),
    MERCEDES("Mercedes", "mercedes_logo", "https://mercedes.smartcar.com", "#222222"),
    MOCK("Mock", "mock_logo", "https://mock.smartcar.com", "#495F5D"),
    NISSAN("Nissan", "nissan_logo", "https://nissan.smartcar.com", "#C71444"),
    NISSANEV("Nissan EV", "nissan_logo", "https://nissanev.smartcar.com", "#C71444"),
    RAM("RAM", "ram_logo", "https://ram.smartcar.com", "#000000"),
    TESLA("Tesla", "tesla_logo", "https://tesla.smartcar.com", "#CC0000"),
    VOLKSWAGEN("Volkswagen", "volkswagen_logo", "https://volkswagen.smartcar.com", "#000000"),
    VOLVO("Volvo", "volvo_logo", "https://volvo.smartcar.com", "#000F60");

    private String displayName;
    private String imageName;
    private String authUrl;
    private String color;

    /**
     * Create a new OEM with the given parameters.
     *
     * @param displayName The name used for display purposes
     * @param imageName   The name of the image used for display purposes
     * @param authUrl     The URL used for authentication
     * @param color       The background color specific to the OEM
     */
    OEM(String displayName, String imageName, String authUrl, String color) {
        this.displayName = displayName;
        this.imageName = imageName;
        this.authUrl = authUrl;
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
     * Gets the OEM's image name.
     *
     * @return The OEM's image name
     */
    protected String getImageName() { return imageName; }
    /**
     * Gets the OEM's authentication URL.
     *
     * @return The OEM's authentication URL
     */
    protected String getAuthUrl() { return authUrl; }

    /**
     * Gets the OEM's defined color.
     *
     * @return The OEM's color specification, in hex code
     */
    protected String getColor() {
        return color;
    }
}


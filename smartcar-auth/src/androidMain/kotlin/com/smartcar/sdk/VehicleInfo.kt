package com.smartcar.sdk

/**
 * A class that stores vehicle data returned in {@link SmartcarResponse}.
 */
class VehicleInfo private constructor(builder: Builder) {

    val vin: String? = builder.vin
    val make: String? = builder.make

    override fun toString(): String {
        return "VehicleInfo{" +
                "vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                '}'
    }

    /**
     * Builder class that allows for optional properties that can be null on VehicleInfo
     */
    class Builder {
        internal var vin: String? = null
        internal var make: String? = null

        /**
         * Sets the make on the Builder. Including a make allows the user to bypass the car brand
         * selection screen.
         *
         * @param make name of the make of a vehicle.
         *
         * @return the builder with a `make` property added
         */
        fun make(make: String?): Builder {
            this.make = make
            return this
        }

        /**
         * Sets the vin on the Builder.
         *
         * @param vin vin of the vehicle
         *
         * @return the builder with a `vin` property added
         */
        fun vin(vin: String?): Builder {
            this.vin = vin
            return this
        }

        /**
         * Instantiates a new VehicleInfo object, which will also have any optional properties
         * that are already set on the Builder object that is calling this method.
         *
         * @return a new instantiation of the VehicleInfo class
         */
        fun build(): VehicleInfo {
            return VehicleInfo(this)
        }
    }
}

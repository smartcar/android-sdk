package com.smartcar.sdk;

/**
 * A class that stores vehicle data returned in {@link SmartcarResponse}.
 */
public class VehicleInfo {
    private String vin;
    private String make;

    /**
     * Assigns properties on the VehicleInfo object.
     *
     * @param builder the builder to obtain the properties from
     */
    private VehicleInfo (Builder builder) {
        this.vin = builder.vin;
        this.make = builder.make;
    }

    /**
     * Returns the make assigned to VehicleInfo
     *
     * @return the make of the vehicle
     */
    public String getMake() {
        return this.make;
    }

    /**
     * Returns the vin assigned to VehicleInfo
     *
     * @return the vin of the vehicle
     */
    public String getVin() {
        return this.vin;
    }

    @Override
    public String toString() {
        return "VehicleInfo{" +
                "vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                '}';
    }

    /**
     * Builder class that allows for optional properties that can be null on VehicleInfo
     */
    public static class Builder {
        private String vin;
        private String make;

        /**
         * Sets the make on the Builder. Including a make allows the user to bypass the car brand
         * selection screen.
         *
         * @param make name of the make of a vehicle.
         *
         * @return the builder with a `make` property added
         */
        public Builder make(String make) {
            this.make = make;
            return this;
        }

        /**
         * Sets the vin on the Builder.
         *
         * @param vin vin of the vehicle
         *
         * @return the builder with a `vin` property added
         */
        public Builder vin(String vin) {
            this.vin = vin;
            return this;
        }

        /**
         * Instantiates a new VehicleInfo object, which will also have any optional properties
         * that are already set on the Builder object that is calling this method.
         *
         * @return a new instantiation of the VehicleInfo class
         */
        public VehicleInfo build() {
            return new VehicleInfo(this);
        }
    }
}

package com.smartcar.sdk;

/**
 * A class that creates a custom VehicleInfo object.
 */
public class VehicleInfo {
    private String vin;
    private String make;
    private String model;
    private Integer year;

    /**
     * Assigns properties on the VehicleInfo object.
     *
     * @param builder the builder to obtain the properties from
     */
    private VehicleInfo (Builder builder) {
        this.vin = builder.vin;
        this.make = builder.make;
        this.model = builder.model;
        this.year = builder.year;
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

    /**
     * Returns the model assigned to VehicleInfo
     *
     * @return the model of the vehicle
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Returns the year assigned to VehicleInfo
     *
     * @return the year of the vehicle
     */
    public Integer getYear() {
        return this.year;
    }

    /**
     * Builder class that allows for optional properties that can be null on VehicleInfo
     */
    public static class Builder {
        private String vin;
        private String make;
        private String model;
        private Integer year;

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
         * Sets the model on the Builder.
         *
         * @param model model of the vehicle
         *
         * @return the builder with a `model` property added
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * Sets the year on the Builder.
         *
         * @param year year of the vehicle
         *
         * @return the builder with a `year` property added
         */
        public Builder year(Integer year) {
            this.year = year;
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

package com.smartcar.sdk;

/**
 * A class that creates a custom VehicleInfo object, which can be used
 * when generating an authentication URL or to receive a vehicle in the response.
 */
public class VehicleInfo {
    private String vin;
    private String make;
    private String model;
    private int year;

    /**
     * Constructor used to build the VehicleInfo object to set a make that allows the user to
     * bypass the brand selection screen.
     *
     * @param make make of a vehicle
     *
     * @return a VehicleInfo object with make set.
    */
    public VehicleInfo(String make) { this.make = make; }

    /**
     * Constructor used to build the VehicleInfo object to receive vehicle information from
     * Connect in case of an incompatible vehicle.
     *
     * @param vin vin of the vehicle
     * @param make make of a vehicle
     * @param model optional model of a vehicle
     * @param year year of a vehicle
     *
     * @return a VehicleInfo object with make set.
     */
    public VehicleInfo(String vin, String make, String model, int year) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
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
    public int getYear() {
        return this.year;
    }
}

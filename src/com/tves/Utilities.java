package com.tves;

/**
 * Static values of assumptions
 */
public class Utilities {
    /**
     * A straight 500 meters long street
     */
    public static int parkingStreetLength = 500;

    /**
     * Each parking place is 5m long
     */
    public static int parkingPlaceLength = 5;

    /**
     * Total Parking places
     */
    public static int totalParkingPlaces = 500;

    /**
     * All Parking places
     */
    public static boolean[] parking = new boolean[totalParkingPlaces];

    /**
     * Adding Noise to Sensors
     */
    public static int noiseS1 = 0;
    public static int noiseS2 = 0;
}

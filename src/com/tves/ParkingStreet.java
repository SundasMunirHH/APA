package com.tves;

/**
 * I assume we need this class. If not we can remove it later.
 * But lets give it a try.
 */
public class ParkingStreet {
    /**
     * A straight 500 meters long street with 5 meter long parking places,
     * total 100 parking places
     */

    /** All parking places from 1 to 100 with boolean value false showing vacant/empty position */
    private boolean[] parkingPlaces;

    /** A straight 500 meters long street */
    private int parkingStreetLength;

    /** Each parking place is 5m long */
    private int parkingPlaceLength;

    /**
     * @Description: constructor to initialize parking street
     * @Pre-condition:
     * @Post-condition:
     *
     * @Testcases: For example, parking area cannot be bigger than parking street
     * @Testcases: parking area cannot be 0 meters long
     * @Testcases: streetLength = 500, placeLength = 5
     *
     * @param streetLength length of the street, e.g., 500 meters
     * @param placeLength length of a single parking area, e.g., 5 meters
     */
    public ParkingStreet(int streetLength, int placeLength){
        /**
         * Example Code
         * if (placeLength <= 0 || streetLength <= 0){
         *      return;//or exception?
         * }
         * this.parkingPlaceLength = placeLength;
         * this.parkingStreetLength = streetLength;
         * int totalParkingPlaces = this.parkingStreetLength / this.parkingPlaceLength;
         * for (int i = 0; i<= totalParkingPlaces; i++){
         *      parkingPlaces[i] = false;
         * }
        */
    }

    /**
     * @Description: A car can register a particular parking place, depending on its current x position
     * For example, if xPos of the car is between [1, 5] meters on the street,
     * it can register 1st vacant parking space on the right
     * xPos of the car is between [6, 10] -> register 2nd parking space as parkingPlaces[0] = true
     * xPos of the car is between [496, 500] -> register last/100th parking space as parkingPlaces[499] = true
     *
     * @param xPos of the car
     */
    public void registerParkingPlace(int xPos){
        /**
         * Example Code
         * int place = (xPos - 1) / this.parkingPlaceLength;
         * parkingPlaces[place] = true;
         */
    }

    /**
     * @Description: A car can register a particular parking place, depending on its current x position
     *
     * @param pos of the parking place [1,100]
     * @return boolean as parkingPlaces[pos]
     */
    public boolean isEmptyParkingPlace(int pos){
        return parkingPlaces[pos];
    }

}

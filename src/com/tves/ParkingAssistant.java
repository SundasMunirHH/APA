package com.tves;

/**
 * An interface to the parking assistant in a car.
 */
public interface ParkingAssistant {
    /**
     * @return A data structure [the current car position, the situation of the detected parking places up to now.]
     * @Description: moves the car 1 meter forward,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved forward beyond the end of the street.
     * @Pre-condition:
     * @Post-condition:
     * @Test-cases:
     */
    public Object[] MoveForward();

    /**
     * @param
     * @return A data structure [the current car position, the situation of the detected parking places up to now.]
     * @throws IllegalArgumentException If what happens
     * @Description: moves the car 1 meter backwards,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved behind if it is already at the beginning of the street.
     * @Pre-condition:
     * @Post-condition:
     * @Test-cases:
     */
    public Object[] MoveBackward();

    /**
     * @return the distance to the nearest object in the right hand side
     * @throws IllegalArgumentException ???
     * @Description: queries the two ultrasound sensors at least 5 times,
     * filters the noise in their results,
     * If one sensor is detected to continuously return very noisy output, it should be completely disregarded.
     * You can use averaging or any other statistical method to filter the noise from the signals received from the ultrasound sensors.
     * @Pre-condition
     * @Post-condition
     * @Test-cases
     */
    public int isEmpty();

    /**
     * @throws IllegalArgumentException ???
     * @Description moves the car to the beginning of the current 5 meter free stretch of parking place, if it is already detected or
     * moves the car forwards towards the end of the street until such a stretch is detected.
     * Then it performs a pre-programmed reverse parallel parking maneuver.
     * @Pre-condition
     * @Post-condition
     * @Test-cases
     */
    public void Park();

    /**
     * @throws IllegalArgumentException If what happens
     * @Description moves the car forward (and to left) to front of the parking place, if it is parked.
     * @Pre-condition
     * @Post-condition
     * @Test-cases
     */
    public void UnPark();

    /**
     * @return current position of the car in the street and its situation (whether it is parked or not). [xPos, true/false]
     * @Description
     * @Pre-condition
     * @Post-condition
     * @Test-cases
     * @throws?
     */
    public Object[] WhereIs();

    /**
     * @Description: sets the occupied parking places on street manually, to sett all parking places on
     * street as occupied, mod = 500 and n = 500
     *
     * @return boolean[]
     */
    // public void setParkingPlaces(int mod, int n);
}

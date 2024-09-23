package com.tves;

/**
 * An interface to the parking assistant in a car.
 */
public interface ParkingAssistant {
    /**
     * @Description: moves the car 1 meter forward,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved forward beyond the end of the street.
     *
     * @return A data structure [the current car position, the situation of the detected parking places up to now.]
     */
    public Object[] MoveForward();

    /**
     * @Description: moves the car 1 meter backwards,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved behind if it is already at the beginning of the street.
     *
     * @return A data structure [the current car position, the situation of the detected parking places up to now.]
     */
    public Object[] MoveBackward();

    /**
     * @Description: queries the two ultrasound sensors at least 5 times,
     * filters the noise in their results,
     * If one sensor is detected to continuously return very noisy output, it should be completely disregarded.
     * You can use averaging or any other statistical method to filter the noise from the signals received from the ultrasound sensors.
     *
     * @return the distance to the nearest object in the right hand side
     */
    public int isEmpty();

    /**
     * @Description moves the car to the beginning of the current 5 meter free stretch of parking place, if it is already detected or
     * moves the car forwards towards the end of the street until such a stretch is detected.
     * Then it performs a pre-programmed reverse parallel parking maneuver.
     */
    public void Park();

    /**
     * @Description moves the car forward (and to left) to front of the parking place, if it is parked.
     */
    public void UnPark();

    /**
     * @return current position of the car in the street and its situation (whether it is parked or not). [xPos, true/false]
     */
    public Object[] WhereIs();

}

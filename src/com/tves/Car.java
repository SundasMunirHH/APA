package com.tves;

public class Car implements ParkingAssistant{

    /** Two ultrasound sensors in the car.*/
    private Sensor frontSensor, backSensor;

    /** Car registers available parking places on the RHS (right hand side).*/
    private boolean availableParkingPlace;

    /** the situation of the detected parking places up to now.*/
    private boolean isRegisteredParkingPlaceVacant;

    /** Indicates whether the car is currently parked.*/
    private boolean isParked;

    /** Current position of the car in a 2D plane.*/
    private int xPosition, yPosition;

    /** Constructor to initialize the Car with specific sensors.*/
    public Car(Sensor sensor1, Sensor sensor2) {
        this.frontSensor = sensor1;
        this.backSensor = sensor2;
    }

    /**
     * moves the car 1 meter forward,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved forward beyond the end of the street.
     *
     * @param
     * @return A data structure [the current car position, the situation of the detected parking places up to now.]
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void MoveForward() {

    }

    /**
     * moves the car 1 meter backwards,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved behind if it is already at the beginning of the street.
     *
     * @param
     * @return A data structure [the current car position, the situation of the detected parking places up to now.]
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void MoveBackward() {

    }

    /**
     * queries the two ultrasound sensors at least 5 times,
     * filters the noise in their results,
     * If one sensor is detected to continuously return very noisy output, it should be completely disregarded.
     * You can use averaging or any other statistical method to filter the noise from the signals received from the ultrasound sensors.
     *
     * @param
     * @return the distance to the nearest object in the right hand side
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void isEmpty() {

    }

    /**
     * moves the car to the beginning of the current 5 meter free stretch of parking place, if it is already detected or
     * moves the car forwards towards the end of the street until such a stretch is detected.
     * Then it performs a pre-programmed reverse parallel parking maneuver.
     *
     * @param ?
     * @return ?
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void Park() {

    }

    /**
     * moves the car forward (and to left) to front of the parking place, if it is parked.
     *
     * @param
     * @return ?
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void UnPark() {

    }

    /**
     *
     * @param
     * @return current position of the car in the street and its situation (whether it is parked or not).
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void WhereIs() {

    }


    public static void main(String[] args) {
     /**
      * We assume that the car moving along a perfectly straight street which is 500 meters long and registers the available parking places on its right-hand side.
      * To do this the car moves forward and uses two ultrasound sensors to check whether there is a free space on its right hand side.
      * The measurements are combined and filtered to reliably find a free parking stretch of 5 meters.
      * The car then moves to the end of the 5 meter stretch and runs a standard parallel reverse parking maneuver.
      */

     /**
      *
      * In all of the movements, the car sends a signal to the actuators moving the car,
      * but the actuators are not modeled in this phase. Likewise,
      * all the sensor signals come from sensor classes that are not modeled in this phase.
      * You can assume random or fixed sensor inputs in this phase.
      * The sensor inputs (for a properly working sensor) are integers (in the range of 0 to 200)
      * indicating the distance to the nearest object on the right.
      */
    }
}

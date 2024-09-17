package com.tves;

public class Car implements ParkingAssistant{

    /** Parking street on which the car is driving.*/
    private ParkingStreet parkingStreet;

    /** Two ultrasound sensors in the car.*/
    private Sensor frontSensor, backSensor;

    /**
     * Car registers available parking places on the RHS (right hand side).
     * parking space number ranges from 1 to 500
     * */
    private int registeredParkingPlace;

    /** the situation of the detected parking places up to now.*/
    private boolean isRegisteredParkingPlaceEmpty;

    /** Indicates whether the car is currently parked.*/
    private boolean isParked;

    /** Current position of the car horizontally.
     * x ranges from 1 to 500 to show car's position on the street
     * */
    private int xPosition;

    /** Constructor to initialize the Car with a given parking street and two sensors.*/
    public Car(ParkingStreet PS, Sensor sensor1, Sensor sensor2) {
        this.parkingStreet = PS;
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
     * queries the two ultrasound sensors at least 5 times, filters the noise in their results,
     * If one sensor is detected to continuously return very noisy output, it should be completely disregarded.
     * You can use averaging or any other statistical method to filter the noise from the signals received from the ultrasound sensors.
     *
     * @return boolean value as true if there is a parking stretch of 5m is found
     */
    @Override
    public boolean isEmpty() {
        /** Pseudo code
         * //The measurements from sensors are combined and filtered to reliably find a free parking stretch of 5 meters.
         * int dataFromFS, dataFromBS;
         * // at least five times ...
         * for (int i = 0; i < 5; i++){
         *      dataFromFS = this.frontSensor.getSensorData();
         *      // i am assuming that noise in the sensor data is when the result is not an integer btw 0 to 200
         *      if (this.frontSensor.isNoise(dataFromFS)){
         *          //its a noise and should be discarded
         *      }
         *      dataFromBS = this.backSensor.getSensorData();
         *      if (this.backSensor.isNoise(dataFromBS)){
         *          //its a noise and should be discarded
         *      }
         * }
         * // what if we still have noisy data
         * while (this.frontSensor.isNoise(dataFromFS) || this.frontSensor.isNoise(dataFromFS) ){
         * }

         * //sensors return integer values from [0,200] depending upon detection of another object
         * // we assume 1 means another object is at 1 meters distance to the sensor.
         * // if dataFromFS == 0 -> there is no object detected by front sensor
         * // To find a parking stretch of 5 meters, the front and back sensor values must span 5 meters
         * int objectAtDistance = Math.abs(dataFromFS - dataFromBS);
         * if (objectAtDistance >= 5){ //we have found a parking stretch of 5m
         *      return true;
         * }
         * */

        return true; // a temporary value

    }

    /**
     * moves the car to the beginning of the current 5 meter free stretch of parking place, if it is already detected or
     * moves the car forwards towards the end of the street until such a stretch is detected.
     * Then it performs a pre-programmed reverse parallel parking maneuver.
     *
     * @param ?
     * @return ?
     * @throws IllegalArgumentException ??
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
      * We assume that the car moving along a perfectly straight street which is 500 meters long
      * and registers the available parking places on its right-hand side.
      * To do this the car moves forward and uses two ultrasound sensors to check whether there is a free space on its right hand side.
      * The measurements are combined and filtered to reliably find a free parking stretch of 5 meters.
      * The car then moves to the end of the 5 meter stretch and runs a standard parallel reverse parking maneuver.
      */

        /**
         * The above translates to code that looks like follows
         * while (this.xPosition < this.parkingStreet.getParkingStreetLength()){
         *      MoveForward(); //Move forward 1m at a time
         *      // ask sensors whether there is empty place on the RHS
         *      if (isEmpty()){
         *          Park();
         *      }
         * }
         *
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

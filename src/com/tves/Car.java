package com.tves;
import java.util.ArrayList;

public class Car implements ParkingAssistant{

    /** Parking street on which the car is driving.*/
    private ParkingStreet parkingStreet;

    /** Two ultrasound sensors in the car.*/
    private Sensor frontSensor, backSensor;

    /**
     * Car registers available parking places on the RHS (right hand side).
     * parking places numbers ranges from 0 to 99
     * */
    private ArrayList<Integer> registeredParkingPlace;

    /** the situation of the detected parking places up to now.*/
    private boolean[] isRegisteredParkingPlaceEmpty;

    /** Indicates whether the car is currently parked.*/
    private boolean isParked;

    /** Current position of the car horizontally.
     * xPosition ranges from 0 to 499 to show car's (horizontal) position on the street
     */
    private int xPosition;

    /** Current position of the car Vertically (e.g., driveway is at y=0; parking place is at y=10).
     * yPosition ranges from 0 to 10 to show car's position on the street
     */
    private int yPosition;

    /** Constructor to initialize the Car with a given parking street and two sensors.*/
    public Car(ParkingStreet PS, Sensor sensor1, Sensor sensor2) {
        this.parkingStreet = PS;
        this.frontSensor = sensor1;
        this.backSensor = sensor2;
        this.registeredParkingPlace = new ArrayList<Integer>(); //ranges between [0,99] parking places.
        this.isParked = false; //By default a car is not parked
        this.xPosition = this.yPosition = 0; //at the start of the street on the driveway
    }

    /**
     * moves the car 1 meter forward,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved forward beyond the end of the street.
     *
     * @param
     * @return A data structure [the current car position, the situation of the detected parking places up to now.]
     */
    @Override
    public Object[] MoveForward() {
        /** Pseudo code
        while (this.xPosition < this.parkingStreet.getParkingStreetLength()) {
            this.xPosition += 1;
        }
         boolean situationOfParkingPlacesUptoNow =
        if (isEmpty()){
            //wherever the car is now (horizontally), there is a free parking space next to it.

        }else{
         //wherever the car is now (horizontally), there is not a free parking space next to it.
         }
        boolean situation = isEmpty();
         */
        // Return a Pair with xposition and situation of the detected parking places
        return new Object[]{this.xPosition, true};
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
        /** Pseudo code
        while (this.xPosition > 0) {
            this.xPosition -= 1;
        }
        boolean situation = isEmpty();
        */
    }

    /**
     * @Description: queries the two ultrasound sensors at least 5 times, filters the noise in their results,
     * If one sensor is detected to continuously return very noisy output, it should be completely disregarded.
     * You can use averaging or any other statistical method to filter the noise from the signals received from the ultrasound sensors.
     *
     * @return boolean value as true if there is a parking stretch of 5m is found
     */
    @Override
    public boolean isEmpty() {
        /** Pseudo code
         * //The measurements from sensors are combined and filtered to reliably find a free parking stretch of 5 meters.
         * //data from front and back sensors
         * int[] dataFromFS = new int[5];  // Array for front sensor data, at least five entries
         * int[] dataFromBS = new int[5];  // Array for back sensor data, at least five entries
         * // query the sensors at least five times ...
         * for (int i = 0; i < 5; i++){
         *      dataFromFS[i] = this.frontSensor.getSensorData();
         *      dataFromBS[i] = this.backSensor.getSensorData();
         * }
         * // get aggregated data
         * int aggrDataFS = this.frontSensor.aggregatedValue(dataFromFS);
         * int aggrDataBS = this.backSensor.aggregatedValue(dataFromBS);
         * // we assume:
         * // aggrDataFS == 1 -> another object is detected at 1 meters distance to the sensor.
         * // aggrDataFS == 0 -> there is no object detected by front sensor
         * // aggrDataFS < 0 -> noisy front sensor
         * if (aggrDataFS < 0 || aggrDataBS < 0){
         *     //we must disregard data from noisy sensor (but what do we assume when we disregard?)
         *     // for now, lets assume we do not go ahead with code when one sensor is noisy
         *     return false;
         * }
         * // To find a parking stretch of 5 meters, the front and back sensor values must span 5 meters
         * int objectAtDistance = Math.abs(aggrDataFS - aggrDataBS);
         * if (objectAtDistance == 0 || objectAtDistance >= 5){
         *      //we have a parking stretch of 5m, when either there are no objects detected or
         *      //objects are detected at least 5m far away
         *      return true;
         * }
         */
        return true; // a temporary value
    }

    /**
     * @Description: moves the car to the beginning of the current 5 meter free stretch of parking place, if it is already detected or
     * moves the car forwards towards the end of the street until such a stretch is detected.
     * Then it performs a pre-programmed reverse parallel parking maneuver.
     *
     * @param ?
     * @return ?
     * @throws IllegalArgumentException ??
     */
    @Override
    public void Park() {
        /** Pseudo code
         * // If the car us already parked we do not need to do anything
         * if (this.isParked){
         *     return;
         * }
         * int driveUpto = 0;
         *  //we have a registered parking place, drive upto it
         * if (this.registeredParkingPlace >= 0){
         *     driveUpto = this.registeredParkingPlace * 5;
         * }else{
         *      //otherwise dirve upto the end of the street
         *     driveUpto = this.parkingStreet.getParkingStreetLength();
         * }
         * while (this.xPosition < driveUpto){
         *      MoveForward(); //Move forward 1m at a time
         *      // if the car does not have a registered parking place
         *      // it needs to register one while driving upto the end of the street
         *      if (this.registeredParkingPlace < 0){
         *          // ask sensors whether there is empty place on the RHS
         *          if (isEmpty()){
         *              // We have found empty space on the RHS, lets register it
         *              this.registeredParkingPlace = this.parkingStreet.registerParkingPlace(this.xPosition);
         *          }
         *      }
         * }
         * if (this.registeredParkingPlace >= 0){
         *      // we have a registered parking place and the car is at front of it
         *      this.isParked = true;
         *      this.yPosition = 0; //lets assume the car moved to the right (on the parking area)
         *      return; //we do not need to do anything else, so we return
         *      }
         *
         * // If the car is at the end of the street and no parking place was available
         * // we move the car backwards to the start of the street
         *  while (this.xPosition > 1){
         *      MoveBackward(); //Move backward 1m at a time
         *  }
         *
         */
    }

    /**
     * @Description: moves the car forward (and to left) to front of the parking place, if it is parked.
     *
     * @param
     * @return ?
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void UnPark() {
         /** Pseudo code
          * //Unpark the car only if it was parked
          *  if (this.isParked){
          *     int driveUpto = this.registeredParkingPlace * 5;
          *     while (this.xPosition < driveUpto){
          *         MoveForward(); //Move forward 1m at a time
          *     }
          *     this.yPosition = 0; //lets assume the car moved to the left (on the drive area)
          *     this.isParked = false;
          *  }
          */
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
         * The above description translates to a code that looks like follows
         *
         * while (this.xPosition < this.parkingStreet.getParkingStreetLength()){
         *      MoveForward(); //Move forward 1m at a time
         *      // ask sensors whether there is empty place on the RHS
         *      if (isEmpty()){
         *          //register empty space
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
      */
    }
}

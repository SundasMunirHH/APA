package com.tves;
import java.util.ArrayList;

public class Car implements ParkingAssistant{

    /** Two ultrasound sensors in the car.*/
    private Sensor frontSensor1, frontSensor2;

    /**
     * Car registers available parking places on the RHS (right hand side).
     * parking places numbers range from 0 to 99
     * */
    private ArrayList<Boolean> registeredParkingPlaces;

    /** Indicates whether the car is currently parked.*/
    private boolean isParked;

    /** Current position of the car horizontally.
     * xPosition ranges from 0 to 499 to show car's (horizontal) position on the street
     */
    private int xPosition;

    private boolean[] parking;

    /** Constructor to initialize the Car with a given parking street and two sensors.*/
    public Car(Sensor sensor1, Sensor sensor2) {
        this.frontSensor1 = sensor1;
        this.frontSensor2 = sensor2;
        this.registeredParkingPlaces = new ArrayList<Boolean>(); //ranges between [0,99] parking places
        for(int i = 0; i < 500; i++){
            registeredParkingPlaces.add(false);
        }
        this.isParked = false; //By default a car is not parked
        this.xPosition = 0; //at the start of the street on the driveway
        this.parking = new boolean[500];
        for(int i = 0; i<this.parking.length;i++){
            if(i%10 == 7 || i%10 == 8 || i%10 == 9){
                this.parking[i] = false;
            }
            else{
                this.parking[i] = true;
            }
        }
    }

    /**
     * moves the car 100 centimeter forward,
     * queries the two sensors through the isEmpty method
     * The car cannot be moved forward beyond the end of the street.
     *
     * @return A data structure [int, ArrayList<Integer>] [the current car position, the situation of the detected parking places up to now.]
     */
    @Override
    public Object[] MoveForward() {
        /** Pseudo code*/
        if (this.isParked){
            // do nothing if it is parked
            return new Object[]{this.xPosition, this.registeredParkingPlaces};
        }
        // moves the car 100 centimeter forward until end of the street
        if(this.xPosition < Utilities.parkingStreetLength - 1) {
            //we cannot let xPosition be 500, then position at RHS will be 100
            this.xPosition += 1;
        }
        // Register parking place on RHS of current position
        this.registeredParkingPlaces.set(this.xPosition, isAvailableParkingPlace());

        // Return a Pair/object with xPosition and situation of the detected available parking places such as [1000,[0,1,3, ..., 86]]
        return new Object[]{this.xPosition, this.registeredParkingPlaces};
    }

    /**
     * Finds 500 cm long empty stretch by querying two sensors through the isEmpty method and
     * registers parking place
     */
    /*
    private void registerParkingPlace(){
        // Get parking place on RHS of current position
        int parkingRHS = this.getParkingPlaceRHS();
        // First detect empty place on RHS to register available parking places
        if (isAvailableParkingPlace()){
            // We have found empty parking place on the RHS, lets register it
            if (!this.registeredParkingPlaces.contains(parkingRHS)) {
                this.registeredParkingPlaces.add(parkingRHS);
            }
        }else{
            //wherever the car is now (horizontally), there is not a free parking space next to it.
            //lets remove from registered parking places
            if (this.registeredParkingPlaces.contains(parkingRHS)) {
                this.registeredParkingPlaces.remove(parkingRHS);
            }
        }
    }
    */
    /**
     * moves the car 1 meter backwards,
     * The car cannot be moved behind if it is already at the beginning of the street.
     *
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public Object[] MoveBackward() {
        /** Pseudo code*/
        if (this.isParked){
            // do nothing if it is parked
            return new Object[]{this.xPosition, this.registeredParkingPlaces};
        }
        if (this.xPosition > 0) {
            this.xPosition -= 1;
        }

        // Register parking place on RHS of current position
        this.registeredParkingPlaces.set(this.xPosition, isAvailableParkingPlace());

        return new Object[]{this.xPosition, this.registeredParkingPlaces};
    }

    /**
     * @Description queries the two ultrasound sensors at least 5 times, filters the noise in their results,
     * If one sensor is detected to continuously return very noisy output, it should be completely disregarded.
     * You can use averaging or any other statistical method to filter the noise from the signals received from the ultrasound sensors.
     *
     * @return boolean value as true if there is a parking stretch of 500cm is found
     */
    @Override
    public int isEmpty() {
        /** Pseudo code*/
          //The measurements from sensors are combined and filtered to reliably find a free parking stretch of 5 meters.
          //data from front and back sensors
          int[] dataFromFS1 = new int[5];  // Array for front sensor data, at least five entries
          int[] dataFromFS2 = new int[5];  // Array for back sensor data, at least five entries
          // query the sensors at least five times ...
          for (int i = 0; i < 5; i++){
               dataFromFS1[i] = this.frontSensor1.getSensorData(this.parking,this.xPosition);
               dataFromFS2[i] = this.frontSensor2.getSensorData(this.parking,this.xPosition);
          }
          // get aggregated data
          int aggrDataFS1 = this.frontSensor1.aggregatedValue(dataFromFS1);
          int aggrDataFS2 = this.frontSensor2.aggregatedValue(dataFromFS2);
          // we assume:
          // aggrDataFS == 1 -> another object is detected at 1 meters distance to the sensor.
          // aggrDataFS == 0 -> another object is colliding
          // aggrDataFS > 0 -> noisy front sensor
        // Evaluating noisy data
        boolean FS1, FS2 = true;
        // decide on a threshold for the data to be considered noisy
        int maxIndexFS1 = 0, maxIndexFS2 = 0, minIndexFS1 = 0, minIndexFS2 = 0, threshold = 10;
        // finding max and min value to compare them and find if they are significant
        for(int i = 0; i < dataFromFS1.length; i++){
            // going through data set to search for the max and min value
            if(dataFromFS1[i] >= dataFromFS1[maxIndexFS1]){
                maxIndexFS1 = i;
            }
            if(dataFromFS1[i] <= dataFromFS1[minIndexFS1]){
                minIndexFS1 = i;
            }
            if(dataFromFS2[i] >= dataFromFS2[maxIndexFS2]){
                maxIndexFS2 = i;
            }
            if(dataFromFS2[i] <= dataFromFS2[minIndexFS2]){
                minIndexFS2 = i;
            }
        }
        // if the difference is high then the disregard sensor
        if(dataFromFS1[maxIndexFS1]-dataFromFS1[minIndexFS1] > threshold){
            FS1 = false;
        }
        if(dataFromFS1[maxIndexFS2]-dataFromFS1[minIndexFS2] > threshold){
            FS2 = false;
        }
        int aggrDataOneSensor;
        if (frontSensor1.isNoise(aggrDataFS1) || frontSensor2.isNoise(aggrDataFS2)){
            //we disregard data from noisy sensor
            // and assume the value of well-functioning sensor
            if (!frontSensor2.isNoise(aggrDataFS2)){
                aggrDataOneSensor = aggrDataFS2;
            }
            else if (!frontSensor1.isNoise(aggrDataFS1)){
                aggrDataOneSensor = aggrDataFS1;
            }else{
                //Oops, both sensors are noisy
                //lets assume there is no object detected in this case (for the timebeing)
                aggrDataOneSensor = -1;
            }
        }
        else{
            //both sensors are well-functioning, get average of both values.
            aggrDataOneSensor = (aggrDataFS1 + aggrDataFS2)/2;
        }
        /*int distanceToObjectRHS;
          if (aggrDataFS < 0 && aggrDataBS < 0){
              //Nothing detected by both sensors
              distanceToObjectRHS = -1;
          }else{
              distanceToObjectRHS = aggrDataFS - aggrDataBS; //Math.abs(aggrDataFS - aggrDataBS);
          }*/
          // To find a parking stretch of 500 centimeters, the front1 and front2 sensor values must span 500 centimeters
        return aggrDataOneSensor;
    }

    /** calls isEmpty method to get sensors data and estimate whether we have 5m long stretch available */
    private boolean isAvailableParkingPlace(){
        int distanceToObjectRHS = isEmpty();
        if (distanceToObjectRHS <= 60 ){
            //If the sensors gives a distance of 60 cm or less then the specific meter of space is occupied
            //otherwise the spot is  considered empty
            return false;
        }else{
            return true;
        }

    }

    /**
     * @Description: moves the car to the beginning of the current 500 centimeter free stretch of parking place, if it is already detected or
     * moves the car forwards towards the end of the street until such a stretch is detected.
     * Then it performs a pre-programmed reverse parallel parking maneuver.
     *
     * @throws IllegalArgumentException ??
     */
    @Override
    public void Park() {
        /** Pseudo code */
        // If the car is already parked we do not need to do anything
        if (this.isParked || this.xPosition < 5) {
            return;
        }
        for (int i = 0; i < 5; i++) {
            if (!this.registeredParkingPlaces.get(this.xPosition)) {
                return;
            }
         /*
         while (!this.isParked && this.xPosition < Utilities.parkingStreetLength - 1){
             Object[] status = MoveForward(); //Moves forward 1m, returns car's pos and situation of registered parking places
             //returned status is [1000,[0,3,6, ..., 71]] //e.g., available parking places are 0,3,6, ..., 71
             ArrayList<Integer> availablePP = (ArrayList<Integer>) status[1];
             // Parking place on RHS of current position
             int parkingRHS = this.getParkingPlaceRHS();
             // is it available?
             if (availablePP.contains(parkingRHS)) {
                 // we have a vacant registered parking place
                 this.isParked = true;
                 //Here we will have reverse parking maneuver.
                 //this.reverseParallelParkingManeuver();
                 break;
             }
          */

            // what if we have still not parked??
        }
        this.xPosition = this.xPosition-2;
        this.isParked = true;
    }

    /**
     * @Description pre-programmed reverse parallel parking maneuver.
     * Currently only sets y to 0 to get the car in the driving area
     *
     */

    /**
     * @Description: moves the car forward (and to left) to front of the parking place, if it is parked.
     *
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public void UnPark() {
         /** Pseudo code*/
           //Unpark the car only if it was parked
            if (this.isParked){
               int driveUpto = this.xPosition + 5; //because 5m is parking place's length
               if (driveUpto > Utilities.parkingStreetLength){
                   driveUpto = Utilities.parkingStreetLength;
               }
               this.isParked = false;
               while (this.xPosition < driveUpto){
                   MoveForward(); //Move forward 1m at a time
               }

            }
    }

    /**
     *
     * @return current position of the car in the street and its situation (whether it is parked or not).
     * @throws IllegalArgumentException If what happens
     */
    @Override
    public Object[] WhereIs() {
        return new Object[]{this.xPosition,this.isParked};
    }

    /**
     * @Description A parking place on the RHS of the car
     * this.xPosition between [0,4] -> getParkingPlaceRHS = 0
     * this.xPosition between [5,9] -> getParkingPlaceRHS = 1
     * this.xPosition between [495,499] -> getParkingPlaceRHS = 99
     *
     * @return int Parking place on the RHS of the car valid value range from 0 to 99
     */
    /*
    private int getParkingPlaceRHS(){
        /** Pseudo Code
        int place = this.xPosition;// / Utilities.parkingPlaceLength;
        return place;
    }
    */

    public static void main(String[] args) {
      /*  Car[] mC = new Car[10];
        for (int i=0;i<10;i++){
            Sensor frontSensor = new UltraSoundSensor("frontSensor");
            Sensor backSensor = new UltraSoundSensor("backSensor");
            mC[i] = new Car(frontSensor, backSensor);
            mC[i].Park();
            Object[] where = mC[i].WhereIs();
            System.out.println(i+1 +" car is parked at: " + where[0]);
        }*/

     /**
      * We assume that the car is moving along a perfectly straight street which is 500 meters long
      * and registers the available parking places on its right-hand side.
      * To do this the car moves forward and uses two ultrasound sensors to check whether there is a free space on its right hand side.
      * The measurements are combined and filtered to reliably find a free parking stretch of 500 centimeters.
      * The car then moves to the end of the 500 centimeter stretch and runs a standard parallel reverse parking maneuver.
      */
    }
}

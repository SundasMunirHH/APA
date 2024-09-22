package com.tves;
import java.util.ArrayList;

public class Car implements ParkingAssistant{

    /** Two ultrasound sensors at the front of the car.*/
    private Sensor frontSensor1, frontSensor2;

    /**
     * Car registers available parking places on the RHS (right hand side).
     * */
    private ArrayList<Boolean> registeredParkingPlaces;

    /**
     * Pre-programmed occupied parking places on the RHS.
     */
    private boolean[] parking;

    /** Indicates whether the car is currently parked.*/
    private boolean isParked;

    /** Current position of the car horizontally.
     * xPosition ranges from 0 to 499 to show car's (horizontal) position on the street
     */
    private int xPosition;

    /** Constructor to initialize the Car with a given parking street and two sensors.*/
    public Car(Sensor sensor1, Sensor sensor2) {
        this.frontSensor1 = sensor1;
        this.frontSensor2 = sensor2;
        this.registeredParkingPlaces = new ArrayList<Boolean>(); //ranges between [0,499] parking places
        for(int i = 0; i < 500; i++){
            registeredParkingPlaces.add(false);
        }
        this.isParked = false; //By default the car is not parked
        this.xPosition = 0; //at the start of the street on the driveway
        //All parking places
        this.parking = new boolean[500];
    }

    /**
     * moves the car 100 centimeter or 1 meter forward,
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
            //we cannot let xPosition be 500,
            this.xPosition += 1;
        }
        // Register parking place on RHS of current position
        this.registeredParkingPlaces.set(this.xPosition, isAvailableParkingPlace());

        // Return a Pair/object with xPosition and situation of the detected available parking places such as [1000,[0,1,3, ..., 86]]
        return new Object[]{this.xPosition, this.registeredParkingPlaces};
    }

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
     * @return distance to the nearest object in centimeters
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
        boolean isFS1Noisy, isFS2Noisy;
        isFS1Noisy = frontSensor1.isNoise(dataFromFS1);
        isFS2Noisy = frontSensor2.isNoise(dataFromFS2);
        //we must aggregate data from both sensors if they are well-functioning sensors
        int aggrDataOneSensor;
        if (isFS1Noisy || isFS2Noisy){
            //we disregard data from noisy sensor and assume the value of well-functioning sensor
            if (!isFS1Noisy){
                aggrDataOneSensor = this.frontSensor1.aggregatedValue(dataFromFS1);
            }
            else if (!isFS2Noisy){
                aggrDataOneSensor = this.frontSensor2.aggregatedValue(dataFromFS2);
            }
            else{
                //Oops, both sensors are noisy, currently we assume this never happens
                //lets assume there is no object detected in this case (for the time being)
                aggrDataOneSensor = -1;
            }
        }
        /*

        // Evaluating noisy data
        boolean FS1, FS2 = true;
        // decide on a threshold for the data to be considered noisy
        int maxIndexFS1 = 0, maxIndexFS2 = 0, minIndexFS1 = 0, minIndexFS2 = 0, threshold = 0;
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
*/
        else{
            //both sensors are well-functioning, get average of both values.
            // get aggregated data
            int aggrDataFS1 = this.frontSensor1.aggregatedValue(dataFromFS1);
            int aggrDataFS2 = this.frontSensor2.aggregatedValue(dataFromFS2);
            aggrDataOneSensor = (aggrDataFS1 + aggrDataFS2) / 2;
        }
        return aggrDataOneSensor;
    }

    /** calls isEmpty method to get sensors data and estimate whether we have 5m long stretch available */
    private boolean isAvailableParkingPlace(){
        int distanceToObjectRHS = isEmpty();
        if (distanceToObjectRHS <= 60){
            //If the sensors gives a distance of 60 cm or less then the specific meter of space is occupied
            //otherwise the spot is considered empty
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
     */
    @Override
    public void Park() {
        // If the car is already parked we do not need to do anything
        if (this.isParked || this.xPosition < 5) {
            return;
        }
        //check the last 5 meters from the current xPosition in the registered parking places
        for (int i = 0; i < 5; i++) {
            if (!this.registeredParkingPlaces.get(this.xPosition - i)) {
                //the parking stretch of 5m is not available
                return;
            }
        }
        this.xPosition = this.xPosition-2;
        this.isParked = true;
    }

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
            int driveUpto = this.xPosition + 2; //because we drive 2m back for parking
            //Testcase: Corresponding to the test case of unparking at the end of the street "testUnParkAtEndOfStreet".
              /*  if (driveUpto >= Utilities.parkingStreetLength){
                   driveUpto = Utilities.parkingStreetLength - 1;
               }*/
            this.isParked = false;
            //Since the car must move forward (and to the left) to get back on the drive area
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
     * @Description: setting the occupied parking places on street manually, to sett all parking places on
     * street as occupied, mod = 500 and n = 500
     *
     * @return boolean[]
     */
    public boolean[] setParkingPlaces(int mod, int n){
        //Setting some parking places to be available and unavailable
        for(int i = 0; i < this.parking.length; i++){
            //Setting all parking places occupied by mod = 1 n = 0.
            if(i%mod == n || i%mod == n+1 || i%mod == n+2){
                this.parking[i] = false;
            }
            else{
                this.parking[i] = true;
            }
        }
        return this.parking;
    }
}

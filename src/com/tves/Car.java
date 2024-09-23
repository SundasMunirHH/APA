package com.tves;

import java.util.ArrayList;

public class Car implements ParkingAssistant {

    /**
     * Two ultrasound sensors at the front of the car.
     */
    private Sensor frontSensor1, frontSensor2;

    /**
     * Car registers available parking places on the RHS (right hand side).
     */
    private ArrayList<Boolean> registeredParkingPlaces;

    /**
     * Indicates whether the car is currently parked.
     */
    private boolean isParked;

    /**
     * Current position of the car horizontally.
     * xPosition ranges from 0 to 499 to show car's (horizontal) position on the street
     */
    private int xPosition;

    /**
     * Constructor to initialize the Car with two sensors.
     */
    public Car(Sensor sensor1, Sensor sensor2) {
        //testcase: testConstructor, Assert 1
        this.frontSensor1 = sensor1;
        this.frontSensor2 = sensor2;

        //testcase: testMoveForward, Assert 2
        this.registeredParkingPlaces = new ArrayList<Boolean>(); //ranges between [0,499] parking places
        for (int i = 0; i < 500; i++) {
            registeredParkingPlaces.add(false);
        }

        //testcase: testConstructor, Assert 2
        this.isParked = false; //By default the car is not parked

        //testcase: testConstructor, Assert 3
        this.xPosition = 0; //at the start of the street on the driveway
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
        //testcase: testMoveForwardWhileParked
        if (this.isParked) {
            // do nothing if it is parked
            return new Object[]{this.xPosition, this.registeredParkingPlaces};
        }

        //testcase: testMoveForward, Assert 1, and testMoveForwardAtEndOfStreet, and
        // moves the car 100 centimeter forward until end of the street
        if (this.xPosition < Utilities.parkingStreetLength - 1) {//testcase:
            //we cannot let xPosition be 500,
            this.xPosition += 1;
        }

        //testcase: testMoveForward, Assert 2, and testMoveForwardParkingAvailable, and testMoveForwardParkingNotAvailable
        // Register parking place on RHS of current position
        this.registeredParkingPlaces.set(this.xPosition, isAvailableParkingPlace());

        //testcase: testMoveForward, Assert 1 and Assert 2
        // Return a Pair/object with xPosition and situation of the detected available parking places such as [1000,[true,true,false, false, ..., false]]
        return new Object[]{this.xPosition, this.registeredParkingPlaces};
    }

    /**
     * moves the car 1 meter backwards,
     * The car cannot be moved behind if it is already at the beginning of the street.
     *
     */
    @Override
    public Object[] MoveBackward() {
        //testcase: testMoveBackwardWhileParked
        if (this.isParked) {
            // do nothing if it is parked
            return new Object[]{this.xPosition, this.registeredParkingPlaces};
        }

        //testcase: testMoveBackwardAtStartOfStreet, and testMoveBackwardBeyondStreet
        if (this.xPosition > 0) {
            this.xPosition -= 1;
        }

        //testcase: testMoveBackward, Assert 2, and testMoveBackwardParkingAvailable, and testMoveBackwardParkingNotAvailable
        // Register parking place on RHS of current position
        this.registeredParkingPlaces.set(this.xPosition, isAvailableParkingPlace());

        //testcase: testMoveBackward, Assert 1 and Assert 2
        return new Object[]{this.xPosition, this.registeredParkingPlaces};
    }

    /**
     * @Description queries the two ultrasound sensors at least 5 times, filters the noise in their results,
     * If one sensor is detected to continuously return very noisy output, it should be completely disregarded.
     * You can use averaging or any other statistical method to filter the noise from the signals received from the ultrasound sensors.
     * @return distance to the nearest object in centimeters
     */
    @Override
    public int isEmpty() {
        //testcase: testisEmpty
        //data from both sensors
        int[] dataFromFS1 = new int[5];  // Array for front sensor data, at least five entries
        int[] dataFromFS2 = new int[5];  // Array for back sensor data, at least five entries

        //testcase: testisEmptyNoisySensor
        for (int i = 0; i < 5; i++) {// query the sensors at least five times ...
            dataFromFS1[i] = this.frontSensor1.getSensorData(this.xPosition, Utilities.noiseS1);
            dataFromFS2[i] = this.frontSensor2.getSensorData(this.xPosition, Utilities.noiseS2);
        }
        boolean isFS1Noisy, isFS2Noisy;
        isFS1Noisy = frontSensor1.isNoise(dataFromFS1);
        isFS2Noisy = frontSensor2.isNoise(dataFromFS2);
        //we must aggregate data from both sensors if they are well-functioning sensors
        int aggrDataOneSensor;
        if (isFS1Noisy || isFS2Noisy) {
            //we disregard data from noisy sensor and assume the value of well-functioning sensor
            if (!isFS1Noisy) {
                aggrDataOneSensor = this.frontSensor1.aggregatedValue(dataFromFS1);
            } else if (!isFS2Noisy) {
                aggrDataOneSensor = this.frontSensor2.aggregatedValue(dataFromFS2);
            } else {
                //testcase: testisEmptyNoisySensor
                //lets assume there is no object detected in this case (for the time being)
                aggrDataOneSensor = -1;
            }
        } else {
            //testcase: testisEmpty
            //both sensors are well-functioning, get average of both values.
            int aggrDataFS1 = this.frontSensor1.aggregatedValue(dataFromFS1);
            int aggrDataFS2 = this.frontSensor2.aggregatedValue(dataFromFS2);
            aggrDataOneSensor = (aggrDataFS1 + aggrDataFS2) / 2;
        }
        //testcase: testisEmpty
        return aggrDataOneSensor;
    }

    /**
     * calls isEmpty method to get sensors data and estimate whether we have 5m long stretch available
     */
    private boolean isAvailableParkingPlace() {
        //testcase: testMoveForward, Assert 2 and testMoveBackward, Assert 2
        int distanceToObjectRHS = isEmpty();
        if (distanceToObjectRHS <= 60) {
            //testcase: testParkWhileParked
            //If the sensors gives a distance of 60 cm or less then the specific meter of space is occupied
            //otherwise the spot is considered empty
            return false;
        } else {
            //testcase: testMoveForward, Assert 2 and testMoveBackward, Assert 2
            return true;
        }
    }

    /**
     * @Description: moves the car to the beginning of the current 500 centimeter free stretch of parking place, if it is already detected or
     * moves the car forwards towards the end of the street until such a stretch is detected.
     * Then it performs a pre-programmed reverse parallel parking maneuver.
     */
    @Override
    public void Park() {
        //testcase: testParkWhileParked and testParkAtStart, Assert 1 ans 2
        // If the car is already parked we do not need to do anything
        if (this.isParked || this.xPosition < 5) {
            return;
        }

        //testcase: testParkWithNoParkingAvailable
        //check the last 5 meters from the current xPosition in the registered parking places
        for (int i = 0; i < 5; i++) {
            if (!this.registeredParkingPlaces.get(this.xPosition - i)) {
                //the parking stretch of 5m is not available
                return;
            }
        }

        //testcase: testPark, Assert 2
        this.xPosition = this.xPosition - 2;

        //testcase: testPark, Assert 3
        setParkingPlaces(this.xPosition, false);

        //testcase: testPark, Assert 1
        this.isParked = true;
    }

    /**
     * @Description: moves the car forward (and to left) to front of the parking place, if it is parked.
     */
    @Override
    public void UnPark() {
        //testcase: testUnParkWhileNotParked
        if (this.isParked) { //Unpark the car only if it was parked

            //testcase: testUnPark, Assert 1.
            this.isParked = false;

            //testcase: testUnPark, Assert 3.
            setParkingPlaces(this.xPosition, true);//Unparking from the parking places

            //testcase: testUnPark, Assert 2
            //Since the car must move forward (and to the left) to get back on the drive area
            int driveUpto = this.xPosition + 2; //because we drove 2m back for parking, to unpark we drive 2m forward
            while (this.xPosition < driveUpto) {
                MoveForward(); //Move forward 1m at a time
            }
        }
    }

    /**
     * @return current position of the car in the street and its situation (whether it is parked or not).
     */
    @Override
    public Object[] WhereIs() {
        //testcase: this is tested with many JUnit tests, such as
        // testParkAtStart, testPark, testParkWhileParked, testParkWithNoParkingAvailable, testUnPark
        return new Object[]{this.xPosition, this.isParked};
    }

    /**
     * @Description: sets the occupied/unoccupied parking places on parking street,
     */
    private void setParkingPlaces(int xPos, boolean value) {
        //testcase: testUnPark Assert 3, and testPark, Assert 3
        Utilities.parking[xPos] = value;
        Utilities.parking[xPos - 1] = value;
        Utilities.parking[xPos - 2] = value;
    }
}

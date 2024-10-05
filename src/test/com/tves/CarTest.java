package com.tves;

import org.junit.jupiter.api.*;
import java.util.ArrayList;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarTest {

    private Car myCar;

    @BeforeEach
    public void createCar() {
        //Arrange
        Sensor sensor1 = new UltraSoundSensor("sensor1");
        Sensor sensor2 = new UltraSoundSensor("sensor2");
        this.myCar = new Car(sensor1, sensor2);

        //Set all parking places to be available ebfore each test case
        for (int i = 0; i < 500; i++) {
            Utilities.parking[i] = true;
        }
    }

    // Testing the constructor or initialization of a car
    @Test
    public void testConstructor() {
        //Test initialization of car
        //Act
        Sensor sensor1 = new UltraSoundSensor("sensor1");
        Sensor sensor2 = new UltraSoundSensor("sensor2");
        Car aCar = new Car(sensor1, sensor2);

        //Assert 1
        Assertions.assertInstanceOf(Car.class, aCar, "Car is not initialized as an object of Car");

        //Testing initialization of car at an expected state
        Object[] whereIs = myCar.WhereIs();
        int startingPosition = (Integer) whereIs[0];
        boolean isParked = (Boolean) whereIs[1];

        //Assert 2
        Assertions.assertFalse(isParked, "The car has initiated with parked state.");

        //Assert 3
        Assertions.assertEquals(0, startingPosition, "The car has not initiated at position 0.");
    }

    //MoveForward() when c1 is false, c2 is true, and c3 is false from decision table.
    @Test
    public void testMoveForward() {
        //Arrange
        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];

        //Act
        Object[] moveResult = myCar.MoveForward();
        int currentPosition = (Integer) moveResult[0];
        ArrayList<Boolean> registeredPP = (ArrayList<Boolean>) moveResult[1];

        //Assert 1
        Assertions.assertEquals(prevPosition + 1, currentPosition, "The car has not moved 1m forward.");

        //Assert 2
        Assertions.assertEquals(500, registeredPP.size(), "Move forward has not been able to register 500 parking places.");
    }

    //MoveForward() when c1 is true.
    @Test
    public void testMoveForwardWhileParked() {
        //Act
        for (int i = 0; i < 6; i++) {
            myCar.MoveForward();
        }
        myCar.Park();

        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        Object[] moveResult = myCar.MoveForward();
        int newPos = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has moved 1m forward while at parking state.");
    }

    //MoveForward() when c1 is false, c2 is true, and c3 is true.
    @Test
    public void testMoveForwardAtEndOfStreet() {
        //Act
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];
        //move forward until end of the street
        while (prevPos < Utilities.parkingStreetLength - 1) {
            Object[] moveResult = myCar.MoveForward();
            prevPos = (Integer) moveResult[0];
        }
        // now move one more time at the end of the street
        Object[] moveResult = myCar.MoveForward();
        int newPos = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has moved 1m forward at the end of the street.");
    }

     //MoveForward() when c1 is false and c2 is false.
     @Test
     public void testMoveForwardBeyondStreet(){
        //No amount of Moving forward should make the car go beyond the street.
         //Act - A large loop to check whether a car can move forward beyond street.
         for (int i = 0; i < 1000; i++) {
             myCar.MoveForward();
         }
         Object[] whereIs = myCar.WhereIs();
         int pos = (Integer) whereIs[0];

         //Assert
         Assertions.assertTrue(pos < Utilities.parkingStreetLength, "The car has moved beyond the street.");
     }

    //MoveForward() when c1 is false, c2 is true, c3 is false, and c4 is true
    @Test
    public void testMoveForwardParkingAvailable(){
        //Act
        Object[] moveResult = myCar.MoveForward();
        int pos = (Integer) moveResult[0];
        ArrayList<Boolean> registeredPP = (ArrayList<Boolean>) moveResult[1];

        //Assert - if there is parking available,  must have true in indexes at RHS
        Assertions.assertTrue(registeredPP.get(pos), "While moving forward, parking is available at RHS but the registered parking array is false");
    }

    //MoveForward() when c1 is false, c2 is true, c3 is false, and c4 is false.
    @Test
    public void testMoveForwardParkingNotAvailable(){
        //Arrange
        //we first create another car and park it at a parking place
        Sensor sensor1 = new UltraSoundSensor("sensor1");
        Sensor sensor2 = new UltraSoundSensor("sensor2");
        Car aCar = new Car(sensor1, sensor2);
        //move a car to the first parking place
        for (int i = 0; i < 5; i++) {
            aCar.MoveForward();
        }
        //park the car at the first parking place
        aCar.Park();

        //Act
        Object[] moveResult = myCar.MoveForward();
        int pos = (Integer) moveResult[0];
        ArrayList<Boolean> registeredPP = (ArrayList<Boolean>) moveResult[1];

        //Assert - if there is parking available,  must have true in indexes at RHS
        Assertions.assertFalse(registeredPP.get(pos), "While moving forward, parking is not available at RHS but the registered parking array is true.");
    }

    //MoveBackward() when c1 is false, c2 is true, and c3 is false.
    @Test
    public void testMoveBackward() {
        //Act
        //first move forward
        myCar.MoveForward();
        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];
        //then move backwards
        Object[] moveResult = myCar.MoveBackward();
        int currentPosition = (Integer) moveResult[0];
        ArrayList<Boolean> registeredPP = (ArrayList<Boolean>) moveResult[1];

        //Assert 1
        Assertions.assertEquals(prevPosition - 1, currentPosition, "The car has moved 1m backward.");

        //Assert 2
        Assertions.assertEquals(500, registeredPP.size(), "Move backward has not been able to register parking places.");

    }

    //MoveBackward() when c1 is true.
    @Test
    public void testMoveBackwardWhileParked() {
        //Act
        //first move forward
        for (int i = 0; i < 6; i++) {
            myCar.MoveForward();
        }
        //Then park the car
        myCar.Park();
        //Then check car's position
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];
        Object[] moveResult = myCar.MoveBackward();
        int newPos = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has moved 1m forward while at parking state.");
    }

    // MoveBackward() when c1 is false, c2 is true, and c3 is true.
    @Test
    public void testMoveBackwardAtStartOfStreet() {
        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];

        //Act
        Object[] moveResult = myCar.MoveBackward();
        int currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition, currentPosition, "The car has moved 1m backward from starting position.");
    }

    //MoveBackward() when c1 is false and c2 is false.
    @Test
    public void testMoveBackwardBeyondStreet() {
        //No amount of Moving backward should make the car go beyond the street.
        //Act - A large loop to check whether a car can move backward beyond street.
        for (int i = 0; i < 1000; i++) {
            myCar.MoveBackward();
        }
        Object[] whereIs = myCar.WhereIs();
        int pos = (Integer) whereIs[0];

        //Assert
        Assertions.assertTrue(pos >= 0, "The car has moved beyond the street.");

    }

    //MoveBackward() when c1 is false, c2 is true, c3 is false, and c4 is true.
    @Test
    public void testMoveBackwardParkingAvailable() {
        //Arrange
        //move the car forward before testing the backward method
        for (int i = 0; i < 5; i++) {
            myCar.MoveForward();
        }

        //Act
        Object[] moveResult = myCar.MoveBackward();
        int pos = (Integer) moveResult[0];
        ArrayList<Boolean> registeredPP = (ArrayList<Boolean>) moveResult[1];

        //Assert - if there is parking available,  must have true in indexes at RHS
        Assertions.assertTrue(registeredPP.get(pos), "While moving forward, parking is available at RHS but the registered parking array is false");

    }

    //MoveBackward() when c1 is false, c2 is true, c3 is false, and c4 is false.
    @Test
    public void testMoveBackwardParkingNotAvailable() {
        //Arrange
        //we first create another car and park it at a parking place
        Sensor sensor1 = new UltraSoundSensor("sensor1");
        Sensor sensor2 = new UltraSoundSensor("sensor2");
        Car aCar = new Car(sensor1, sensor2);
        //move a car to the first parking place
        for (int i = 0; i < 5; i++) {
            aCar.MoveForward();
        }
        //park the car at the first parking place
        aCar.Park();

        //Act
        //move my car (the car under test) to the first parking place
        for (int i = 0; i < 4; i++) {
            myCar.MoveForward();
        }
        //Since we aim to test move backward here
        Object[] moveResult = myCar.MoveBackward();
        int pos = (Integer) moveResult[0];
        ArrayList<Boolean> registeredPP = (ArrayList<Boolean>) moveResult[1];

        //Assert - if there is parking available,  must have true in indexes at RHS
        Assertions.assertFalse(registeredPP.get(pos), "While moving backward, parking is not available at RHS but the registered parking array is true.");
    }

    @Test
    public void testisEmpty() {
        //Act
        int distance = myCar.isEmpty();
        //Assert
        Assertions.assertNotEquals(-1, distance, "The sensor data is noisy.");
    }

    @Test
    public void testisEmptyNoisySensor() {
        //Act
        Utilities.noiseS1 = 100;
        Utilities.noiseS2 = 0;
        myCar.isEmpty();
        //We can just check the line coverage, there is nothing to assert in this case

        Utilities.noiseS1 = 0;
        Utilities.noiseS2 = 100;
        myCar.isEmpty();
        //We can just check the line coverage, there is nothing to assert in this case

        Utilities.noiseS1 = 100;
        Utilities.noiseS2 = 100;
        int distance = myCar.isEmpty();

        //Assert
        Assertions.assertEquals(-1, distance, "The empty method still returns a valid distance when both sensors are noisy.");

        Utilities.noiseS1 = 0;
        Utilities.noiseS2 = 0;
    }

    //Park() when c1 is false, c2 is true. We name this method “testParkAtStart()”
    @Test
    public void testParkAtStart() {
        //Arrange
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        myCar.Park();
        whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];
        int newPos = (Integer) whereIs[0];

        //Assert 1
        Assertions.assertFalse(isParked, "The car has parked at the beginning of the street (before driving 5m).");

        //Since the position of the car moves 2m backwards when it parks, we further check the position as well
        //Assert 2
        Assertions.assertEquals(prevPos, newPos, "The car has parked.");
    }

    //Park() when c1 is false, c2 is false, c3 is true.
    @Test
    public void testPark() {
        //Arrange
        //we first drive at least 5 meters ahead on the road
        for (int i = 0; i < 5; i++) {
            myCar.MoveForward();
        }
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        myCar.Park();
        whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];
        int newPos = (Integer) whereIs[0];

        //Assert 1
        Assertions.assertTrue(isParked, "The car has not parked.");
        //Since the position of the car moves 2m backwards when it parks, we further check the postion as well

        //Assert 2
        Assertions.assertEquals(prevPos - 2, newPos, "The car has not parked.");

        //Assert 3
        Assertions.assertFalse(Utilities.parking[newPos], "The parking place on RHS is not occupied.");
    }

    //Park() when c1 is true.
    @Test
    public void testParkWhileParked() {
        //Arrange
        //we first drive at least 5meters ahead on the road
        for (int i = 0; i < 5; i++) {
            myCar.MoveForward();
        }
        //Act
        myCar.Park();
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        //in case of parking twice, isParked remain same but the x position must not change.
        myCar.Park();
        whereIs = myCar.WhereIs();
        int newPos = (Integer) whereIs[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has tried to park again.");

    }

    //Park() when c1 is false, c2 is false, c3 is false.
    @Test
    public void testParkWithNoParkingAvailable() {
        //Arrange
        //we first create another car and park it at a specific parking place
        Sensor sensor1 = new UltraSoundSensor("sensor1");
        Sensor sensor2 = new UltraSoundSensor("sensor2");
        Car aCar = new Car(sensor1, sensor2);
        //move a car to the first parking place
        for (int i = 0; i < 5; i++) {
            aCar.MoveForward();
        }
        //park the car at the first parking place
        aCar.Park();
        Object[] whereIs = aCar.WhereIs();

        //Now drive our car at the same spot where parking is occupied
        for (int i = 0; i < 5; i++) {
            myCar.MoveForward();
        }
        whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        myCar.Park();
        whereIs = myCar.WhereIs();
        int newPos = (Integer) whereIs[0];
        boolean isParked = (Boolean) whereIs[1];

        //Assert 1
        Assertions.assertFalse(isParked, "The car has parked where no parking is available.");

        //Assert 2
        Assertions.assertEquals(prevPos, newPos, "The car has tried to park twice.");
    }

    // UnPark() when c1 is true.
    @Test
    public void testUnPark() {
        //Arrange
        //we must first park in order to test unPark, and we must drive at least 5m to successfully park
        for (int i = 0; i < 5; i++) {
            myCar.MoveForward();
        }
        myCar.Park();
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        myCar.UnPark();
        whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];
        int newPos = (Integer) whereIs[0];

        //Assert 1
        Assertions.assertFalse(isParked, "The car is still parked.");

        //Assert 2 - we gotta move 2m forward when unparking
        Assertions.assertEquals(prevPos + 2, newPos, "The car is still parked and has not changed in position.");

        //Assert 3
        Assertions.assertTrue(Utilities.parking[newPos], "The parking place on RHS is still occupied after unparking.");
    }

    // UnPark() when c1 is false.
    @Test
    public void testUnParkWhileNotParked() {
        //Act
        myCar.UnPark();
        Object[] whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];

        //Assert
        Assertions.assertFalse(isParked, "The car has unparked when it was not even parked.");
    }

    // UnPark() at the end of the street”
    @Test
    public void testUnParkAtEndOfStreet() {
        //Arrange
        //we must first park in order to test unPark, and we must drive at least 5m to successfully park
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            myCar.MoveForward();
        }
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        myCar.Park();
        Object[] whereIs1 = myCar.WhereIs();
        prevPos = (Integer) whereIs1[0];
        //Act
        myCar.UnPark();
        whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];
        int newPos = (Integer) whereIs[0];

        //Assert
        Assertions.assertFalse(isParked, "The car is still parked.");

        //Assert - since we move 2m forward when unparking
        Assertions.assertEquals(prevPos + 2, newPos, "The car is still parked and has not changed in position.");
    }

    //test: unparks the car and drive to the end of the street. also attempts to drive past end of street

    //test: drives car from start, attempts to drive where it cant park, tries to back and drive
    //past start point.

    //test: tries to unpark when its not parked.


}
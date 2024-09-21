package com.tves;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarTest {

    private Car myCar;

    @BeforeAll
    public void createCar(){
        //Arrange
        Sensor frontSensor = new UltraSoundSensor("frontSensor");
        Sensor backSensor = new UltraSoundSensor("backSensor");
        this.myCar = new Car(frontSensor, backSensor);
    }

    // Testing the constructor or initialization of a car
    @Test
    public void testConstructor() {
    //Test initialization of car
        //Act
        Sensor frontSensor = new UltraSoundSensor("frontSensor");
        Sensor backSensor = new UltraSoundSensor("backSensor");
        Car aCar = new Car(frontSensor, backSensor);

        Assertions.assertInstanceOf(Car.class, aCar,"Car is not initialized as an object of Car");
    }

    //MoveForward() when c1 is false, c2 is true, and c3 is false.
    @Test
    public void testMoveForward() {
        //Arrange
        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];

        //Act
        Object[] moveResult = myCar.MoveForward();
        int currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition+1, currentPosition, "The car has not moved 1m forward.");
    }

    //MoveForward() when c1 is true.
    @Test
    public void testMoveForwardWhileParked(){
        //Act
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
    public void testMoveForwardAtEndOfStreet(){
        //Act
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];
        //move forward until end of the street
        while(prevPos < Utilities.parkingStreetLength - 1){
            Object[] moveResult = myCar.MoveForward();
            prevPos = (Integer) moveResult[0];
        }
        // now move one more time at the end of the street
        Object[] moveResult = myCar.MoveForward();
        int newPos = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has moved 1m forward at the end of the street.");
    }

    //MoveForward() when c1 is false, c2 is true, c3 is false, and c4 is true
    @Test
    public void testMoveForwardParkingAvailable(){
        //Act
        Object[] moveResult = myCar.MoveForward();
        //situation of the registered parking places
        ArrayList<Integer> prevPP = (ArrayList<Integer>) moveResult[1];
        int prevSize = prevPP.size();
        //everytime it moves forward, it should register new available parking places on the RHS.
        moveResult = myCar.MoveForward();
        //situation of the registered parking places
        ArrayList<Integer> newPP = (ArrayList<Integer>) moveResult[1];
        int newSize = newPP.size();
        //Assert
        Assertions.assertEquals(prevSize+1, newSize, "MoveForward has not registered new available parking place on RHS.");
    }

    //     * MoveBackward() when c1 is false, c2 is true, and c3 is false.
    @Test
    public void testMoveBackward(){
        //Act
        //first move forward
        myCar.MoveForward();
        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];
        //then move backwards
        Object[] moveResult = myCar.MoveBackward();
        int currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition-1, currentPosition, "The car has moved 1m backward.");

    }

    //MoveBackward() when c1 is true.
    @Test
    public void testMoveBackwardWhileParked(){
        //Act
        //first move forward
        myCar.MoveForward();
        //Then park
        myCar.Park();
        //Then check position
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        Object[] moveResult = myCar.MoveBackward();
        int newPos = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has moved 1m forward while at parking state.");
    }

    // MoveBackward() when c1 is false, c2 is true, and c3 is true.
    @Test
    public void testMoveBackwardAtStartOfStreet(){
        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];

        //Act
        Object[] moveResult = myCar.MoveBackward();
        int currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition, currentPosition, "The car has moved 1m backward from starting position.");
    }
    //MoveBackward() when c1 is false, c2 is true, c3 is false, and c4 is true.
    @Test
    public void testMoveBackwardParkingAvailable(){
        //Act
        Object[] moveResult = myCar.MoveBackward();
        //situation of the registered parking places
        ArrayList<Integer> prevPP = (ArrayList<Integer>) moveResult[1];
        int prevSize = prevPP.size();
        //everytime it moves forward, it should register new available parking places on the RHS.
        moveResult = myCar.MoveBackward();
        //situation of the registered parking places
        ArrayList<Integer> newPP = (ArrayList<Integer>) moveResult[1];
        int newSize = newPP.size();
        //Assert
        Assertions.assertEquals(prevSize+1, newSize, "MoveForward has not registered new available parking place on RHS.");
    }

    /*
    //MoveForward() when c1 is false, c2 is true, c3 is false, and c4 is false.
    @Test
    public void testMoveForwardNotParkingAvailable(){
        //It is complicated to arrange a scenario for this test case
        //At least one parking place must be occupied
        //and for that, sensors must detect another object at that parking place.
    }
    */
    /**
     *
     * MoveForward() when c1 is false and c2 is false.
    @Test
    public void testMoveForwardBeyondStreet(){
        //Not possible to execute this testcase because we cannot arrange a scenario for this test case
        // The Car class do not allow updating xPosition of the car directly.
        //rather it is updated as MoveForward.
        //No amount of Moving forward will make the car go beyond the street.
    }
    */
/*
    @Test
    public void testisEmpty(){

        //Act
        //int distance = myCar.isEmpty();
        //Assert
   //     Assertions.assertNotNull(distance, "The sensor data is null.");
    }

    @Test
    public void testisEmpty(){

    }
    @Test
    public void testPark(){
        //Act
        myCar.Park();
        Object[] whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];

        //Assert
        Assertions.assertTrue(isParked, "The car has not parked.");
    }

    @Test
    public void testUnPark(){
        //Act
        myCar.UnPark();
        Object[] whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];

        //Assert
        Assertions.assertFalse(isParked, "The car is still parked.");
    }
*/
}
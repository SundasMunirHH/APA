package com.tves;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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

    @Test
    public void testConstructor() {
    //Test initialization of car
        //Act
        Sensor frontSensor = new UltraSoundSensor("frontSensor");
        Sensor backSensor = new UltraSoundSensor("backSensor");
        Car aCar = new Car(frontSensor, backSensor);

        Assertions.assertInstanceOf(Car.class, aCar,"Car is not initialized as an object of Car");
    }


    @Test
    public void testMoveForward() {

        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];

        //Act
        Object[] moveResult = myCar.MoveForward();
        int currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition+1, currentPosition, "The car has not moved exactly 1m forward.");

    }

    @Test
    public void testMoveBackward(){
        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];

        //Act
        Object[] moveResult = myCar.MoveBackward();
        int currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition, currentPosition, "The car has moved 1m backward from starting position.");

        //Act
        //first move forward
        myCar.MoveForward();
        //first move forward
        myCar.MoveForward();
        //then move backwards
        moveResult = myCar.MoveBackward();
        currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition+1, currentPosition, "The car has moved 1m backward.");

    }

    @Test
    public void testisEmpty(){

        //Act
        int distance = myCar.isEmpty();
        //Assert
        Assertions.assertNotNull(distance, "The sensor data is null.");
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
    public void testMoveForwardAfterPark(){
        //Act
        myCar.Park();
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];
        boolean situation = (Boolean) whereIs[1];
        System.out.println("isparked? " + situation);
        //Act
        Object[] moveResult = myCar.MoveForward();
        int newPos = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has moved 1m forward at parking state.");

    }

    @Test
    public void testMoveForwardBeyondStreetLength(){
        //Act
        myCar.Park();
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];
        boolean situation = (Boolean) whereIs[1];
        System.out.println("isparked? " + situation);
        //Act
        Object[] moveResult = myCar.MoveForward();
        int newPos = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has moved 1m forward at parking state.");

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

}
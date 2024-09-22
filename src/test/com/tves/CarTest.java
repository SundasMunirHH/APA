package com.tves;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarTest {

    private Car myCar;

    @BeforeEach
    public void createCar(){
        //Arrange
        Sensor sensor1 = new UltraSoundSensor("sensor1");
        Sensor sensor2 = new UltraSoundSensor("sensor2");
        this.myCar = new Car(sensor1, sensor2);
    }

    // Testing the constructor or initialization of a car
    @Test
    public void testConstructor() {
    //Test initialization of car
        //Act
        Sensor sensor1 = new UltraSoundSensor("sensor1");
        Sensor sensor2 = new UltraSoundSensor("sensor2");
        Car aCar = new Car(sensor1, sensor2);
        Assertions.assertInstanceOf(Car.class, aCar,"Car is not initialized as an object of Car");
    }

    //MoveForward() when c1 is false, c2 is true, and c3 is false from decision table.
    @Test
    public void testMoveForward() {
        myCar.setParkingPlaces(10, 7);
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
        myCar.setParkingPlaces(10, 7);
        //Act
        for(int i = 0; i < 6; i++){
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
    public void testMoveForwardAtEndOfStreet(){
        myCar.setParkingPlaces(10, 7);
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

    //     * MoveBackward() when c1 is false, c2 is true, and c3 is false.
    @Test
    public void testMoveBackward(){
        myCar.setParkingPlaces(10, 7);
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
        myCar.setParkingPlaces(10, 7);
        //Act
        //first move forward
        for(int i = 0; i < 6; i++){
            myCar.MoveForward();
        }
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

    @Test
    public void testisEmpty(){
        //Act
        int distance = myCar.isEmpty();
        //Assert
        Assertions.assertNotNull(distance, "The sensor data is null.");
    }

    //Park() when c1 is false, c2 is true. We name this method “testParkAtStart()”
    @Test
    public void testParkAtStart(){
        //Arrange
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        myCar.Park();
        whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];
        int newPos = (Integer) whereIs[0];

        //Assert
        Assertions.assertFalse(isParked, "The car has parked at the beginning of the street (before driving 5m).");
        //Since the position of the car moves 2m backwards when it parks, we further check the postion as well
        //Assert
        Assertions.assertEquals(prevPos,newPos, "The car has parked.");
    }

    //Park() when c1 is false, c2 is false, c3 is true. We name this method “testPark()”
    @Test
    public void testPark(){
        myCar.setParkingPlaces(10, 7);
        //Arrange
        //we first drive at least 5 meters ahead on the road
        for (int i =0; i<5;i++){
            myCar.MoveForward();
        }
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        myCar.Park();
        whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];
        int newPos = (Integer) whereIs[0];

        //Assert
        Assertions.assertTrue(isParked, "The car has not parked.");
        //Since the position of the car moves 2m backwards when it parks, we further check the postion as well
        //Assert
        Assertions.assertEquals(prevPos-2,newPos, "The car has not parked.");
    }

    //Park() when c1 is true, we name this method “testParkWhileParked()”
    @Test
    public void testParkWhileParked(){
        myCar.setParkingPlaces(10, 7);
        //Arrange
        //we first drive at least 5meters ahead on the road
        for (int i =0; i<5;i++){
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

    //Park() when c1 is false, c2 is false, c3 is false. We name this method “testParkWithNoParkingAvailable()”
    @Test
    public void testParkWithNoParkingAvailable(){
        myCar.setParkingPlaces(1, 0);
        //Arrange
        //we first drive at a spot where parking is occupied
        //Since we have deliberately made the parking unavailable if(i%10 == 7 || i%10 == 8 || i%10 == 9){

        for (int i =0; i<18;i++){
            myCar.MoveForward();
        }
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        //Act
        myCar.Park();
        whereIs = myCar.WhereIs();
        int newPos = (Integer) whereIs[0];
        boolean isParked = (Boolean) whereIs[1];

        //Assert
        Assertions.assertFalse(isParked, "The car has parked at when no parking is available.");

        //Assert
        Assertions.assertEquals(prevPos, newPos, "The car has tried to park twice.");
    }

    // UnPark() when c1 is true. We name this method “testUnPark()”
    @Test
    public void testUnPark(){
        myCar.setParkingPlaces(10, 7);
        //Arrange
        //we must first park in order to test unPark, and we must drive at least 5m to successfully park
        for (int i =0; i<5;i++){
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

        //Assert
        Assertions.assertFalse(isParked, "The car is still parked.");

        //Assert - we gotta move 2m forward when unparking
        Assertions.assertEquals(prevPos+2, newPos, "The car is still parked and has not changed in position.");
    }

    // UnPark() when c1 is false. We name this method “testUnParkWhileNotParked()”
    @Test
    public void testUnParkWhileNotParked(){
        myCar.setParkingPlaces(10, 7);
        //Act
        myCar.UnPark();
        Object[] whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];

        //Assert
        Assertions.assertFalse(isParked, "The car has unparked when it was not even parked.");
    }

    // UnPark() at the end of the street”
    @Test
    public void testUnParkAtEndOfStreet(){
        myCar.setParkingPlaces(499, 499);
        //Arrange
        //we must first park in order to test unPark, and we must drive at least 5m to successfully park
        for (int i =0; i< Utilities.parkingStreetLength - 1 ;i++){
            myCar.MoveForward();
        }
        Object[] whereIs = myCar.WhereIs();
        int prevPos = (Integer) whereIs[0];

        myCar.Park();
        Object[] whereIs1 = myCar.WhereIs();
        prevPos = (Integer) whereIs1[0];
    boolean isP = (Boolean) whereIs1[1];
        //Act
        myCar.UnPark();
        whereIs = myCar.WhereIs();
        boolean isParked = (Boolean) whereIs[1];
        int newPos = (Integer) whereIs[0];

        //Assert
        Assertions.assertFalse(isParked, "The car is still parked.");

        //Assert - we gotta move 2m forward when unparking
        Assertions.assertEquals(prevPos+2, newPos, "The car is still parked and has not changed in position.");
    }
/*
//3 tests for park, and 2 for unpark
   //MoveForward() when c1 is false, c2 is true, c3 is false, and c4 is true
    /*@Test
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
    }*/
    //MoveBackward() when c1 is false, c2 is true, c3 is false, and c4 is true.
    /*@Test
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
    }*/

    /*
    //MoveForward() when c1 is false, c2 is true, c3 is false, and c4 is false.
    @Test
    public void testMoveForwardNotParkingAvailable(){
        //It is complicated to arrange a scenario for this test case
        //At least one parking place must be occupied
        //and for that, sensors must detect another object at that parking place.
    }
    */
    /*
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
    public void testisEmpty1NoisySensor(){
        //it is difficult to deliberately make one sensor data as noisy data
        //Act
        int distance = myCar.isEmpty();
        //Assert
        Assertions.assertNotNull(distance, "The sensor data is null.");
    }
*/
}
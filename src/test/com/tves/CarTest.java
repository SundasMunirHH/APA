package com.tves;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import static org.junit.jupiter.api.Assertions.*;
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
    public void testMoveForward() {

        Object[] whereIs = myCar.WhereIs();
        int prevPosition = (Integer) whereIs[0];

        //Act
        Object[] moveResult = myCar.MoveForward();
        int currentPosition = (Integer) moveResult[0];

        //Assert
        Assertions.assertEquals(prevPosition+1, currentPosition, "The car has not moved exactly 1m forward.");

    }

}
package com.tves;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockConstruction;


import org.mockito.junit.jupiter.MockitoExtension;

//Tell mockito to create mocks based on @Mock annotation
@ExtendWith(MockitoExtension.class)
class CarMockitoTest {
    @Mock
    //Mock the Car instance
    Sensor MockFS1, MockFS2;
    Car CarMock;

    @Test
    public void testMockitoConstructor() {
        //Stubbing
        Car CarMock = mock(Car.class);
        //Configure Mock to return true when isEmpty() is called.
        when(CarMock.isEmpty()).thenReturn(201);

        //MockedConstruction<Car> CarMockito = mockConstruction(Car.class);

      /*  try(MockedConstruction<Car> carMock = mockConstruction(Car.class)){
            //Arrange
            Sensor sensor1 = new UltraSoundSensor("sensor1");
            Sensor sensor2 = new UltraSoundSensor("sensor2");
            when(carMock.constructed().WhereIs());

        }*/

/*
       // when(new Car(any(Car.class).thenReturn(null)));

       CarMock = new Car(sensor1, sensor2);
        //The car object should not be null
        assertNotNull(CarMock);

        //assertEquals()

        //Testing initialization of car at an expected state
        Object[] whereIs = CarMock.WhereIs();
        int startingPosition = (Integer) whereIs[0];
        boolean isParked = (Boolean) whereIs[1];

        assertFalse(isParked);
        assertEquals(0,startingPosition);

        //whereIs = CarMock.WhereIs();
        //int newPos = (Integer) whereIs[0];
       // when(CarMock.MoveForward()).thenReturn(new Object[]{1, this.registeredParkingPlaces};);
*/
    }
}
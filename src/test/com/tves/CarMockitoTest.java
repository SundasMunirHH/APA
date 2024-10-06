package com.tves;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

//Tell mockito to create mocks based on @Mock annotation
@ExtendWith(MockitoExtension.class)
class CarMockitoTest {
    @Mock
    //Mock Sensors
    Sensor mockedSensor1, mockedSensor2;
    //Mock Actuator
    Actuator mockedActuator;
    //Inject the mock dependencies into constructor in Car
    @InjectMocks
    Car mockedCar;

    @BeforeEach
    public void createCarMock() {
        mockedCar = mock(Car.class);
    }

    @Test
    public void testMockitoScenario1() {
        //Scenario# 1
        //Starts at the beginning of the street,
        // Lenient stubbing (Mockito will not complain if this stub is unused)
        lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{0, false});

        //The sensor data should be mocked such that it represent a street with three parking places of mutually different sizes,
        // one should be not enough for safe parking and the other two enough for parking.
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            Utilities.parking[i] = true;//currently all parking places are available, we can hardcode to make some parking places unavailable
        }
        int pp = 1;
        //Moves along the street and scan the available parking places,
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            mockedCar.MoveForward();
            verify(mockedCar, times(pp)).MoveForward();
            //if moveForward works correctly, the WhereIs() method will return a position that is i+1
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i+1, false});
            if(i == Utilities.parkingStreetLength/2) {
                // One of the sensors should be broken halfway in the middle of the scenario
                // (i.e., when the car has reached the middle of the street while moving forward)
                Utilities.noiseS1 = 100;
            }
            pp++;
          }
        Object[] whereIs = mockedCar.WhereIs();
        int pos = (Integer) whereIs[0];
        System.out.println("Current Pos: "+ pos );//just varifying that the car is at the end of the street
        //Verify that move forward was called 499 times in above loop
        verify(mockedCar, times(499)).MoveForward();

        int p = 1;
        //Moves backwards until the most efficient parking place (the smallest available parking where it can still park safely),
        for (int i = Utilities.parkingStreetLength-1; i>0; i--) {
            mockedCar.MoveBackward();
            verify(mockedCar, times(p)).MoveBackward();
            //verify that the car is moving 1m backward in each iteration
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i-1, false});
            //lets try to park the mocked car
            mockedCar.Park();
            //verify that the car parked
            verify(mockedCar, times(p)).Park();

            whereIs = mockedCar.WhereIs();
            pos = (Integer) whereIs[0];
            boolean isParked = (Boolean) whereIs[1];
            System.out.println("Pos now: "+ pos + " is parked? " + isParked);//IMP: why is the car not parking?
            if(isParked || pos == 0) {
                //Parks the car,
                //The car should be parked here because all the parking places are available
                System.out.println(isParked + "?");
                break;
            }
            p++;
        }

        //Unparks the car and drive to the end of the street.
        mockedCar.UnPark();
        //verify that the car unparked
        verify(mockedCar).UnPark();

        whereIs = mockedCar.WhereIs();
        int i = (Integer) whereIs[0];
        System.out.println("Where is the car now? " + i);
        while ( i < Utilities.parkingStreetLength - 1) {
            mockedCar.MoveForward();
            //Verify that move forward was called in each loop iteration
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i+1, false});
            i++;
        }
        Utilities.noiseS1 = 0;//Resetting the noise in the sensor
    }

    @Test
    public void testMockitoScenario2() {
        //Stubbing
    }

    @Test
    public void testMockitoConstructor() {
        //Stubbing
        //carMock.isEmpty();
        mockedCar.WhereIs();
        //verify(carMock).isEmpty();
        verify(mockedCar).WhereIs();
        //since we do not really call the WhereIS method, not using linennt would cause the
        // error of unnecessary stubbing.
        // Lenient stubbing (Mockito will not complain if this stub is unused)
        lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{0, false});
        //Configure Mock to return true when isEmpty() is called.
       // when(CarMock.isEmpty()).thenReturn(201);

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

    // We need a single mock whose methods must be invoked in a particular order
}
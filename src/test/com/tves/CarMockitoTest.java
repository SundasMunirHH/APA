package com.tves;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

//Tell mockito to create mocks based on @Mock annotation
@ExtendWith(MockitoExtension.class)
class CarMockitoTest {
    @Mock
    Sensor mockedSensor1, mockedSensor2;
    Car mockedCar;

    @BeforeEach
    public void createCarMock() {
        mockedCar = spy(new Car(mockedSensor1, mockedSensor2));
        //By default all parking places are available
        //Each scenario will make some parking places occupied
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            Utilities.parking[i] = true;
        }
    }

   @Test
    public void testCarInit(){
        // Checking the parameters were asserted
        assertNotNull(mockedCar);
        assertNotNull(mockedSensor1);
        assertNotNull(mockedSensor2);
    }

    @Test
    public void testMockitoScenario1() {
        //Scenario# 1
        //Starts at the beginning of the street,
        // Lenient stubbing (Mockito will not complain if this stub is unused)
        lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{0, false});

        //To specify that only a few parking places must be available
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            //We only make 2 parking places to be available and rest of the street is occupied
            // One parking place is second last place - close to the end of the street
            // Another is the first parking place at the beginning
            if ((i >= 490 && i < 495) || (i >= 0 && i <= 5)){
                Utilities.parking[i] = true;
            }else{
                Utilities.parking[i] = false;
            }
        }
        int t = 1;
        //Moves along the street and scan the available parking places,
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            mockedCar.MoveForward();
            verify(mockedCar, times(t)).MoveForward();
            //if moveForward works correctly, the WhereIs() method will return a position that is i+1
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i+1, false});
            if(i == Utilities.parkingStreetLength/2) {
                // One of the sensors should be broken halfway in the middle of the scenario
                Utilities.noiseS1 = 100;
            }
            t++;
          }

        //Verify that move forward was called 499 times in the above loop
        verify(mockedCar, times(499)).MoveForward();

        t = 1;
        //Moves backwards until the most efficient parking place (the smallest available parking where it can still park safely),
        for (int i = Utilities.parkingStreetLength-1; i>0; i--) {
            mockedCar.MoveBackward();
            //verify that Move backward is called in each iteration
            verify(mockedCar, times(t)).MoveBackward();
            //verify that the car is moving 1m backward in each iteration
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i-1, false});

            // Since only two parking places are available
            lenient().when(mockedCar.isAvailableParkingPlace()).thenReturn(Utilities.parking[i]);

            if(mockedCar.isAvailableParkingPlace()) {
                //Parks the car,
                //The car should be parked here because all the parking places are available
                //lets try to park the mocked car
                mockedCar.Park();
                //verify that the parked method was called
                verify(mockedCar).Park();
                //verify that the car parked at the first available parking place that it found (at the end of the street)
                lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{493, true});
                break;
            }
            t++;
        }

        //Unparks the car and drive to the end of the street.
        mockedCar.UnPark();
        //verify that the car unparked
        verify(mockedCar).UnPark();
        //verify that the car unparked from the position that it was parked in
        lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{493, false});

        Object[] whereIs = mockedCar.WhereIs();
        int pos = (Integer) whereIs[0];

        while (pos < Utilities.parkingStreetLength - 1) {
            mockedCar.MoveForward();
            //Verify that move forward was called in each loop iteration
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{pos+1, false});
            pos++;
        }
        Utilities.noiseS1 = 0;//Resetting the noise in the sensor
    }

    @Test
    public void testMockitoScenario2() {
        //Scenario# 2
        //Starts at the beginning of the street,
        lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{0, false});
        Random randomSensorVal = new Random();

        int t = 1;
        //Moves along the street and scan the available parking places,
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            // Randomizes noise value between 0-10
            Utilities.noiseS1 = randomSensorVal.nextInt(10);
            mockedSensor1.getSensorData(i, Utilities.noiseS1);
            //Mock the sensordata with noise values
            when(mockedSensor1.getSensorData(i, Utilities.noiseS1)).thenReturn(20, 20, 23, 18, 2);
            //Verify that the sensor was called correctly
            verify(mockedSensor1, times(5)).getSensorData(i, Utilities.noiseS1);
            // Checking if sensor 2 is broken
            if(Utilities.noiseS2 != 200) {
                Utilities.noiseS2 = randomSensorVal.nextInt(10);
                when(mockedSensor2.getSensorData(i, Utilities.noiseS2)).thenReturn(22, 20, 40, 19, 21);
                verify(mockedSensor2, times(5)).getSensorData(i, Utilities.noiseS2);
            }
            mockedCar.MoveForward();
            verify(mockedCar, times(t)).MoveForward();
            //if moveForward works correctly, the WhereIs() method will return a position that is i+1
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i+1, false});
            // The sensors are a little noisy but not distrupted
            // Halfway into the street sensor2 will break.
            if(i == Utilities.parkingStreetLength/2) {
              Utilities.noiseS2 = 200;
            }
            t++;
        }/*
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
        Utilities.noiseS1 = 0;//Resetting the noise in the sensor*/
    }

    /*@Test
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

    }*/

    // We need a single mock whose methods must be invoked in a particular order
}
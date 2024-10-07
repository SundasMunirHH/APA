package com.tves;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

//Tell mockito to create mocks based on @Mock annotation
@ExtendWith(MockitoExtension.class)
class CarMockitoTest {
    @Mock
    UltraSoundSensor mockedSensor1, mockedSensor2;
    Car mockedCar;

    @BeforeEach
    public void createCarMock() {
        mockedSensor1 = spy(new UltraSoundSensor("Sensor1"));
        mockedSensor2= spy(new UltraSoundSensor("Sensor2"));

        mockedCar = spy(new Car(mockedSensor1, mockedSensor2));
        //By default all parking places are available
        //Each scenario will make some parking places occupied
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            Utilities.parking[i] = true;
        }
        //Resetting the noise in the sensor
        Utilities.noiseS1 = Utilities.noiseS2 = 0;
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
            //stub and verify that isAvailableParkingPlace() is being called with each move
            mockedCar.isAvailableParkingPlace();
            verify(mockedCar, atLeast(1)).isAvailableParkingPlace();

            // One of the sensors should be broken halfway in the middle of the scenario
            if(i == Utilities.parkingStreetLength/2) {
                Utilities.noiseS1 = 100;
                //mocking sensor data
                mockedSensor1.getSensorData(i, Utilities.noiseS1);

                verify(mockedSensor1).getSensorData(i, Utilities.noiseS1);
                //verify noisy sensor
                lenient().when(mockedSensor1.getSensorData(i, Utilities.noiseS1)).thenReturn(42);
/*
                mockedSensor1.isNoise(any());
                verify(mockedSensor1, atLeast(1)).isNoise(any());
                //verify noisy sensor
                lenient().when(mockedSensor1.isNoise(any())).thenReturn(true);

*/
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
        //Car starts at the beginning of the street,
        lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{0, false});

        Random randomSensorVal = new Random();

        int t = 1;
        //Moves along the street and scan the available parking places,
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            // Randomizes noise value between 0-10
            Utilities.noiseS1 = randomSensorVal.nextInt(10);
            //Mock the sensor data with noise values
            mockedSensor1.getSensorData(i, Utilities.noiseS1);

            if (i >= 230 && i <=235){
                //Sensor 2 works correctly in the middle of the street next to one specific parking place
                Utilities.noiseS2 = 0;
            }else{
                Utilities.noiseS2 = randomSensorVal.nextInt(10);
                mockedSensor2.getSensorData(i, Utilities.noiseS2);

            }
            mockedCar.MoveForward();
            verify(mockedCar, times(t)).MoveForward();
            //if moveForward works correctly, the WhereIs() method will return a position that is i+1
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i+1, false});
            t++;
        }

        //Verify that move forward was called 499 times in above loop
        verify(mockedCar, times(499)).MoveForward();

        int p = 1;
        //Moves backwards until the most efficient parking place (the smallest available parking where it can still park safely),
        for (int i = Utilities.parkingStreetLength-1; i>0; i--) {
            mockedCar.MoveBackward();
            verify(mockedCar, times(p)).MoveBackward();
            //verify that the car is moving 1m backward in each iteration
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i-1, false});

            // One sensor detected only one parking place as available/vacant, between 230 and 235
            if (i >= 230 && i <=235){
                lenient().when(mockedCar.isAvailableParkingPlace()).thenReturn(true);
            }else {
                lenient().when(mockedCar.isAvailableParkingPlace()).thenReturn(false);
            }

            mockedCar.isAvailableParkingPlace();

            if(mockedCar.isAvailableParkingPlace()) {
                //Parks the car,
                mockedCar.Park();

                //verify that the park method was called
                verify(mockedCar).Park();

                //verify that the car parked at the only available parking place that it found
                lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{234, true});
                break;
            }
            p++;
        }
        //Resetting the noise in the sensor
        Utilities.noiseS1 = Utilities.noiseS2 = 0;
    }

    @Test
    public void testMockitoScenario_Sensors() {
        stub_verify_sensors();
        mockedCar.isAvailableParkingPlace();
        verify(mockedCar,atLeast(1)).isAvailableParkingPlace();
     //Just trying every possible combination of either sensor going noisy or both working.
        Utilities.noiseS1 = 100;
        Utilities.noiseS2 = 0;
        stub_verify_sensors();
        mockedCar.isEmpty();
        verify(mockedCar,atLeast(1)).isEmpty();
        Utilities.noiseS1 = 0;
        Utilities.noiseS2 = 100;
        stub_verify_sensors();
        mockedCar.isEmpty();
        verify(mockedCar,atLeast(1)).isEmpty();
        Utilities.noiseS1 = 100;
        Utilities.noiseS2 = 100;
        stub_verify_sensors();
        mockedCar.isEmpty();
        verify(mockedCar,atLeast(1)).isEmpty();
    }

   // @Test
    void stub_verify_sensors()
    {
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {

            //mocking sensor data
            mockedSensor1.getSensorData(i, Utilities.noiseS1);
            mockedSensor2.getSensorData(i, Utilities.noiseS2);

            verify(mockedSensor1, atLeast(1)).getSensorData(i, Utilities.noiseS1);
            verify(mockedSensor2, atLeast(1)).getSensorData(i, Utilities.noiseS2);

            //verify noisy sensor
            //lenient().when(mockedSensor1.getSensorData(i, Utilities.noiseS1)).thenReturn(42);
            int[] intArray = {Utilities.noiseS1,Utilities.noiseS1,Utilities.noiseS1,Utilities.noiseS1,Utilities.noiseS1};
            mockedSensor1.aggregatedValue(intArray);
            verify(mockedSensor1, atLeast(1)).aggregatedValue(intArray);
            mockedSensor1.isNoise(intArray);
            mockedSensor2.isNoise(intArray);
            verify(mockedSensor1, atLeast(1)).isNoise(intArray);
            verify(mockedSensor2, atLeast(1)).isNoise(intArray);
            //verify noisy sensor
            lenient().when(mockedSensor1.isNoise(intArray)).thenReturn(Utilities.noiseS1 == 100?true:false);
            lenient().when(mockedSensor2.isNoise(intArray)).thenReturn(Utilities.noiseS2 == 100?true:false);
        }
    }
    @Test
    public void testMockitoScenario3() {
        //Scenario# 3
        //Car starts at the beginning of the street,
        lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{0, false});

        //try to park at the beginning of the stret
        mockedCar.Park();
        //verify that the park method was called
        verify(mockedCar).Park();

        // Since all parking places are available
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            mockedCar.isAvailableParkingPlace();
            verify(mockedCar, atLeast(1)).isAvailableParkingPlace();
            lenient().when(mockedCar.isAvailableParkingPlace()).thenReturn(true);
        }

        int t = 1;
        //Moves along the street and scan the available parking places,
        for (int i = 0; i < Utilities.parkingStreetLength - 1; i++) {
            mockedCar.MoveForward();
            verify(mockedCar, times(t)).MoveForward();
            //if moveForward works correctly, the WhereIs() method will return a position that is i+1
            lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i+1, false});
            t++;
            if (i == Utilities.parkingStreetLength/2){
                //Parks the car,
                mockedCar.Park();

                //verify that the park method was called
                verify(mockedCar, times(2)).Park();

                //verify that the car parked at the only available parking place that it found
                lenient().when(mockedCar.WhereIs()).thenReturn(new Object[]{i, true});

                //Verify that the car can not move forward while parked
                mockedCar.MoveForward();
                verify(mockedCar, times(t)).MoveForward();
                lenient().when(mockedCar.MoveForward()).thenReturn(new Object[]{i, new ArrayList<Boolean>()});

                //Verify that the car can not move backwards while parked
                mockedCar.MoveBackward();
                verify(mockedCar, times(1)).MoveBackward();
                lenient().when(mockedCar.MoveBackward()).thenReturn(new Object[]{i, new ArrayList<Boolean>()});

                //
                mockedCar.UnPark();
                mockedCar.UnPark();

                verify(mockedCar, times(2)).UnPark();
                break;
            }
        }

      //  mockedCar.MoveForward();
       // verify(mockedCar, times(t)).MoveForward();

        //Parks the car,
     //   mockedCar.Park();

        //verify that the park method was called
       // verify(mockedCar, times(2)).Park();

    }
}
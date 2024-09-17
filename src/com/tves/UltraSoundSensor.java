package com.tves;

public class UltraSoundSensor implements Sensor{

    /**
     * The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     */
    @Override
    public int getSensorData() {
        // Return data from the sensor
        return 0; // Example value in meters
    }

}

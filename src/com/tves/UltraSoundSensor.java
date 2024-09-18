package com.tves;
import java.util.ArrayList;

public class UltraSoundSensor implements Sensor{

    /** Since we can have a front and a back sensor, we need a name to produce error reports. */
    private String sensorName;

    public UltraSoundSensor(String name){
        this.sensorName = name;
    }
    /**
     * The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     */
    @Override
    public int getSensorData() {
        // Return data from the sensor
        return 0; // Example value in meters, 0 means no object detected
    }

    @Override
    public boolean isNoise(int value) {
        // Does the value belong to noisy state
        return value < 0 || value > 200 ? true : false;
    }

    @Override
    public int aggregatedValue(int[] readings) {
        ArrayList<Integer> validReadings = new ArrayList<>();

        // Loop through sensor readings and filter noise
        for (int reading : readings) {
            if (!isNoise(reading)) {
                validReadings.add(reading);
            }
        }

        // If too many noisy readings, disregard this sensor
        if (validReadings.size() < 3) { // Arbitrary threshold: if less than 3 valid readings, ignore
            System.out.println(this.sensorName + " produced mostly noisy data and will be disregarded.");
            return -1;
        }

        // Calculate the average of valid readings
        int sum = 0;
        for (int reading : validReadings) {
            sum += reading;
        }
        return sum / validReadings.size();
    }

}

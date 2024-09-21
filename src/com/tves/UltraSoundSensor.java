package com.tves;
import java.util.ArrayList;
import java.util.Random;

public class UltraSoundSensor implements Sensor{

    /** Since we can have two sensors, we need a name to produce error reports. */
    private String sensorName;

    public UltraSoundSensor(String name){
        this.sensorName = name;
    }
    /**
     * The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     */
    @Override
    public int getSensorData(boolean[] data, int position) {
        if(data[position] == false){
            return (int) ((Math.random()*(55-45))+45);
        }
        return (int) ((Math.random()*(195-185))+185);
    }

    @Override
    public boolean isNoise(int[] sensorData) {
        // decide on a threshold for the data to be considered noisy
        int maxIndex = 0, minIndex = 0, threshold = 10;
        // finding max and min value to compare them and find if they are significant
        for(int i = 0; i < sensorData.length; i++){
            // going through data set to search for the max and min value
            if(sensorData[i] >= sensorData[maxIndex]){
                maxIndex = i;
            }
            if(sensorData[i] <= sensorData[minIndex]){
                minIndex = i;
            }
        }
        // if the difference is high then it is a noisy sensor, we can disregard data from this sensor
        if(sensorData[maxIndex] - sensorData[minIndex] > threshold){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int aggregatedValue(int[] readings) {
        // If too many noisy readings, disregard this sensor
        if (isNoise(readings)) { // Arbitrary threshold: if less than 3 valid readings, ignore
            System.out.println(this.sensorName + " produced noisy data and will be disregarded.");
            return -1;
        }

        // Calculate the average of valid readings
        int sum = 0;
        for (int reading : readings) {
            sum += reading;
        }
        return sum / readings.length;
    }

}

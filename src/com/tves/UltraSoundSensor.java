package com.tves;

public class UltraSoundSensor implements Sensor {

    /**
     * Since we can have two sensors, we need a name to produce error reports.
     */
    private String sensorName;

    public UltraSoundSensor(String name) {
        this.sensorName = name;
    }

    /**
     * The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     */
    @Override
    public int getSensorData(int position, int noise) {
        //since a sensor has a range of 200m, it can detect anther object 100 meters ahead of it and 100 meters behind it
        //We for sure do not want to detect more or less than available data length
        int startingRange = position - 100 < 0 ? 0 : position - 100;
        int endingRange = position + 100 >= Utilities.parking.length ? Utilities.parking.length - 1 : position + 100;
        int closesObjectAtRHS = endingRange + 1; //starting with a bigger value
        for (int i = startingRange; i <= endingRange; i++) {
            if (!Utilities.parking[i]) {//the position within 200 meter is occupied
                //what is the shortest +ive distance to occupied spot
                int dis = Math.abs(position - i);
                if (closesObjectAtRHS > dis) {
                    closesObjectAtRHS = dis;
                }
            }
        }
        //We need test cases where we deliberately introduce noise to some sensors
        //A well functioning sensor has 0 noise.
        closesObjectAtRHS = closesObjectAtRHS + (int) (Math.random() * noise);
        return closesObjectAtRHS;
      /*  if(!data[position]){
            return (int) ((Math.random()*(55-45))+45);
        }
        return (int) ((Math.random()*(195-185))+185);*/
    }

    @Override
    public boolean isNoise(int[] sensorData) {
        // decide on a threshold for the data to be considered noisy
        int maxIndex = 0, minIndex = 0, threshold = 10;
        // finding max and min value to compare them and find if they are significant
        for (int i = 0; i < sensorData.length; i++) {
            // going through data set to search for the max and min value
            if (sensorData[i] >= sensorData[maxIndex]) {
                maxIndex = i;
            }
            if (sensorData[i] <= sensorData[minIndex]) {
                minIndex = i;
            }
        }
        // if the difference is high then it is a noisy sensor, we can disregard data from this sensor
        if (sensorData[maxIndex] - sensorData[minIndex] > threshold) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int aggregatedValue(int[] readings) {
        // Calculate the average of valid readings
        int sum = 0;
        for (int reading : readings) {
            sum += reading;
        }
        return sum / readings.length;
    }

}

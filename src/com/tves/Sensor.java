package com.tves;


public interface Sensor {
    /**
     * @Description The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     *
     * @param position is where the sensor takes its input and noise is out-of-bound value
     * @return: int sensor data or its reading
     */
    int getSensorData(int position, int noise);


    /**
     * @Description If the sensor inputs are not in the range of 0 to 200, it indicates noise.
     *
     * @param sensorData representing at least 5 readings from a sensor
     * @return boolean value with true indicating noisy reading
     */
    boolean isNoise(int[] sensorData);

    /**
     * @Description Aggregate valid readings/inputs from a sensor
     *
     * @param readings from sensors
     * @return int value
     */
    int aggregatedValue(int[] readings);
}

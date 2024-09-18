package com.tves;

public interface Sensor {
    /**
     * @Description: The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     *
     * @return: sensor data or its reading
     */
    int getSensorData();

    /**
     * @Description If the sensor inputs are not integers in the range of 0 to 200, it indicates noise.
     *
     * @return boolean value with true indicating noisy reading
     */
    boolean isNoise(int value);

    /**
     * @Description Aggregate valid readings/inputs from a sensor
     *
     * @param readings from sensors
     * @return int value
     */
    int aggregatedValue(int[] readings);
}

package com.tves;


public interface Sensor {
    /**
     * @Description The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     *
     * @Pre-condition:
     * @Post-condition:
     * @Test-cases:
     *
     * @return: sensor data or its reading
     */
    int getSensorData(boolean[] data, int position);



    /**
     * @Description If the sensor inputs are not in the range of 0 to 200, it indicates noise.
     *
     * @Pre-condition:
     * @Post-condition:
     * @Test-cases:
     * @return boolean value with true indicating noisy reading
     */
    boolean isNoise(int value);

    /**
     * @Description Aggregate valid readings/inputs from a sensor
     *
     * @Pre-condition:
     * @Post-condition:
     * @Test-cases:
     *
     * @param readings from sensors
     * @return int value
     */
    int aggregatedValue(int[] readings);
}

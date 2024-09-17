package com.tves;

public interface Sensor {
    /**
     * The sensor inputs are integers (in the range of 0 to 200)
     * indicating the distance to the nearest object on the right.
     */
    int getSensorData();

}

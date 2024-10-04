package com.tves;

//Actuators are the controllers of the car engine that will be called in the
// MoveForward and MoveBackward methods of Phase 1
public interface Actuator {

    /**
     * @Description The actuator is a controller that
     * moves the car in forward or backward direction
     *
     * @param direction for car movement, and current xPosition of the car
     * @return int showing new position of the car in the street
     */
    int MoveCar(int direction, int xPosition);
}

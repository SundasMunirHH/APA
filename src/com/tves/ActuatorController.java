package com.tves;

public class ActuatorController implements Actuator {

    public ActuatorController() {

    }

    //@Testcases: testMoveBackwardAtStartOfStreet, and testMoveBackwardBeyondStreet
    public int MoveCar(int direction, int xPosition) {
        //@Testcases: testMoveForward-Assert 1, and testMoveForwardAtEndOfStreet
        if (direction > 0 && xPosition < Utilities.parkingStreetLength - 1) {
            //we cannot let xPosition be 500,
            // moves the car 100 centimeter forward
            xPosition += direction;
        } else if (direction < 0 && xPosition > 0) {//@Testcases: testMoveBackwardAtStartOfStreet, and testMoveBackwardBeyondStreet
            //move backward
            //we cannot let xPosition be -ve
            // moves the car 100 centimeter backward
            xPosition += direction;
        }
        return xPosition;
    }
}

package com.lazarusid.spookystepper;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

/**
 * Created by clay on 12/30/16.
 */

public class StepperRunner {

    public Gpio stepPort;
    public Gpio dirPort;

    public void step() throws IOException {
        stepPort.setValue(true);
        stepPort.setValue(false);
    }

    public void setDirection(Direction dir) throws IOException {

        if (Direction.UP == dir) {
            dirPort.setValue(true);
        }

    }
}

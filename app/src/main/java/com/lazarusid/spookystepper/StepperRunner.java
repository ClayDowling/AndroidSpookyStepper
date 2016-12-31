package com.lazarusid.spookystepper;

import android.os.SystemClock;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

/**
 * Created by clay on 12/30/16.
 */

public class StepperRunner {

    public Gpio stepPort;
    public Gpio dirPort;

    public long stepDelay;
    public boolean canLoop = true;

    public TimeProvider clock;

    public void step() throws IOException {
        stepPort.setValue(true);
        stepPort.setValue(false);
    }

    public void setDirection(Direction dir) throws IOException {
        if (Direction.UP == dir) {
            dirPort.setValue(true);
        } else if (Direction.DOWN == dir) {
            dirPort.setValue(false);
        }
    }

    public void run() throws IOException {
        long startTime = clock.getTime();
        long targetTime = startTime + stepDelay;
        do {
            Thread.yield();
            if (clock.getTime() > targetTime) {
                step();
            }
        } while(canLoop);
    }
}

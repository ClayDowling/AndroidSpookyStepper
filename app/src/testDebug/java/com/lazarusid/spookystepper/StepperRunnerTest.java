package com.lazarusid.spookystepper;

import com.google.android.things.pio.Gpio;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by clay on 12/31/16.
 */
public class StepperRunnerTest {

    private StepperRunner runner;

    private Gpio stepPort;
    private Gpio dirPort;

    @Before
    public void setUp() throws Exception {
        stepPort = mock(Gpio.class);
        dirPort = mock(Gpio.class);

        runner = new StepperRunner();
        runner.stepPort = stepPort;
        runner.dirPort = dirPort;
    }

    @Test
    public void step_willSetStepPortHighThenLow() throws IOException {
        runner.step();

        verify(stepPort).setValue(true);
        verify(stepPort).setValue(false);
    }

    @Test
    public void setDirection_givenUp_setsDirPortHigh() throws IOException {
        runner.setDirection(Direction.UP);
        verify(dirPort).setValue(true);
    }
}
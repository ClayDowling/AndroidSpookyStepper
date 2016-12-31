package com.lazarusid.spookystepper;

import android.os.SystemClock;

import com.google.android.things.pio.Gpio;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StepperRunnerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private StepperRunner runner;

    private Gpio stepPort;
    private Gpio dirPort;
    private TimeProvider clock;

    @Before
    public void setUp() throws Exception {
        stepPort = mock(Gpio.class);
        dirPort = mock(Gpio.class);
        clock = mock(TimeProvider.class);

        runner = new StepperRunner();
        runner.canLoop = false;
        runner.stepPort = stepPort;
        runner.dirPort = dirPort;
        runner.clock = clock;
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

    @Test
    public void setDirection_giveDown_setsDirPortLow() throws IOException {
        runner.setDirection(Direction.DOWN);
        verify(dirPort).setValue(false);
    }

    @Test
    public void run_withStepDelay15_willNotStepBefore15miliseconds() throws IOException {
        when(clock.getTime()).thenReturn(10L, 24L);
        runner.stepDelay = 15;
        runner.run();
        verify(stepPort, never()).setValue(anyBoolean());
    }

    @Test
    public void run_withStepDelay15_willStepAfter16milliseconds() throws IOException {
        when(clock.getTime()).thenReturn(10L, 26L);
        runner.stepDelay = 15;
        runner.run();
        verify(stepPort).setValue(true);
    }

}
package com.rxnctrllabs.androidledblink;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;

@Module
@Singleton
public class StepperState {

    @Inject
    public StepperState() {}

    public String stepPort = "BCM21";
    public String dirPort = "BCM20";
    public int manSteps = 400;
    public boolean goingUp = false;
}

package com.rxnctrllabs.androidledblink;

import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

public class MainActivity extends BaseThinkActivity {

    @Singleton
    @Component(modules = {PeripheralManagerServiceModule.class})
    public interface Injector {
        void inject(MainActivity mainActivity);
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    PeripheralManagerService service;

    @Inject
    StepperState state;

    private Gpio stepPort = null;
    private Gpio dirPort = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainActivity_Injector.create().inject(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        connectToGpioPort();

        moveMotor();
    }

    @Override
    protected void onDestroy() {
        if (null != this.stepPort)
            try {
                this.stepPort.close();
                this.dirPort.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onDestroy();
    }

    private void connectToGpioPort() {
        try {
            PeripheralManagerService manager = new PeripheralManagerService();
            this.stepPort = manager.openGpio(state.stepPort);
            this.dirPort = manager.openGpio(state.dirPort);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO", e);
        }
    }

    private void moveMotor() {
        new Thread(new Runnable() {

            private int delay = 50;

            @Override
            public void run() {
                try {
                    MainActivity.this.stepPort.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
                    MainActivity.this.stepPort.setActiveType(Gpio.ACTIVE_LOW);

                    long startTime = System.currentTimeMillis();
                    long targetTime = startTime + delay;
                    while (true) {
                        Thread.yield();
                        MainActivity.this.stepPort.setValue(true);
                        if (System.currentTimeMillis() > targetTime) {
                            startTime = System.currentTimeMillis();
                            targetTime = startTime + delay;
                            MainActivity.this.stepPort.setValue(true);
                            MainActivity.this.stepPort.setValue(false);
                        }
                    }

                } catch (IOException e) {
                    Log.w(TAG, "GPIO Exception", e);
                }
            }
        }).start();
    }
}

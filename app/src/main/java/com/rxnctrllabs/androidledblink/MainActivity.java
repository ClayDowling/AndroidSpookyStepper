package com.rxnctrllabs.androidledblink;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

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

    private Gpio gpio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainActivity_Injector.create().inject(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        listAvailableGpio();
        listAvailablePwm();
        listAvailableUart();
        listAvailableI2C();
        connectToGpioPort("BCM21");

        blinkLED();
    }

    @Override
    protected void onDestroy() {
        if (null != this.gpio)
            try {
                this.gpio.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onDestroy();
    }

    private void listAvailableGpio() {
        TextView availableGpioText = findViewById(R.id.availableGpioText, TextView.class);

        List<String> gpioList = this.service.getGpioList();

        String text = "GPIO: " + gpioList.toString();
        availableGpioText.setText(text);
    }

    private void listAvailablePwm() {
        TextView availablePwmText = findViewById(R.id.availablePwmText, TextView.class);

        List<String> pwmList = this.service.getPwmList();

        String text = "PWM: " + pwmList.toString();
        availablePwmText.setText(text);
    }

    private void listAvailableUart() {
        TextView availableUartText = findViewById(R.id.availableUartText, TextView.class);

        List<String> uartList = this.service.getUartDeviceList();

        String text = "UART: " + uartList.toString();
        availableUartText.setText(text);
    }

    private void listAvailableI2C() {
        TextView availableI2CText = findViewById(R.id.availableI2CText, TextView.class);

        List<String> i2cList = this.service.getI2cBusList();

        String text = "I2C: " + i2cList.toString();
        availableI2CText.setText(text);
    }

    private void connectToGpioPort(String gpioName) {
        try {
            PeripheralManagerService manager = new PeripheralManagerService();
            this.gpio = manager.openGpio(gpioName);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO", e);
        }
    }

    private void blinkLED() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    MainActivity.this.gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
                    MainActivity.this.gpio.setActiveType(Gpio.ACTIVE_LOW);

                    long startTime = System.currentTimeMillis();
                    long targetTime = startTime + 1000;
                    while (true) {
                        Thread.yield();
                        if (System.currentTimeMillis() > targetTime) {
                            startTime = System.currentTimeMillis();
                            targetTime = startTime + 1000;
                            MainActivity.this.gpio.setValue(!MainActivity.this.gpio.getValue());
                        }
                    }

                } catch (IOException e) {
                    Log.w(TAG, "GPIO Exception", e);
                }
            }
        }).start();
    }
}

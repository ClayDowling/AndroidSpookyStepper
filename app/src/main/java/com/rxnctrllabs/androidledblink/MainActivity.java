package com.rxnctrllabs.androidledblink;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Gpio gpio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        listAvailableGpio();
        listAvailablePwm();
        listAvailableUart();
        listAvailableI2C();
        connectToGpioPort("BCM21");

        blinkLed();
    }

    @Override
    protected void onDestroy() {
        if (null != gpio)
            try {
                gpio.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onDestroy();
    }

    private void listAvailableGpio() {
        TextView availableGpioText = (TextView) findViewById(R.id.availableGpioText);

        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> gpioList = manager.getGpioList();

        String text = "GPIO: " + gpioList.toString();
        availableGpioText.setText(text);
    }

    private void listAvailablePwm() {
        TextView availablePwmText = (TextView) findViewById(R.id.availablePwmText);

        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> pwmList = manager.getPwmList();

        String text = "PWM: " + pwmList.toString();
        availablePwmText.setText(text);
    }

    private void listAvailableUart() {
        TextView availableUartText = (TextView) findViewById(R.id.availableUartText);

        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> uartList = manager.getUartDeviceList();

        String text = "UART: " + uartList.toString();
        availableUartText.setText(text);
    }

    private void listAvailableI2C() {
        TextView availableI2CText = (TextView) findViewById(R.id.availableI2CText);

        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> i2cList = manager.getI2cBusList();

        String text = "I2C: " + i2cList.toString();
        availableI2CText.setText(text);
    }

    private void connectToGpioPort(String gpioName) {
        try {
            PeripheralManagerService manager = new PeripheralManagerService();
            gpio = manager.openGpio(gpioName);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO", e);
        }
    }

    private void blinkLed() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
                    gpio.setActiveType(Gpio.ACTIVE_LOW);

                    long startTime = System.currentTimeMillis();
                    long targetTime = startTime + 1000;
                    while (true) {
                        Thread.yield();
                        if (System.currentTimeMillis() > targetTime) {
                            startTime = System.currentTimeMillis();
                            targetTime = startTime + 1000;
                            gpio.setValue(!gpio.getValue());
                        }
                    }

                } catch (IOException e) {
                    Log.w(TAG, "GPIO Exception", e);
                }
            }
        }).start();

    }
}

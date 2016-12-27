package com.rxnctrllabs.androidledblink;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;

import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> portList = manager.getGpioList();
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + portList);
        }
    }
}

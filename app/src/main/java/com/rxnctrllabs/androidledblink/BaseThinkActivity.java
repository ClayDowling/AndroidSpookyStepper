package com.rxnctrllabs.androidledblink;

import android.app.Activity;

public abstract class BaseThinkActivity extends Activity {

    public <T> T findViewById(final int id, final Class<T> clazz) {
        return (T) findViewById(id);
    }
}

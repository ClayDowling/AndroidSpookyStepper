package com.lazarusid.spookystepper;

import android.os.SystemClock;

/**
 * Created by clay on 12/31/16.
 */

public class SystemTimeProvider implements TimeProvider {
    @Override
    public long getTime() {
        return SystemClock.uptimeMillis();
    }
}

package com.lazarusid.spookystepper;

import com.google.android.things.pio.PeripheralManagerService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class PeripheralManagerServiceModule {

    @Provides
    @Singleton
    PeripheralManagerService providePeripheralManagerService() {
        return new PeripheralManagerService();
    }
}

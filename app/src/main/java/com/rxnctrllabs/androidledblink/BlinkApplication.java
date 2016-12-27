package com.rxnctrllabs.androidledblink;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

public class BlinkApplication extends Application {

    @Singleton
    @Component(modules = {PeripheralManagerServiceModule.class})
    public interface ApplicationComponent {
        void inject(MainActivity mainActivity);
    }

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        this.applicationComponent = DaggerBlinkApplication_ApplicationComponent.builder().build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}

package com.swivl.tz;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class AppApplication extends Application {

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

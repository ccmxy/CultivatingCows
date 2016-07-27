package com.cultivatingcows;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;


public class BootstrapClass extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }

}

package com.cultivatingcows;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**** Class which gives application access to Bootstrap. *****/
public class BootstrapClass extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }

}

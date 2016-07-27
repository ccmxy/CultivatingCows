package com.cultivatingcows;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.parse.Parse;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //ParseCrashReporting.enable(this);
        TypefaceProvider.registerDefaultIconSets();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "7DNaExGH9NK4AWOHPh3xg07BXQ8HvFw4fqe5gpHM", "pRFGQEEZfQ8IV0rt9soZfJqgnclLydKJAy9ENVAN");
    }


}


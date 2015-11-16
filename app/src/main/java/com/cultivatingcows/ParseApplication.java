package com.cultivatingcows;

import android.app.Application;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.cultivatingcows.UI.RegisterLoginActivity;
import com.parse.Parse;

/**
 * Created by Guest on 11/16/15.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "7DNaExGH9NK4AWOHPh3xg07BXQ8HvFw4fqe5gpHM", "pRFGQEEZfQ8IV0rt9soZfJqgnclLydKJAy9ENVAN");
    }


}


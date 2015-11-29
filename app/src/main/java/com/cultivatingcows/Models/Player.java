package com.cultivatingcows.Models;

import android.app.Activity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by colleenminor on 11/28/15.
 */
@ParseClassName("Player")
public class Player extends ParseObject {


    public Player() {
        super();
    }

    public Player(ParseUser thisUser, final String TAG, final Activity context) {
        put("User", thisUser);
        put("score", 0);
    }

    public Player(ParseUser thisUser) {
        put("User", thisUser);
        put("score", 0);
    }
}

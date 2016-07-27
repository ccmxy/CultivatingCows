package com.cultivatingcows.Models;

import android.app.Activity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
Not very used class.
 */

@ParseClassName("Player")
public class Player extends ParseObject {
    private List<Player> mPlayers;


    public Player() {
        super();
    }

    //Just in case...
    public Player(ParseUser thisUser, final String TAG, final Activity context) {
        put("User", thisUser);
        put("score", 0);
        String username = thisUser.getUsername();
        put("name", username);
    }

    public Player(ParseUser thisUser) {
        put("User", thisUser);
        put("score", 0);
        String username = thisUser.getUsername();
        put("name", username);
    }



}

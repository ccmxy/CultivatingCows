package com.cultivatingcows.Models;

import android.app.Activity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by colleenminor on 11/28/15.
 */
@ParseClassName("Player")
public class Player extends ParseObject {
    private List<Player> mPlayers;


    public Player() {
        super();
    }

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

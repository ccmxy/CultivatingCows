package com.cultivatingcows.Models;

import android.app.Activity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Guest on 11/12/15.
 */
@ParseClassName("Game")
public class Game extends ParseObject {
    public Game(){
        super();
    }

    public Game(String gameName, List<ParseUser> players){
        put("name", gameName);
        put("players", players);
    }

    public void saveGame() {
        saveInBackground();
    }



}

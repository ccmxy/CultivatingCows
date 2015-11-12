package com.cultivatingcows.Models;

import android.app.Activity;

import com.cultivatingcows.ErrorHelper;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Guest on 11/12/15.
 */
@ParseClassName("Game")
public class Game extends ParseObject {
    private static List<Game> mGames;

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

    public static ParseQuery<Game> gamesListQuery() {
        return ParseQuery.getQuery(Game.class)
                .orderByAscending("name");
    }

    public static void findAllGames(final String tag, final Activity context, final Runnable runnable) {
        gamesListQuery().findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> games, com.parse.ParseException e) {
                if (e == null) {
                    mGames = games;
                   context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                    //int x = 2;
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }


            }
        });
    }

    public static ParseQuery<Game> specificGameQuery(String gameName) {
        return ParseQuery.getQuery(Game.class)
                .whereEqualTo("name", gameName);
    }


    public static void findGameByName(final String tag, final Activity context, final Runnable runnable){

    }

    public static List<Game> getGames() {
        return mGames;
    }







}

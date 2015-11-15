package com.cultivatingcows.Models;

import android.app.Activity;

import com.cultivatingcows.ErrorHelper;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Guest on 11/12/15.
 */
@ParseClassName("Game")
public class Game extends ParseObject {
    private static List<Game> mGames;
    private static ParseObject mGame;

    public Game(){
        super();
    }

    public Game(String gameName, List<ParseUser> players, int numPlayers){
        put("name", gameName);
        put("players", players);
        put("numPlayers", numPlayers);
        put("curNumPlayers", 1);
        put("inProgress", false);
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
                }


            }
        });
    }

    public static ParseQuery<Game> specificGameQuery(String gameName) {
        return ParseQuery.getQuery(Game.class)
                .whereEqualTo("name", gameName);
    }

    public static List<Game> getGames() {
        return mGames;
    }

    //The two below have not been tested
    public static void findGameByName(final String gameName, final String tag, final Activity context, final Runnable runnable){
        specificGameQuery(gameName).findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> games, ParseException e) {
                if (e == null) {
                    mGame = games.get(0);
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }
            }
        });
    }

    public static ParseObject getThisGame(){
        return mGame;
    }


//    public static void findGameByName(final String gameName, final String tag, final Activity context, final Runnable runnable){
//        List<ParseUser> players = User.findPlayers(gameName);
//
//
//        specificGameQuery(gameName).findInBackground(new FindCallback<Game>() {
//            @Override
//            public void done(List<Game> objects, ParseException e) {
//                if (e == null) {
//                    mGames = objects;
//                    Game thisGame = mGames.get(0);
//                    List<ParseUser> players = User.findPlayers(thisGame);
//                    context.runOnUiThread(runnable);
//                } else {
//                    ErrorHelper.handleError(tag, context, e.getMessage());
//                    //int x = 2;
//                    // Sign up didn't succeed. Look at the ParseException
//                    // to figure out what went wrong
//                }
//
//
//            }
//        });
//
//    }




}

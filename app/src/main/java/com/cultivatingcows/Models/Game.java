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
    private List<ParseUser> mPlayers;
    private List<Player> mThePlayers;
    private String mName;
    private static ParseUser mWhosTurn;

    public Game(){
        super();
    }


    public Game(String gameName, List<ParseUser> players, List<Player> playersList, int numPlayers){
        put("name", gameName);
        put("players", players);
        put("playersList", playersList);
        put("numPlayers", numPlayers);
        put("curNumPlayers", 1);
        put("inProgress", false);
        put("whosTurn", players.get(0));
    }



    public Game(ParseObject game, List<ParseUser> mPlayersList){
        mName = game.getString("name");
        mPlayers = mPlayersList;
        mWhosTurn = game.getParseUser("whosTurn");
    }

    public void nextTurn() {
        int idx = mPlayers.indexOf(mWhosTurn);
        if(idx == mPlayers.size() - 1){
            mWhosTurn = mPlayers.get(0);
        }
        else {
            mWhosTurn = mPlayers.get(idx + 1);
        }
    }

    public String getName(){
        return mName;
    }

    public List<ParseUser> getPlayers(){
        return mPlayers;
    }

    public static ParseUser getWhosTurn(){
        return mWhosTurn;
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

    public static void findAllGamesNotInProgress(final String tag, final Activity context, final Runnable runnable) {
        gamesListQuery().whereEqualTo("inProgress", false).findInBackground(new FindCallback<Game>() {
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

}

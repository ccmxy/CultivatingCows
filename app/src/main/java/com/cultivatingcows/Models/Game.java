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

@ParseClassName("Game")
public class Game extends ParseObject {
    /******* mGames is a list of games that is accessed during the
     runnable in findAllGamesNotInProgress in
    GameHomeActivity:     *******/
    private static List<Game> mGames;
    /******* mGame is accessed during the runnable passed into
     findGameByName:     *******/
    private static ParseObject mGame;
    /******* mPlayers is the list of users who are signed up
     to play this game:     *******/
    private List<ParseUser> mPlayers;
    /******* mName is the name of the game:  *******/
    private String mName;
    /******* mWhosTurn keeps track of who's turn it is to roll:  *******/
    private static ParseUser mWhosTurn;


    //This is necessary to initialize the Parse object:
    public Game(){
        super();
    }

    /******* This is the constructor used for creating a new game:   *******/
    public Game(String gameName, List<ParseUser> players, List<Player> playersList, int numPlayers){
        put("name", gameName);
        put("players", players);
        put("playersList", playersList);
        put("numPlayers", numPlayers);
        put("curNumPlayers", 1);
        put("inProgress", false);
        put("whosTurn", players.get(0));
    }

    /******* This constructor is used during a runnable that is
     sent in to findGameByName, for the purpose of accessing  :     *******/
    public Game(ParseObject game, List<ParseUser> mPlayersList){
        mName = game.getString("name");
        mPlayers = mPlayersList;
        mWhosTurn = game.getParseUser("whosTurn");
    }


    //This function changes the static mWhosTurn variable of this class,
    // but in order to actually update the database
    // the game object has to actually have the new mWhosTurn
    // put in its whosTurn column and be saved again:
    public void nextTurn() {
        int idx = mPlayers.indexOf(mWhosTurn);
        if(idx == mPlayers.size() - 1){
            mWhosTurn = mPlayers.get(0);
        }
        else {
            mWhosTurn = mPlayers.get(idx + 1);
        }
    }


    //This uses a Parse.com metho to save the ParseObject:
    public void saveGame() {
        saveInBackground();
    }
    //This is a Parse.com query used by the findGameByName function:
    public static ParseQuery<Game> gamesListQuery() {
        return ParseQuery.getQuery(Game.class)
                .orderByAscending("name");
    }

    //This is a Parse.com query used by the findGameByName function:
    public static ParseQuery<Game> specificGameQuery(String gameName) {
        return ParseQuery.getQuery(Game.class)
                .whereEqualTo("name", gameName);
    }


    //This retrieves a list of games that are not in progress (that can be joined) and
    // the mGames variable is accessed by calling getGames during the runnable
    // that is passed to this function.
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

    //This retrieves a single game by running a query to match a "Game" ParseObject's
    // "name" column to the first string that's sent in.
    // The mGame variable is accessed by calling getThisGame during the runnable
    // that is passed to this function.
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

    //I don't use this but it retreives all the games
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

    ///****** Getters called during runnables: ******////
    public static List<Game> getGames() {
        return mGames;
    }


    public String getName(){
        return mName;
    }


    public static ParseUser getWhosTurn(){
        return mWhosTurn;
    }

    //This is used during the findGameByName runnable, to get the a Game ParseObject into a variable:
    public static ParseObject getThisGame(){
        return mGame;
    }

}

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
 * Created by Guest on 7/26/16.
 */
@ParseClassName("SpecialMap")
public class SpecialMap extends ParseObject {
    private static List<SpecialMap> mSpecialMaps;
    public static List<String> mWordsList;
    private static ParseObject mSpecialMap;
    private List<ParseUser> mPlayers;
    private List<Player> mThePlayers;
    private String mName;
    private static ParseUser mWhosTurn;

    public SpecialMap(){
        super();
    }

    public SpecialMap(String gameName, List<ParseUser> players, List<Player> playersList, int numPlayers){
        put("name", gameName);
        put("players", players);
        put("playersList", playersList);
        put("numPlayers", numPlayers);
        put("curNumPlayers", 1);
        put("inProgress", false);
        put("whosTurn", players.get(0));
    }

    public SpecialMap(ParseObject game, List<ParseUser> mPlayersList){
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

    public void saveSpecialMap() {
        saveInBackground();
    }

    public static ParseQuery<SpecialMap> gamesListQuery() {
        return ParseQuery.getQuery(SpecialMap.class)
                .orderByAscending("name");
    }

    public static void findAllSpecialMaps(final String tag, final Activity context, final Runnable runnable) {
        gamesListQuery().findInBackground(new FindCallback<SpecialMap>() {
            @Override
            public void done(List<SpecialMap> games, com.parse.ParseException e) {
                if (e == null) {
                    mSpecialMaps = games;
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }


            }
        });
    }

    public static void findAllSpecialMapsNotInProgress(final String tag, final Activity context, final Runnable runnable) {
        gamesListQuery().whereEqualTo("inProgress", false).findInBackground(new FindCallback<SpecialMap>() {
            @Override
            public void done(List<SpecialMap> games, com.parse.ParseException e) {
                if (e == null) {
                    mSpecialMaps = games;
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }
            }
        });
    }

    public static ParseQuery<SpecialMap> specificSpecialMapQuery(String gameName) {
        return ParseQuery.getQuery(SpecialMap.class)
                .whereEqualTo("name", gameName);
    }

    public static List<SpecialMap> getSpecialMaps() {
        return mSpecialMaps;
    }

    public static void findSpecialMapByName(final String gameName, final String tag, final Activity context, final Runnable runnable){
        specificSpecialMapQuery(gameName).findInBackground(new FindCallback<SpecialMap>() {
            @Override
            public void done(List<SpecialMap> games, ParseException e) {
                if (e == null) {
                    mSpecialMap = games.get(0);
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }
            }
        });
    }

    public static ParseObject getThisSpecialMap(){
        return mSpecialMap;
    }

}

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
    private int mLatitude;
    private int mLongitude;
    private static ParseUser mWhosTurn;

    public SpecialMap(){
        super();
    }

    public SpecialMap(String name, int latitude, int longitude, String msg){
        put("name", name);
        put("latitude", latitude); //was players
        put("longitude", longitude); //was playerList
        put("msg", msg); //was numPlayers
    }

    //THE PLAYERSLIST AS PARSEUSER MAY HAVE BEEN THE ISSUE THE WHOLE TIME
    public SpecialMap(ParseObject SpecialMap){
        mName = SpecialMap.getString("name");
        mLatitude = SpecialMap.getInt("latitude");
        mLongitude = SpecialMap.getInt("longitude");
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

    public static ParseQuery<SpecialMap> specialMapsListQuery() {
        return ParseQuery.getQuery(SpecialMap.class)
                .orderByAscending("name");
    }

    public static void findAllSpecialMaps(final String tag, final Activity context, final Runnable runnable) {
        specialMapsListQuery().findInBackground(new FindCallback<SpecialMap>() {
            @Override
            public void done(List<SpecialMap> specialMaps, com.parse.ParseException e) {
                if (e == null) {
                    mSpecialMaps = specialMaps;
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }


            }
        });
    }

    public static void findAllSpecialMapsNotInProgress(final String tag, final Activity context, final Runnable runnable) {
        specialMapsListQuery().whereEqualTo("inProgress", false).findInBackground(new FindCallback<SpecialMap>() {
            @Override
            public void done(List<SpecialMap> specialMaps, com.parse.ParseException e) {
                if (e == null) {
                    mSpecialMaps = specialMaps;
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }
            }
        });
    }

    public static ParseQuery<SpecialMap> specificSpecialMapQuery(String specialMapName) {
        return ParseQuery.getQuery(SpecialMap.class)
                .whereEqualTo("name", specialMapName);
    }

    public static List<SpecialMap> getSpecialMaps() {
        return mSpecialMaps;
    }

    public static void findSpecialMapByName(final String specialMapName, final String tag, final Activity context, final Runnable runnable){
        specificSpecialMapQuery(specialMapName).findInBackground(new FindCallback<SpecialMap>() {
            @Override
            public void done(List<SpecialMap> specialMaps, ParseException e) {
                if (e == null) {
                    mSpecialMap = specialMaps.get(0);
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

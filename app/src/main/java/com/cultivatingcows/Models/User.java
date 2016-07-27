package com.cultivatingcows.Models;

import android.app.Activity;

import com.cultivatingcows.ErrorHelper;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


/**
 * Created by colleenminor on 11/11/15.
 */

@ParseClassName("_User")
public class User extends ParseUser {
    private static List<ParseUser> mPlayers;
    private static List<Player> mPlayersList;
    private ParseUser mParseUser;
    private String mEmail;
    private String mUsername;
    private String[] mGameNameStringArray;
    private static List<String> mUserGames;
    private static List<String> mUserMaps;


    public User() {
        super();
    }

    public User(ParseUser thisUser, final String TAG, final Activity context, final Runnable runnable) {
        mUsername = thisUser.getUsername();
        mEmail = thisUser.getEmail();
        mUserGames = thisUser.getList("game");
        mUserMaps = thisUser.getList("specialMap");
        context.runOnUiThread(runnable);
    }

    public static List<String> getUserGames() {
        return mUserGames;
    }

    //This gets passed into the runnable
    public static List<String> getUserMapNames() {
        return mUserMaps;
    }


    public User(String username, String password, String email) {
        setUsername(username);
        mUsername = username;
        setPassword(password);
        setEmail(email);
        mEmail = email;
        put("score", 0);
    }

    public void register(final String tag, final Activity context, final Runnable runnable) {
        signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }

            }
        });
    }

    public static void logIn(final String username, final String password, final String tag, final Activity context, final Runnable runnable) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                } else {
                    context.runOnUiThread(runnable);
                }
            }
        });
    }

    public static void findPlayers(final String gameName, final String tag, final Activity context, final Runnable runnable) {
        playersListQuery(gameName).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> players, com.parse.ParseException e) {
                if (e == null) {
                    mPlayers = players;
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }
            }
        });
    }

    public static ParseQuery<ParseUser> playersListQuery(String gameName) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        return userQuery.whereEqualTo("game", gameName);
    }

    public static List<ParseUser> getPlayers() {
        return mPlayers;
    }

}

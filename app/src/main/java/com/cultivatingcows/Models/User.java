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


//User is a built-in Parse.com object that is useful for loggin in:
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

    //This is needed for Parse.com to create an object:
    public User() {
        super();
    }

    //Constructor for creating a new ParseUser:
    public User(String username, String password, String email) {
        setUsername(username);
        mUsername = username;
        setPassword(password);
        setEmail(email);
        mEmail = email;
        put("score", 0);
    }

    //Constructor for creating a local User object from a retrieved ParseUser for accessing variables:
    public User(ParseUser thisUser, final String TAG, final Activity context, final Runnable runnable) {
        mUsername = thisUser.getUsername();
        mEmail = thisUser.getEmail();
        mUserGames = thisUser.getList("game");
        context.runOnUiThread(runnable);
    }

    //Getters to call during queries:
    public static List<String> getUserGames() {
        return mUserGames;
    }

    public static List<ParseUser> getPlayers() {
        return mPlayers;
    }



    //The runnable here is used to start the MainActivity page:
    public void register(final String tag, final Activity context, final Runnable runnable) {
        //Sign up functionality built into the Parse.com _User class:
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

    //The runnable here is used to start the MainActivity page:
    public static void logIn(final String username, final String password, final String tag, final Activity context, final Runnable runnable) {
        //logInInBackground is inherent to the Parse.com _User class:
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

    //findPlayers is in the User class because it wasn't any easier to do from the Game class:
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

    //This is the query used with findPlayers:
    public static ParseQuery<ParseUser> playersListQuery(String gameName) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        return userQuery.whereEqualTo("game", gameName);
    }
}

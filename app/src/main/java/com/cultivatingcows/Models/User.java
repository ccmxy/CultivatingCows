package com.cultivatingcows.Models;

import android.app.Activity;

import com.cultivatingcows.ErrorHelper;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by colleenminor on 11/11/15.
 */

@ParseClassName("_User")
public class User extends ParseUser {

    private ParseUser mParseUser;
    private String mEmail;
    private String mUsername;
    private String[] mGameNameStringArray;
    private static List<String> mUserGames;

    public User(){
        super();
    }

    public User(ParseUser thisUser, final String TAG, final Activity context, final Runnable runnable){
        mUsername = thisUser.getUsername();
        mEmail = thisUser.getEmail();
        mUserGames = thisUser.getList("game");
        context.runOnUiThread(runnable);
    }

    public static List<String> getUserGames(){
        return mUserGames;
    }

    public User(String username, String password, String email){
        setUsername(username);
        mUsername = username;
        setPassword(password);
        setEmail(email);
        mEmail = email;
    }

    public void register(final String tag, final Activity context, final Runnable runnable){
        signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
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

}

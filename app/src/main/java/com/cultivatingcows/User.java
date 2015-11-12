package com.cultivatingcows;

import android.app.Activity;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by colleenminor on 11/11/15.
 */

@ParseClassName("_User")
public class User extends ParseUser {

    public User(){
        super();
    }

    public User(String username, String password, String email){
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    public void register(final String tag, final Activity context, final Runnable runnable){
        signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());

                    int x = 2;
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }

            }
        });
    }

}

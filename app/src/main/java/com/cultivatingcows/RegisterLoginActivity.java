package com.cultivatingcows;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cultivatingcows.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterLoginActivity extends AppCompatActivity {
    private static final String TAG = RegisterLoginActivity.class.getSimpleName();

    @Bind(R.id.usernameRegister)
    EditText mUsernameRegister;

    @Bind(R.id.passwordRegister)
    EditText mPasswordRegister;

    @Bind(R.id.emailRegister)
    EditText mEmailRegister;

    @Bind(R.id.usernameLogin)
    EditText mUsernameLogin;

    @Bind(R.id.passwordLogin)
    EditText mPasswordLogin;

    @Bind(R.id.signUpButton)
    Button mSignupButton;

    @Bind(R.id.logInButton)
    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        ButterKnife.bind(this);

        ParseObject.registerSubclass(User.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "7DNaExGH9NK4AWOHPh3xg07BXQ8HvFw4fqe5gpHM", "pRFGQEEZfQ8IV0rt9soZfJqgnclLydKJAy9ENVAN");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameRegister.getText().toString().trim();
                String password = mPasswordRegister.getText().toString().trim();
                String email = mEmailRegister.getText().toString().trim();
                User newUser = new User(username, password, email);
                newUser.register(TAG, RegisterLoginActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }

        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameLogin.getText().toString().trim();
                String password = mPasswordLogin.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    ErrorHelper.displayAlertDialog(RegisterLoginActivity.this, getString(R.string
                            .login_error_message));
                } else {
                    // Login
                    User.logIn(username, password, TAG, RegisterLoginActivity.this, new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

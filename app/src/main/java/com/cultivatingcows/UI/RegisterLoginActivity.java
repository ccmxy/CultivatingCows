package com.cultivatingcows.UI;

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

import com.cultivatingcows.ErrorHelper;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterLoginActivity extends AppCompatActivity {
    private static final String TAG = RegisterLoginActivity.class.getSimpleName();
    private int mWhichPressed;

    @Bind(R.id.usernameEditText)
    EditText mUsernameEditText;

    @Bind(R.id.passwordEditText)
    EditText mPasswordEditText;

    @Bind(R.id.emailEditText)
    EditText mEmailEditText;

    @Bind(R.id.signUpButton)
    Button mSignupButton;

    @Bind(R.id.logInButton)
    Button mLoginButton;

    @Bind(R.id.goButton)
    Button mGoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        ButterKnife.bind(this);
        mWhichPressed = 2;

        ParseObject.registerSubclass(User.class);
//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, "7DNaExGH9NK4AWOHPh3xg07BXQ8HvFw4fqe5gpHM", "pRFGQEEZfQ8IV0rt9soZfJqgnclLydKJAy9ENVAN");
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
                mWhichPressed = 0;
//                String username = mUsernameEditText.getText().toString().trim();
//                String password = mPasswordEditText.getText().toString().trim();
//                String email = mEmailEditText.getText().toString().trim();
//                User newUser = new User(username, password, email);
//                newUser.register(TAG, RegisterLoginActivity.this, new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    }
//                });
            }

        });

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Register was pressed:
                if (mWhichPressed == 0){
                    String username = mUsernameEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString().trim();
                    String email = mEmailEditText.getText().toString().trim();
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
                //Log in was pressed:
                else if (mWhichPressed == 1){
                    String username = mUsernameEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString().trim();
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
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mWhichPressed = 1;
//                String username = mUsernameEditText.getText().toString().trim();
//                String password = mPasswordEditText.getText().toString().trim();
//                if (username.isEmpty() || password.isEmpty()) {
//                    ErrorHelper.displayAlertDialog(RegisterLoginActivity.this, getString(R.string
//                            .login_error_message));
//                } else {
//                    // Login
//                    User.logIn(username, password, TAG, RegisterLoginActivity.this, new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//                    });
//                }

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
        if (id == R.id.action_login_page) {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_all_games_page) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_your_games_page) {
            Intent intent = new Intent(this, YourGamesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }}

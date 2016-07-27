package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.cultivatingcows.ErrorHelper;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterLoginActivity extends AppCompatActivity {
    private static final String TAG = RegisterLoginActivity.class.getSimpleName();

    @Bind(R.id.usernameEditText)
    BootstrapEditText mUsernameEditText;

    @Bind(R.id.passwordEditText)
    BootstrapEditText mPasswordEditText;

    @Bind(R.id.emailEditText)
    BootstrapEditText mEmailEditText;

    @Bind(R.id.logInButton)
    BootstrapButton mLoginButton;

    @Bind(R.id.goButton)
    BootstrapButton mGoButton;

    @Bind(R.id.signUpButton)
    BootstrapButton mSignupButton;

    @Bind(R.id.communityMapButton)
    BootstrapButton mCommunityMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        ButterKnife.bind(this);
        mLoginButton.setActivated(false);
        mSignupButton.setActivated(false);

        ParseObject.registerSubclass(User.class);
        //ParseObject.registerSubclass(SpecialMap.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Register was pressed:
                if (mSignupButton.isActivated()) {
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
                else if (mLoginButton.isActivated()) {
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

        mCommunityMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLoginActivity.this, AddMapPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });



        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginButton.setActivated(true);
                mSignupButton.setActivated(false);
                mUsernameEditText.setVisibility(View.VISIBLE);
                mPasswordEditText.setVisibility(View.VISIBLE);
                mEmailEditText.setVisibility(View.GONE);
                mGoButton.setVisibility(View.VISIBLE);
                mLoginButton.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                mSignupButton.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                mLoginButton.setBootstrapSize(DefaultBootstrapSize.LG);
                mSignupButton.setBootstrapSize(DefaultBootstrapSize.MD);
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignupButton.setActivated(true);
                mLoginButton.setActivated(false);
                mUsernameEditText.setVisibility(View.VISIBLE);
                mPasswordEditText.setVisibility(View.VISIBLE);
                mEmailEditText.setVisibility(View.VISIBLE);
                mGoButton.setVisibility(View.VISIBLE);
                mLoginButton.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                mSignupButton.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                mLoginButton.setBootstrapSize(DefaultBootstrapSize.MD);
                mSignupButton.setBootstrapSize(DefaultBootstrapSize.LG);
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

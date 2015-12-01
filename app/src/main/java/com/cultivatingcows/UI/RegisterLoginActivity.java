package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.cultivatingcows.BootstrapClass;
import com.cultivatingcows.ErrorHelper;
import com.cultivatingcows.Models.Player;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterLoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        ButterKnife.bind(this);
        mLoginButton.setActivated(false);
        mSignupButton.setActivated(false);
        BootstrapClass strappy;
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Player.class);


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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

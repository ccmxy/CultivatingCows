package com.cultivatingcows.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cultivatingcows.ErrorHelper;
import com.cultivatingcows.Models.Game;
import com.cultivatingcows.Models.PairOfDice;
import com.cultivatingcows.Models.Player;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/****** The main game page, where users take turns rolling dice. ****/
public class GamePageActivity extends AppCompatActivity {
    private static final String TAG = GamePageActivity.class.getSimpleName();
    private ParseObject mParseGame;
    private Game mGame;
    private List<ParseUser> mPlayers;
    private List<Player> mPlayersList;
    private ParseUser mWhosTurn;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private List<String> mScoreStrings = new ArrayList<String>();
    private Context mContext;


    @Bind(R.id.whosTurnText)
    TextView mWhosTurnText;

    @Bind(R.id.gameNameGamePageText)
    TextView gameNameText;

    @Bind(R.id.refreshGameButton)
    Button mRefreshButton;

    @Bind(R.id.playerOneScoreTextView)
    TextView mPlayerOneScore;

    @Bind(R.id.playerTwoScoreTextView)
    TextView mPlayerTwoScore;

    @Bind(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_game_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String gameName = intent.getStringExtra("gameName");
        gameNameText.setText(gameName);

        //The findPlayers method is in the User method instead of the Game method:
        User.findPlayers(gameName, TAG, GamePageActivity.this, new Runnable() {
            @Override
            public void run() {
                mPlayers = User.getPlayers();
                for (ParseUser player : mPlayers) {
                    int score = player.getInt("score");
                    String username = player.getUsername();
                    String stringToAdd = username + ": score = " + score;
                    mScoreStrings.add(stringToAdd);
                }
                mPlayerOneScore.setText(mScoreStrings.get(0));
                mPlayerTwoScore.setText(mScoreStrings.get(1));

            }
        });

        //This is not a function. I put a runnable in so it's a very long call
        // to findGameByName in the Game class.
        Game.findGameByName(gameName, TAG, GamePageActivity.this, new Runnable() {
            @Override
            public void run() {
                //At this point Game can access the Parse Object
                // it searched for in mParseGame because mParseGame
                // was retrieved and set as a static member of the class
                // before the runnable began.
                mParseGame = Game.getThisGame();

                //The new Game here is making a local game object to access variables from:
                mGame = new Game(mParseGame, mPlayers);

                mWhosTurn = Game.getWhosTurn();
                String thisTurn = mWhosTurn.getUsername();
                mWhosTurnText.setText("It is " + thisTurn + "'s turn.");
                if (currentUser == mWhosTurn) {
                    mWhosTurnText.setText("It is your turn, " + thisTurn + ".");
                    fab.setVisibility(View.VISIBLE);
                }
                else{
                    mWhosTurnText.setText("It is " + thisTurn + "'s turn.");
                    fab.setVisibility(View.INVISIBLE);
                }

            }
        });
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PairOfDice dice = new PairOfDice();
                dice.roll();
                int theRoll = dice.die1;
                int oldScore = currentUser.getInt("score");
                int score = oldScore + theRoll;
                currentUser.put("score", score);
                Game.findGameByName(gameName, TAG, GamePageActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        mParseGame = Game.getThisGame();
                        mGame = new Game(mParseGame, mPlayers);
                        //Switch current turn in game class:
                        mGame.nextTurn();
                        //Get current turn in game class:
                        mWhosTurn = Game.getWhosTurn();
                        //Update the Game ParseObject and save:
                        mParseGame.put("whosTurn", mWhosTurn);
                        mParseGame.saveInBackground();
                        //Set the dice to invisible when it is not the player's turn:
                        fab.setVisibility(View.INVISIBLE);
                    }
                });
                //Alert the user how they rolled with a brief snackbar pop up:
                Snackbar.make(view, "You rolled a " + theRoll, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Add a handler to snackbar to refresh when it's done:
                final Handler handler = new Handler();
                //Refresh the page about a 1.5 second delay:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = getIntent();
                        startActivity(intent);
                    }
                }, 1500);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //This creates a builder, a pop-up dialoge that the user can create a
        // new Game object with.
        if (id == R.id.action_quick_login) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GamePageActivity.this);
            builder.setTitle("Quick Login");
            final EditText userNameInpuut = new EditText(GamePageActivity.this);
            userNameInpuut.setHint("Username");
            userNameInpuut.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(userNameInpuut);
            final EditText passwordInput = new EditText(GamePageActivity.this);
            passwordInput.setHint("Password");
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout ll = new LinearLayout(GamePageActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(userNameInpuut);
            ll.addView(passwordInput);
            builder.setView(ll);
            builder.setMessage("Enter your login parameters.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Get the user currently logged in. getCurrentUser() is build-in to Parse.com.
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            String loginUserName = userNameInpuut.getText().toString();
                            String password = passwordInput.getText().toString();
                            if (loginUserName.isEmpty() || password.isEmpty()) {
                                ErrorHelper.displayAlertDialog(GamePageActivity.this, getString(R.string
                                        .login_error_message));
                            } else {
                                // Login
                                User.logIn(loginUserName, password, TAG, GamePageActivity.this, new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (id == R.id.action_login_page) {
            Intent intent = new Intent(GamePageActivity.this, RegisterLoginActivity.class);
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
        if (id == R.id.action_community_map_page) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

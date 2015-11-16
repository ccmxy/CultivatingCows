package com.cultivatingcows.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cultivatingcows.ErrorHelper;
import com.cultivatingcows.Models.Game;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameHomeActivity extends AppCompatActivity {
    private static final String TAG = GameHomeActivity.class.getSimpleName();

    @Bind(R.id.gameNameTextView)
    TextView gameNameText;

    @Bind(R.id.playerNamesTextView)
    TextView mPlayerNamesText;

    @Bind(R.id.readyToPlayTextView)
    TextView mCanWePlay;

    @Bind(R.id.joinGameButton)
    Button mJoinGameButton;

    @Bind(R.id.beginGameButton)
    Button mBeginGameButton;

    @Bind(R.id.refreshButton)
    Button mRefreshButton;


    private static ParseUser currentUser;
    private List<ParseUser> mPlayers;
    private String mPlayerNames;
    private ParseObject mGame;
    private String mEnoughPlayersText;
    private int mMaxNumPlayers;
    private int mCurNumPlayers;
    private boolean curUserIsPlaying;
    private static Context mContext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_home);
        ButterKnife.bind(this);
        currentUser = ParseUser.getCurrentUser();
        Intent intent = getIntent();
        final String gameName = intent.getStringExtra("gameName");
        gameNameText.setText(gameName);

        User.findPlayers(gameName, TAG, GameHomeActivity.this, new Runnable() {
            @Override
            public void run() {
                mPlayers = User.getPlayers();
                mPlayerNames = getPlayersList();
                mPlayerNamesText.setText(mPlayerNames);
                curUserIsPlaying = checkIfUserInGame(currentUser);
                if (curUserIsPlaying) {
                    mJoinGameButton.setVisibility(View.INVISIBLE);
                } else {
                    mBeginGameButton.setVisibility(View.INVISIBLE);
                }
            }
        });


        Game.findGameByName(gameName, TAG, GameHomeActivity.this, new Runnable() {
            @Override
            public void run() {
                mGame = Game.getThisGame();
                mMaxNumPlayers = mGame.getInt("numPlayers");
                if (mPlayers != null) {
                    mCurNumPlayers = mPlayers.size();
                } else {
                    List<ParseObject> count = mGame.getList("players");
                    if(count != null) {
                        mCurNumPlayers = count.size();
                    }
                    else {
                        mCurNumPlayers = 0;
                    }
                }
                mEnoughPlayersText = ("This game needs " + mMaxNumPlayers + " players and we have " + mCurNumPlayers + ". ");
                mGame.put("curNumPlayers", mCurNumPlayers);
                if (mMaxNumPlayers > mCurNumPlayers) {
                    mEnoughPlayersText += '\n' + "Find some more players so we can start! ";
                    mBeginGameButton.setVisibility(View.INVISIBLE);
                } else {
                    mEnoughPlayersText += '\n' + "Let's play!";
                    mGame.put("inProgress", true);
                    mGame.put("startingPlayer", currentUser);
                    mGame.saveInBackground();
                }
                mCanWePlay.setText(mEnoughPlayersText);
            }
        });

        mJoinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GameHomeActivity.this);
                builder.setTitle("Join Game");
                builder.setMessage("By clicking okay, you are agreeing to join this game.").
                        setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                currentUser.add("game", gameName);
                                currentUser.saveInBackground();
                                mGame.add("players", currentUser);
                                mGame.saveInBackground();
                                Toast.makeText(GameHomeActivity.this, "Congratulations on joining " + gameName + "!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mBeginGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("hasBegan", gameName);
                currentUser.saveInBackground();
                Intent intent = new Intent(GameHomeActivity.this, GamePageActivity.class);
                intent.putExtra("gameName", gameName);
                startActivity(intent);
            }
        });

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                startActivity(intent);
            }
        });
    }

    public boolean checkIfUserInGame(ParseUser currentUser){
        String username = currentUser.getUsername();
        if (mPlayers.contains(currentUser)){
            return true;
        }
        else{
            return false;
        }
    }

    public String getPlayersList(){
        String playerNameString = "Players: ";
        for(ParseUser player: mPlayers){
            playerNameString += player.getUsername();
            playerNameString += ", ";
        }
        return removeLastChar(playerNameString);
    }

    private static String removeLastChar(String str) {
        return str.substring(0,str.length()-2);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_quick_login) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GameHomeActivity.this);
            builder.setTitle("Quick Login");
            final EditText userNameInpuut = new EditText(GameHomeActivity.this);
            userNameInpuut.setHint("Username");
            userNameInpuut.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(userNameInpuut);
            final EditText passwordInput = new EditText(GameHomeActivity.this);
            passwordInput.setHint("Password");
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout ll = new LinearLayout(GameHomeActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(userNameInpuut);
            ll.addView(passwordInput);
            builder.setView(ll);
            builder.setMessage("Enter your login parameters.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            List<ParseUser> players = new ArrayList<ParseUser>();
                            players.add(currentUser);
                            String loginUserName = userNameInpuut.getText().toString();
                            String password = passwordInput.getText().toString();
                            if (loginUserName.isEmpty() || password.isEmpty()) {
                                ErrorHelper.displayAlertDialog(GameHomeActivity.this, getString(R.string
                                        .login_error_message));
                            } else {
                                // Login
                                User.logIn(loginUserName, password, TAG, GameHomeActivity.this, new Runnable() {
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
            Intent intent = new Intent(GameHomeActivity.this, RegisterLoginActivity.class);
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
    }

}

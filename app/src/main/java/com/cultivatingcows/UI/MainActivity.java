package com.cultivatingcows.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cultivatingcows.ErrorHelper;
import com.cultivatingcows.Models.Game;
import com.cultivatingcows.Models.Player;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.yourGamesButton)
    Button mYourGamesButton;

    @Bind(R.id.newGameButton)
    Button mNewGameButton;

    @Bind(R.id.listView)
    ListView mGamesList;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private ArrayAdapter<String> mArrayAdapter;
    private String mGameId;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    private List<Game> mAllGames;
    private ParseObject mThisGame;
    private static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Player.class);
        ParseObject.registerSubclass(Game.class);
        setSupportActionBar(toolbar);


        mYourGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YourGamesActivity.class);
                startActivity(intent);
            }
        });

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("New Game");
                final EditText gameNameInput = new EditText(MainActivity.this);
                gameNameInput.setHint("Name your new game");
                gameNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(gameNameInput);
                final EditText numPlayersInput = new EditText(MainActivity.this);
                numPlayersInput.setHint("Number of players for this game (2-4)");
                numPlayersInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout ll = new LinearLayout(MainActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(gameNameInput);
                ll.addView(numPlayersInput);
                builder.setView(ll);
                builder.setMessage("Note: This will create a new game.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                List<ParseUser> players = new ArrayList<ParseUser>();
                                players.add(currentUser);

                                /**/
                                List<Player> playersList = new ArrayList<Player>();
                                final Player startingPlayer = new Player(currentUser, TAG, MainActivity.this);
                                //startingPlayer.saveInBackground();
                               // public Player(ParseUser thisUser, final String TAG, final Activity context, final Runnable runnable) {

                                playersList.add(startingPlayer);

                                /**/

                                String gameName = gameNameInput.getText().toString();
                                int numPlayers = Integer.parseInt(numPlayersInput.getText().toString());

                                // Game newGame = new Game(gameName, players, numPlayers);

                                Game newGame = new Game(gameName, players, playersList, numPlayers);

                                newGame.saveGame();
                                currentUser.add("game", gameName);
                                currentUser.saveInBackground();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                dialog.dismiss();
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
        });

        Game.findAllGamesNotInProgress(TAG, MainActivity.this, new Runnable() {
            @Override
            public void run() {
                mAllGames = Game.getGames();
                List<String> gamesStringList = new ArrayList<>();
                for (ParseObject Game : mAllGames) {
                    String gameName = Game.getString("name");
                    gamesStringList.add(gameName);
                    setThatList(gamesStringList, mArrayAdapter, mGamesList);
                    makeListClickable(mGamesList);
                }
            }
        });
    } //End of onCreate

    public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView) {
        arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                stringList);
        listView.setAdapter(arrayAdapter);
    }

    public void makeListClickable(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final String gameName = (String) arg0.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, GameHomeActivity.class);
                intent.putExtra("gameName", gameName);
                startActivity(intent);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_quick_login) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Quick Login");
            final EditText userNameInpuut = new EditText(MainActivity.this);
            userNameInpuut.setHint("Username");
            userNameInpuut.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(userNameInpuut);
            final EditText passwordInput = new EditText(MainActivity.this);
            passwordInput.setHint("Password");
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout ll = new LinearLayout(MainActivity.this);
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
                                ErrorHelper.displayAlertDialog(MainActivity.this, getString(R.string
                                        .login_error_message));
                            } else {
                                // Login
                                User.logIn(loginUserName, password, TAG, MainActivity.this, new Runnable() {
                                    @Override
                                    public void run() {
                                      //  Intent intent = new Intent(mContext, YourGamesActivity.class);
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
            Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
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



package com.cultivatingcows.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cultivatingcows.Models.Game;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ParseObject.registerSubclass(User.class);
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
                                String gameName = gameNameInput.getText().toString();
                                int numPlayers = Integer.parseInt(numPlayersInput.getText().toString());
                                Game newGame = new Game(gameName, players, numPlayers);
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



//    public void makeListClickable(ListView listView) {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Join Game");
//                final String gameName = (String) arg0.getItemAtPosition(position);
//                builder.setMessage("By clicking okay, you are agreeing to join this game.").
//                setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        currentUser.add("game", gameName);
//                            currentUser.saveInBackground();
//                            Toast.makeText(MainActivity.this, "Congratulations on joining " + gameName + "!", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });
//
//    }

}



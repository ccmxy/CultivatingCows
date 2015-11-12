package com.cultivatingcows;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cultivatingcows.Models.Game;
import com.cultivatingcows.Models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.greetingText)
    TextView mGreetingText;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ParseObject.registerSubclass(Game.class);
        setSupportActionBar(toolbar);

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("New Game");
                final EditText gameNameInput = new EditText(MainActivity.this);
                gameNameInput.setHint("Name your new game");
                gameNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(gameNameInput);
                builder.setMessage("Note: This will create a new game with you as the owner.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                List<ParseUser> players = new ArrayList<ParseUser>();
                                players.add(currentUser);
                                String gameName = gameNameInput.getText().toString();
                                Game newGame = new Game(gameName, players);
                                newGame.saveGame();
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

        Game.findAllGames(TAG, MainActivity.this, new Runnable() {
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
//        List<String> gamesStringList = new ArrayList<>();
//        for (ParseObject Game : mAllGames) {
//            String gameName = Game.getString("name");
//            gamesStringList.add(gameName);
//        }//End of for loop
//
//        setThatList(gamesStringList, mArrayAdapter, mGamesList);

    } //End of onCreate

        public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView) {
        arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                stringList);
        listView.setAdapter(arrayAdapter);
       // makeListClickable(listView);
    }
    public void makeListClickable(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String gameName = (String) arg0.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Congradulations on clicking " + gameName + "!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}




//        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Game");
//        //Note: Specify here, with, ie, query2.whereEqualTo("G", "Dan Stemkoski");
//        query2.findInBackground(new FindCallback<ParseObject>() {
//            public void done(final List<ParseObject> gameList, ParseException e) {
//                if (e == null) {
//                    Log.d("game", "Retrieved " + gameList.size() + " games");
//
//                    List<String> gamesStringList = new ArrayList<>();
//
//                    for (ParseObject Game : gameList) {
//                        String gameName = Game.getString("name");
//                        gamesStringList.add(gameName);
//
//                        /*Here is how you would get the usernames of the owners (from the relational "owner"):
//                        Game.getParseObject("owner")
//                                .fetchIfNeededInBackground(new GetCallback<ParseUser>() {
//                                    public void done(ParseUser gameOwner, ParseException e) {
//                                        String gameOwnerName = gameOwner.getString("username");
//                                    }
//                                });
//                                */
//
//                    }//End of for loop
//                    setThatList(gamesStringList, mArrayAdapter, mGamesList);
//                } else {
//                    Log.d("game", "Error: " + e.getMessage());
//                }
//            }
//        });
  //  }//End of onCreate method

//    public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView) {
//        arrayAdapter = new ArrayAdapter<String>(
//                MainActivity.this,
//                android.R.layout.simple_list_item_1,
//                stringList);
//        listView.setAdapter(arrayAdapter);
//        makeListClickable(listView);
//    }

//    public void makeListClickable(ListView listView) {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                String line = (String) arg0.getItemAtPosition(position);
//
//                //Specific to this program:
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Game").whereEqualTo("name", line);
//                query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> objects, ParseException e) {
//                        ParseObject thisGame = objects.get(0);
//                        mGameId = thisGame.getObjectId();
//                    }
//                });
//                query.getInBackground(mGameId, new GetCallback<ParseObject>() {
//                    public void done(ParseObject game, ParseException e) {
//                        if (e == null) {
//                            List<ParseUser> players = game.getList("players");
//                            players.add(currentUser);
//                            game.put("players", players);
//                            game.saveInBackground();
//                        } else {
//                            Toast.makeText(MainActivity.this, "MAYDAY", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//
//                });
//            }
//        });
//
//    }
//}

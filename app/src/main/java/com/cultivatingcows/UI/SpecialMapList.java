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
import android.widget.SimpleAdapter;

import com.cultivatingcows.ErrorHelper;
import com.cultivatingcows.Models.Player;
import com.cultivatingcows.Models.SpecialMap;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
//import im.delight.android.location.SimpleLocation;

public class SpecialMapList extends AppCompatActivity {
    private static final String TAG = SpecialMapList.class.getSimpleName();

    @Bind(R.id.yourGamesButton)
    Button mYourSpecialMapsButton;

    @Bind(R.id.newGameButton)
    Button mNewSpecialMapButton;

    @Bind(R.id.listView)
    ListView mSpecialMapsList;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.getLocation)
    Button mGetLocation;


    private ArrayAdapter<String> mArrayAdapter;
    private String mSpecialMapId;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    private List<SpecialMap> mAllSpecialMaps;
    private ParseObject mThisSpecialMap;
    private static Context mContext;
//    private SimpleLocation location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_special_map_list);
        ButterKnife.bind(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Player.class);
        ParseObject.registerSubclass(SpecialMap.class);
        setSupportActionBar(toolbar);

        // construct a new instance of SimpleLocation
//        location = new SimpleLocation(this);

//        // if we can't access the location yet
//        if (!location.hasLocationEnabled()) {
//            // ask the user to enable location access
//            SimpleLocation.openSettings(this);
//        }

        mYourSpecialMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialMapList.this, YourMapsActivity.class);
                startActivity(intent);
            }
        });

        mGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialMapList.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        mNewSpecialMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecialMapList.this);
                builder.setTitle("New Map");

                final EditText mapNameInput = new EditText(SpecialMapList.this);
                mapNameInput.setHint("Name your new map");
                mapNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
             //   builder.setView(gameNameInput);

                final EditText latitudeInput = new EditText(SpecialMapList.this);
                latitudeInput.setHint("Latitude");
                latitudeInput.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);

                final EditText longitudeInput = new EditText(SpecialMapList.this);
                longitudeInput.setHint("Longitude");
                longitudeInput.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);

                final EditText msgInput = new EditText(SpecialMapList.this);
                msgInput.setHint("Post a message to the world");
                msgInput.setInputType(InputType.TYPE_CLASS_TEXT);


                LinearLayout ll = new LinearLayout(SpecialMapList.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(mapNameInput);
                ll.addView(latitudeInput);
                ll.addView(longitudeInput);
                ll.addView(msgInput);
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
                                final Player startingPlayer = new Player(currentUser, TAG, SpecialMapList.this);
                                playersList.add(startingPlayer);
                                /**/

                                String specialMapName = mapNameInput.getText().toString();
                                int longitude = Integer.parseInt(longitudeInput.getText().toString());
                                int latitude = Integer.parseInt(latitudeInput.getText().toString());
                                String msg = msgInput.getText().toString();

//                                int latitude = Integer.parseInt(latitudeInput.getText().toString());
//                                int longitude = Integer.parseInt(latitudeInput.getText().toString());

//                                int longitude = 151;
//                                int latitude = -34;

                                SpecialMap newSpecialMap = new SpecialMap(specialMapName, latitude, longitude, msg);

                                newSpecialMap.saveSpecialMap();
                                currentUser.add("specialMap", specialMapName);
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

        SpecialMap.findAllSpecialMaps(TAG, SpecialMapList.this, new Runnable() {
            @Override
            public void run() {
                mAllSpecialMaps = SpecialMap.getSpecialMaps();
                List<String[]> gamesStringList = new ArrayList<>();

                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                for (ParseObject SpecialMap : mAllSpecialMaps) {
                    String name = SpecialMap.getString("name");
                    int latitude = SpecialMap.getInt("latitude");
                    String latitudeString = latitude + "";
                    int longitude = SpecialMap.getInt("longitude");
                    String longitudeString = longitude + "";
                    String msg = SpecialMap.getString("msg");

                    Map<String, String> datum = new HashMap<String, String>(4);
                    datum.put("SpecialMap Name", name);
//                    Toast.makeText(getApplicationContext(), latitudeString, Toast.LENGTH_SHORT).show();
                    datum.put("Latitude", latitudeString);
                    datum.put("Longitude", longitudeString);
                    datum.put("Msg", msg);
                    data.add(datum);
                    setThatList(gamesStringList, mArrayAdapter, mSpecialMapsList, data);
                    makeListClickable(mSpecialMapsList);
                }
            }
        });
    } //End of onCreate

    public void setThatList(List<String[]> stringList, ArrayAdapter<String> arrayAdapter, ListView listView,  List<Map<String, String>> data) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                SpecialMapList.this,
                data,
                android.R.layout.simple_list_item_2,
                new String[] {"SpecialMap Name", "Latitude", "Longitude", "Msg"},
                new int[] {android.R.id.text1, android.R.id.text2, android.R.id.custom});
        listView.setAdapter(simpleAdapter);
    }

    public void makeListClickable(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Map<String, String> datum = (Map<String, String>) arg0.getItemAtPosition(position);
                Object nameObj = datum.get("SpecialMap Name");
                final String name = (String) nameObj;
                Object latObj = datum.get("Latitude");
                final String latitude = (String) latObj;
                Object longObj = datum.get("Longitude");
                final String longitude = (String) longObj;
                Object msgObj = datum.get("Msg");
                final String msg = (String) msgObj;
                Intent intent = new Intent(SpecialMapList.this, MapsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("msg", msg);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(SpecialMapList.this);
            builder.setTitle("Quick Login");
            final EditText userNameInpuut = new EditText(SpecialMapList.this);
            userNameInpuut.setHint("Username");
            userNameInpuut.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(userNameInpuut);
            final EditText passwordInput = new EditText(SpecialMapList.this);
            passwordInput.setHint("Password");
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout ll = new LinearLayout(SpecialMapList.this);
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
                                ErrorHelper.displayAlertDialog(SpecialMapList.this, getString(R.string
                                        .login_error_message));
                            } else {
                                // Login
                                User.logIn(loginUserName, password, TAG, SpecialMapList.this, new Runnable() {
                                    @Override
                                    public void run() {
                                        //  Intent intent = new Intent(mContext, YourSpecialMapsActivity.class);
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
            Intent intent = new Intent(SpecialMapList.this, RegisterLoginActivity.class);
            startActivity(intent);
        }

//        if (id == R.id.action_all_games_page) {
//            startActivity(intent);
//        }
        if (id == R.id.action_your_games_page) {
            Intent intent = new Intent(this,YourGamesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}



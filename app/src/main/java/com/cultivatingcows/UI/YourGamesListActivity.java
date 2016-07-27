package com.cultivatingcows.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class YourGamesListActivity extends AppCompatActivity {
    private List<String> mUserGamesStrings;
    private static final String TAG = YourGamesListActivity.class.getSimpleName();

    @Bind(R.id.yourGamesList)
    ListView mGamesList;

    @Bind(R.id.allGamesButton)
    Button mAllGamesButton;

    private ArrayAdapter<String> mArrayAdapter;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private String mHasBegan;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_games_list);
        ButterKnife.bind(this);
        User theUser = new User(currentUser, TAG, YourGamesListActivity.this, new Runnable() {
            @Override
            public void run() {
                mUserGamesStrings = User.getUserGames();
                    if(mUserGamesStrings != null) {
                        mHasBegan = currentUser.getString("hasBegan");
                        setThatList(mUserGamesStrings, mArrayAdapter, mGamesList);
                        makeListClickable(mGamesList);
                    }
                }
            });

        mAllGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourGamesListActivity.this, GamesListActivity.class);
                startActivity(intent);
            }
        });

    }//END OF ONCREATE
    public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView) {
        arrayAdapter = new ArrayAdapter<String>(
                YourGamesListActivity.this,
                android.R.layout.simple_list_item_1,
                stringList);
        listView.setAdapter(arrayAdapter);
    }
    public void makeListClickable(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final String gameName = (String) arg0.getItemAtPosition(position);
                if(mHasBegan != null) {
                    if (mHasBegan.equals(gameName)) {
                        Intent intent = new Intent(YourGamesListActivity.this, PlayGamePageActivity.class);
                        intent.putExtra("gameName", gameName);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(YourGamesListActivity.this, GameHomeActivity.class);
                        intent.putExtra("gameName", gameName);
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = new Intent(YourGamesListActivity.this, GameHomeActivity.class);
                    intent.putExtra("gameName", gameName);
                    startActivity(intent);
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
        if (id == R.id.action_login_page) {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_all_games_page) {
            Intent intent = new Intent(this, GamesListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_your_games_page) {
            Intent intent = new Intent(this, YourGamesListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_community_map_page) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}

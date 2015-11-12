package com.cultivatingcows;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cultivatingcows.Models.Game;
import com.cultivatingcows.Models.User;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class YourGamesActivity extends AppCompatActivity {
    /*2Delete later*/
    private List<String> mUserGamesStrings;
    //private List<Game> mUserGames;
    private static final String TAG = YourGamesActivity.class.getSimpleName();

    @Bind(R.id.yourGamesList)
    ListView mGamesList;

    private ArrayAdapter<String> mArrayAdapter;
    private ParseUser currentUser = ParseUser.getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_games);
        ButterKnife.bind(this);
        User theUser = new User(currentUser, TAG, YourGamesActivity.this, new Runnable() {
            @Override
            public void run() {
                mUserGamesStrings = User.getUserGames();
                setThatList(mUserGamesStrings, mArrayAdapter, mGamesList);
                makeListClickable(mGamesList);
                }
            });
    }//END OF ONCREATE
    public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView) {
        arrayAdapter = new ArrayAdapter<String>(
                YourGamesActivity.this,
                android.R.layout.simple_list_item_1,
                stringList);
        listView.setAdapter(arrayAdapter);
        //makeListClickable(listView);
    }
    public void makeListClickable(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(YourGamesActivity.this, GameHomeActivity.class);
                startActivity(intent);
//                int three = 4;
//              Toast.makeText(YourGamesActivity.this, "Waka waka", Toast.LENGTH_LONG);
//                final String gameName = (String) arg0.getItemAtPosition(position);
//                Toast.makeText(YourGamesActivity.this, "You are a player in " + gameName, Toast.LENGTH_SHORT);
            }
        });
    }
}

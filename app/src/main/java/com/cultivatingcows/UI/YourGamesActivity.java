package com.cultivatingcows.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class YourGamesActivity extends AppCompatActivity {
    /*2Delete later*/
    private List<String> mUserGamesStrings;
    //private List<Game> mUserGames;
    private static final String TAG = YourGamesActivity.class.getSimpleName();

    @Bind(R.id.yourGamesList)
    ListView mGamesList;

    @Bind(R.id.allGamesButton)
    Button mAllGamesButton;

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
                    if(mUserGamesStrings != null) {
                        setThatList(mUserGamesStrings, mArrayAdapter, mGamesList);
                        makeListClickable(mGamesList);
                    }
                }
            });

        mAllGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourGamesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }//END OF ONCREATE
    public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView) {
        arrayAdapter = new ArrayAdapter<String>(
                YourGamesActivity.this,
                android.R.layout.simple_list_item_1,
                stringList);
        listView.setAdapter(arrayAdapter);
    }
    public void makeListClickable(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final String gameName = (String) arg0.getItemAtPosition(position);
                Intent intent = new Intent(YourGamesActivity.this, GameHomeActivity.class);
                intent.putExtra("gameName", gameName);
                startActivity(intent);
            }
        });
    }
}

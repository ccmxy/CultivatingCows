package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameHomeActivity extends AppCompatActivity {
    private static final String TAG = GameHomeActivity.class.getSimpleName();

    @Bind(R.id.gameNameTextView)
    TextView gameNameText;

    @Bind(R.id.playerNamesTextView)
    TextView mPlayerNamesText;

    private List<ParseUser> mPlayers;
    private String mPlayerNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_home);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String gameName = intent.getStringExtra("gameName");
        gameNameText.setText(gameName);

        User.findPlayers(gameName, TAG, GameHomeActivity.this, new Runnable() {
            @Override
            public void run() {
                mPlayers = User.getPlayers();
                mPlayerNames = getPlayersList();
                mPlayerNamesText.setText(mPlayerNames);
            }
        });
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
        return str.substring(0,str.length()-1);
    }

}

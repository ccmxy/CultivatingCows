package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cultivatingcows.Models.Game;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;
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

    @Bind(R.id.readyToPlayTextView)
    TextView mCanWePlay;

    private List<ParseUser> mPlayers;
    private String mPlayerNames;
    private ParseObject mGame;
    private String enoughPlayersText;
    private int mMaxNumPlayers;
    private int mCurNumPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_home);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String gameName = intent.getStringExtra("gameName");
        gameNameText.setText(gameName);


        User.findPlayers(gameName, TAG, GameHomeActivity.this, new Runnable() {
            @Override
            public void run() {
                mPlayers = User.getPlayers();
                mPlayerNames = getPlayersList();
                mPlayerNamesText.setText(mPlayerNames);
            }
        });

        Game.findGameByName(gameName, TAG, GameHomeActivity.this, new Runnable() {
            @Override
            public void run() {

                mGame = Game.getThisGame();
                mMaxNumPlayers = mGame.getInt("numPlayers");
                mCurNumPlayers = mPlayers.size();
                enoughPlayersText = ("This game needs " + mMaxNumPlayers + " players and we have " + mCurNumPlayers + ". ");
                mGame.put("curNumPlayers", mCurNumPlayers);
                if (mMaxNumPlayers > mCurNumPlayers) {
                    enoughPlayersText += "Find some more players so we can start! ";
                } else {
                    enoughPlayersText += "Let's play!";
                }
                mCanWePlay.setText(enoughPlayersText);
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

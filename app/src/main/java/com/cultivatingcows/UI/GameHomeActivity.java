package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    @Bind(R.id.joinGameButton)
    Button mJoinGameButton;

    @Bind(R.id.beginGameButton)
    Button mBeginGameButton;

    private ParseUser currentUser = ParseUser.getCurrentUser();
    private List<ParseUser> mPlayers;
    private String mPlayerNames;
    private ParseObject mGame;
    private String mEnoughPlayersText;
    private int mMaxNumPlayers;
    private int mCurNumPlayers;
    private boolean curUserIsPlaying;



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
                curUserIsPlaying = checkIfUserInGame(currentUser);
                if(curUserIsPlaying){
                    mJoinGameButton.setVisibility(View.INVISIBLE);
                }
                else{
                    mBeginGameButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        Game.findGameByName(gameName, TAG, GameHomeActivity.this, new Runnable() {
            @Override
            public void run() {
                mGame = Game.getThisGame();
                mMaxNumPlayers = mGame.getInt("numPlayers");
                if(mPlayers != null) {
                    mCurNumPlayers = mPlayers.size();
                }
                else{
                    mCurNumPlayers = 0;
                }
                mEnoughPlayersText = ("This game needs " + mMaxNumPlayers + " players and we have " + mCurNumPlayers + ". ");
                mGame.put("curNumPlayers", mCurNumPlayers);
                if (mMaxNumPlayers > mCurNumPlayers) {
                    mEnoughPlayersText += '\n' + "Find some more players so we can start! ";
                    mBeginGameButton.setVisibility(View.INVISIBLE);
                } else {
                    mEnoughPlayersText += '\n' + "Let's play!";
                }
                mCanWePlay.setText(mEnoughPlayersText);
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

}

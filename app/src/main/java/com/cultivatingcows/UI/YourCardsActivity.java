package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cultivatingcows.Models.Game;
import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class YourCardsActivity extends AppCompatActivity {
    private static final String TAG = YourCardsActivity.class.getSimpleName();

    private ParseUser currentUser = ParseUser.getCurrentUser();
    private ParseObject mParseGame;
    private Game mGame;
    private List<ParseUser> mPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String gameName = intent.getStringExtra("gameName");

        User.findPlayers(gameName, TAG, YourCardsActivity.this, new Runnable() {
            @Override
            public void run() {
                mPlayers = User.getPlayers();
                for (ParseUser player : mPlayers) {
                    int score = player.getInt("score");
                    String username = player.getUsername();
                    String stringToAdd = username + ": score = " + score;
                }
            }
        });


        Game.findGameByName(gameName, TAG, YourCardsActivity.this, new Runnable() {
            @Override
            public void run() {
                mParseGame = Game.getThisGame();
                //mGame = new Game(mParseGame, mPlayers);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

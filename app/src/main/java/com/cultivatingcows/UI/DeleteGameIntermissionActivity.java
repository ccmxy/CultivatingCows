package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cultivatingcows.Models.Game;
import com.cultivatingcows.R;
import com.parse.ParseObject;

import java.util.Timer;
import java.util.TimerTask;

public class DeleteGameIntermissionActivity extends AppCompatActivity {
    private static final String TAG = DeleteGameIntermissionActivity.class.getSimpleName();
    private ParseObject mParseGame;
    private Game mGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_game_intermission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        final String gameName = intent.getStringExtra("gameName");

        Game.findGameByName(gameName, TAG, DeleteGameIntermissionActivity.this, new Runnable() {
            @Override
            public void run() {
                //At this point Game can access the Parse Object
                // it searched for in mParseGame because mParseGame
                // was retrieved and set as a static member of the class
                // before the runnable began.
                mParseGame = Game.getThisGame();
                mParseGame.deleteInBackground();


            }
        });
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(DeleteGameIntermissionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 3000);

    }

}
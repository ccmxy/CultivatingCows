package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cultivatingcows.Models.Game;
import com.cultivatingcows.R;
import com.parse.ParseObject;

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

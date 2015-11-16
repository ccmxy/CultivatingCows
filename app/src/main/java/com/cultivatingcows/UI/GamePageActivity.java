package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cultivatingcows.Models.Game;
import com.cultivatingcows.R;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GamePageActivity extends AppCompatActivity {
    private static final String TAG = GamePageActivity.class.getSimpleName();
    private ParseObject mParseGame;
    private Game mGame;

    @Bind(R.id.whosTurnText)
    TextView mWhosTurnText;

    @Bind(R.id.gameNameGamePageText)
    TextView gameNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String gameName = intent.getStringExtra("gameName");
        gameNameText.setText(gameName);

       // final String gameName, final String tag, final Activity context, final Runnable runnable)

        Game.findGameByName(gameName, TAG, GamePageActivity.this, new Runnable() {
            @Override
            public void run() {
                mParseGame = Game.getThisGame();
                //mGame = new Game(mParseGame);
            }
        });

       // ParseUser whosTurn = mGame.getWhosTurn();





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

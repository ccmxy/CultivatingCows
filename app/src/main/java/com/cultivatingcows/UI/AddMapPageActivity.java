package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.cultivatingcows.Models.SpecialMap;
import com.cultivatingcows.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddMapPageActivity extends AppCompatActivity {
    private static final String TAG = AddMapPageActivity.class.getSimpleName();
    @Bind(R.id.latitudeEditText)
    BootstrapEditText mLatitudeEditText;

    @Bind(R.id.longitudeEditText)
    BootstrapEditText mLongitudeEditText;

    @Bind(R.id.messageEditText)
    BootstrapEditText mMessageEditText;

    @Bind(R.id.newMarkerButton)
    BootstrapButton mNewMapButton;

    @Bind(R.id.getLocationButton)
    BootstrapButton mGetLocationButton;

    @Bind(R.id.goToMapButton)
    BootstrapButton mGoToMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_map_page);
        ButterKnife.bind(this);
        mNewMapButton.setActivated(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int longitude = Integer.valueOf(mLatitudeEditText.getText().toString().trim());
                int latitude = Integer.valueOf(mLongitudeEditText.getText().toString().trim());
                String msg = mMessageEditText.getText().toString().trim();
                mLatitudeEditText.setVisibility(View.GONE);
                mLongitudeEditText.setVisibility(View.GONE);
                mMessageEditText.setVisibility(View.GONE);


                SpecialMap newSpecialMap = new SpecialMap(latitude, longitude, msg);

                newSpecialMap.saveSpecialMap();

            }

        });

        mGetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMapPageActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMapPageActivity.this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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

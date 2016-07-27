package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.cultivatingcows.Models.SpecialMap;
import com.cultivatingcows.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseObject;

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

    @Bind(R.id.reavealAddMarkFormButton)
    BootstrapButton mRevealAddMarkFormButton;

    @Bind(R.id.newMarkerButton)
    BootstrapButton mNewMapButton;

    @Bind(R.id.getLocationButton)
    BootstrapButton mGetLocationButton;

    @Bind(R.id.goToMapButton)
    BootstrapButton mGoToMap;

    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_map_page);
        ButterKnife.bind(this);
        ParseObject.registerSubclass(SpecialMap.class);

        mNewMapButton.setActivated(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRevealAddMarkFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongitudeEditText.setVisibility(View.VISIBLE);
                mLatitudeEditText.setVisibility(View.VISIBLE);
                mMessageEditText.setVisibility(View.VISIBLE);
                mNewMapButton.setVisibility(View.VISIBLE);
            }
        });

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
                Intent intent = new Intent(AddMapPageActivity.this, UserLatlongActivity.class);
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
    }

}

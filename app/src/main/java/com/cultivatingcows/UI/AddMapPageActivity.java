package com.cultivatingcows.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.cultivatingcows.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

////******* Activity where there are options to either get geolocation, add a marker
// to the community map, or view the community map. All take you to new activities.********/////

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
    private static Context mContext;
    private ParseObject mThisGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        setContentView(R.layout.activity_add_map_page);
        ButterKnife.bind(this);

        //Really a newMarkerButton. Adds
        // user-customized marker to the community map.
        mNewMapButton.setActivated(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Make the text inputs for adding a new marker visible:
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
                //Just for styling:
                mNewMapButton.setActivated(true);
                //Get the user's values out of the EditTexts:
                int latitude = Integer.valueOf(mLatitudeEditText.getText().toString().trim());
                int longitude = Integer.valueOf(mLongitudeEditText.getText().toString().trim());
                String msg = mMessageEditText.getText().toString().trim();
                mLatitudeEditText.setVisibility(View.GONE);
                mLongitudeEditText.setVisibility(View.GONE);
                mMessageEditText.setVisibility(View.GONE);
                mNewMapButton.setVisibility(View.GONE);
                //Create the new SpecialMap object:
                ParseObject specialMap = new ParseObject("SpecialMap");
                specialMap.put("latitude", latitude);
                specialMap.put("longitude", longitude);
                specialMap.put("msg", msg);
                //Save SpecialMap object to database:
                specialMap.saveInBackground();
            }

        });

        mGetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMapPageActivity.this, UserLatlongActivity.class);
                startActivity(intent);
            }
        });

        mGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMapPageActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

}

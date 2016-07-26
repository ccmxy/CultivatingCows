package com.cultivatingcows.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cultivatingcows.Models.SpecialMap;
import com.cultivatingcows.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import java.util.List;

public class MapsActivity extends FragmentActivity {
    private static final String TAG =MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private List<SpecialMap> mAllSpecialMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
//        final String name = intent.getStringExtra("name");
//        final String latitude = intent.getStringExtra("latitude");
//        final String longitude = intent.getStringExtra("longitude");
//        final String msg = intent.getStringExtra("msg");
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {

//
//        int latInt = Integer.parseInt(latitude);
//        int longInt = Integer.parseInt(longitude);
//
//        LatLng sydney = new LatLng(latInt, longInt);
//
//        mMap.addMarker(new MarkerOptions().position(sydney).title(msg));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        SpecialMap.findAllSpecialMaps(TAG, MapsActivity.this, new Runnable() {
            @Override
            public void run() {
                mAllSpecialMaps = SpecialMap.getSpecialMaps();
                for (ParseObject SpecialMap : mAllSpecialMaps) {
                    int latitude = SpecialMap.getInt("latitude");
                    int longitude = SpecialMap.getInt("longitude");
                    String msg = SpecialMap.getString("msg");
                    LatLng sydney = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(sydney).title(msg));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            }
        });

    }
}

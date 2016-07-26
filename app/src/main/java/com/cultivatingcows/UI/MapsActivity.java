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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity {
    private static final String TAG =MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private List<SpecialMap> mAllSpecialMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String latitude = intent.getStringExtra("latitude");
        final String longitude = intent.getStringExtra("longitude");
        final String msg = intent.getStringExtra("msg");
//        Toast.makeText(MapsActivity.this, name, Toast.LENGTH_SHORT).show();
//        Toast.makeText(MapsActivity.this, latitude, Toast.LENGTH_SHORT).show();
        setUpMapIfNeeded(latitude, longitude, msg);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setUpMapIfNeeded(String latitude,);
//    }


    private void setUpMapIfNeeded(String latitude, String longitude, String msg) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(latitude, longitude, msg);
            }
        }
    }


    private void setUpMap(String latitude, String longitude, String msg) {


        int latInt = Integer.parseInt(latitude);
        int longInt = Integer.parseInt(longitude);

        LatLng sydney = new LatLng(latInt, longInt);

//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(sydney).title(msg));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        SpecialMap.findAllSpecialMaps(TAG, MapsActivity.this, new Runnable() {
            @Override
            public void run() {
                mAllSpecialMaps = SpecialMap.getSpecialMaps();
                List<String[]> gamesStringList = new ArrayList<>();

                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                for (ParseObject SpecialMap : mAllSpecialMaps) {
                    String name = SpecialMap.getString("name");
                    int latitude = SpecialMap.getInt("latitude");
                    String latitudeString = latitude + "";
                    int longitude = SpecialMap.getInt("longitude");
                    String longitudeString = longitude + "";
                    String msg = SpecialMap.getString("msg");
                    LatLng sydney = new LatLng(latitude, longitude);

                    mMap.addMarker(new MarkerOptions().position(sydney).title(msg));


                }
            }
        });





//        mMap.addMarker(new MarkerOptions().position(new LatLng(27.175306, 78.042144)).title("Taj Mahal").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.175306, 78.042144), 15));

    }
}

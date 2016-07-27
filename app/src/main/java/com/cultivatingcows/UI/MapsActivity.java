package com.cultivatingcows.UI;

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

/*This is the activity that shows a full-screen map of all of the markers that Users
have added to the community map.
 */
public class MapsActivity extends FragmentActivity {
    private static final String TAG =MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private List<SpecialMap> mAllSpecialMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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


    //Set up the map with all markers and zoom in on the last one:
    private void setUpMap() {
        SpecialMap.findAllSpecialMaps(TAG, MapsActivity.this, new Runnable() {
            @Override
            public void run() {
                mAllSpecialMaps = SpecialMap.getSpecialMaps();
                for (ParseObject SpecialMap : mAllSpecialMaps) {
                    int latitude = SpecialMap.getInt("latitude");
                    int longitude = SpecialMap.getInt("longitude");
                    String msg = SpecialMap.getString("msg");
                    LatLng marker = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(marker).title(msg));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                }
            }
        });

    }
}

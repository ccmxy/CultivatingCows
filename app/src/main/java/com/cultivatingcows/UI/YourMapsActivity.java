package com.cultivatingcows.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class YourMapsActivity extends AppCompatActivity {
    /*2Delete later*/
    private List<String> mUserMapsStrings;
    //private List<Map> mUserMaps;
    private static final String TAG = YourMapsActivity.class.getSimpleName();

    @Bind(R.id.yourMapsList)
    ListView mMapsList;

    @Bind(R.id.allMapsButton)
    Button mAllMapsButton;

    private ArrayAdapter<String> mArrayAdapter;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private String mHasBegan;
    private static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_maps);
        ButterKnife.bind(this);
        User theUser = new User(currentUser, TAG, YourMapsActivity.this, new Runnable() {
            @Override
            public void run() {
                mUserMapsStrings = User.getUserMapNames();
                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                if(mUserMapsStrings != null) {
                    //mHasBegan = currentUser.getString("hasBegan");
                    for (String thisMapString: mUserMapsStrings) {
                        Map<String, String> datum = new HashMap<String, String>(4);
                        String[] thisMapArray = thisMapString.split("\\+");

                        datum.put("SpecialMap Name", thisMapArray[0]);
                        datum.put("Latitude", thisMapArray[1]);
                        datum.put("Longitude", thisMapArray[2]);
                        datum.put("Msg", thisMapArray[3]);
                        data.add(datum);
                        setThatList(mUserMapsStrings, mArrayAdapter, mMapsList, data);
                        makeListClickable(mMapsList);
                    }
                }
            }
        });

    }//END OF ONCREATE
    public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView, List<Map<String, String>> data) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                YourMapsActivity.this,
                data,
                android.R.layout.simple_list_item_2,
                new String[] {"SpecialMap Name", "Latitude", "Longitude", "Msg"},
                new int[] {android.R.id.text1, android.R.id.text2, android.R.id.custom});
        listView.setAdapter(simpleAdapter);
    }

    public void makeListClickable(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Map<String, String> datum = (Map<String, String>) arg0.getItemAtPosition(position);
                Object nameObj = datum.get("SpecialMap Name");
                final String name = (String) nameObj;
                Object latObj = datum.get("Latitude");
                final String latitude = (String) latObj;
                Object longObj = datum.get("Longitude");
                final String longitude = (String) longObj;
                Object msgObj = datum.get("Msg");
                final String msg = (String) msgObj;
                Intent intent = new Intent(YourMapsActivity.this, MapsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("msg", msg);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login_page) {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
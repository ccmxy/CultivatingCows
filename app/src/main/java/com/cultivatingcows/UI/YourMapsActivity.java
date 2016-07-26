package com.cultivatingcows.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cultivatingcows.Models.User;
import com.cultivatingcows.R;
import com.parse.ParseUser;

import java.util.List;

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
                mUserMapsStrings = User.getUserMaps();
                if(mUserMapsStrings != null) {
                    mHasBegan = currentUser.getString("hasBegan");
                    setThatList(mUserMapsStrings, mArrayAdapter, mMapsList);
                   // makeListClickable(mMapsList);
                }
            }
        });

        mAllMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourMapsActivity.this, SpecialMapList.class);
                startActivity(intent);
            }
        });

    }//END OF ONCREATE
    public void setThatList(List<String> stringList, ArrayAdapter<String> arrayAdapter, ListView listView) {
        arrayAdapter = new ArrayAdapter<String>(
                YourMapsActivity.this,
                android.R.layout.simple_list_item_1,
                stringList);
        listView.setAdapter(arrayAdapter);
    }
//    public void makeListClickable(ListView listView) {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                final String mapName = (String) arg0.getItemAtPosition(position);
//                if(mHasBegan != null) {
//                    if (mHasBegan.equals(mapName)) {
//                        Intent intent = new Intent(YourMapsActivity.this, MapPageActivity.class);
//                        intent.putExtra("mapName", mapName);
//                        startActivity(intent);
//                    }
//                    else{
//                        Intent intent = new Intent(YourMapsActivity.this, MapHomeActivity.class);
//                        intent.putExtra("mapName", mapName);
//                        startActivity(intent);
//                    }
//                }
//                else {
//                    Intent intent = new Intent(YourMapsActivity.this, MapHomeActivity.class);
//                    intent.putExtra("mapName", mapName);
//                    startActivity(intent);
//                }
//            }
//        });
//    }

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
//        if (id == R.id.action_all_maps_page) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
//        if (id == R.id.action_your_maps_page) {
//            Intent intent = new Intent(this, YourMapsActivity.class);
//            startActivity(intent);
//        }
        return super.onOptionsItemSelected(item);
    }


}

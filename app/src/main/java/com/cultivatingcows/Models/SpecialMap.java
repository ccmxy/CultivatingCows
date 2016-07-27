package com.cultivatingcows.Models;

import android.app.Activity;

import com.cultivatingcows.ErrorHelper;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * SpecialMaps are markers for the community map.
 */
@ParseClassName("SpecialMap")
public class SpecialMap extends ParseObject {
    private static List<SpecialMap> mSpecialMaps;

    //A query that's run in findAllSpecialMaps:
    public static ParseQuery<SpecialMap> specialMapsListQuery() {
        return ParseQuery.getQuery(SpecialMap.class)
                .orderByAscending("name");
    }

    //This function is always run with a runnable which manipulates the retrieved objects:
    public static void findAllSpecialMaps(final String tag, final Activity context, final Runnable runnable) {
        specialMapsListQuery().findInBackground(new FindCallback<SpecialMap>() {
            @Override
            public void done(List<SpecialMap> specialMaps, com.parse.ParseException e) {
                if (e == null) {
                    mSpecialMaps = specialMaps;
                    context.runOnUiThread(runnable);
                } else {
                    ErrorHelper.handleError(tag, context, e.getMessage());
                }
            }
        });
    }

    //This is called in the runnable that's sent into findAllSpecialMaps.
    public static List<SpecialMap> getSpecialMaps() {
        return mSpecialMaps;
    }

    //I don't use this constructor, or the save method below. I
    // create with new ParseObject and save manually.
    public SpecialMap(int latitude, int longitude, String msg){
        put("latitude", latitude);
        put("longitude", longitude);
        put("msg", msg);
    }

    public void saveSpecialMap() {
        saveInBackground();
    }
}

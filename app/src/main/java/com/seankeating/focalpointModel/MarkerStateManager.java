package com.seankeating.focalpointModel;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Sean on 30/03/2016.
 */
public class MarkerStateManager {

    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";

    private static final String PREFS_NAME = "mapMarkerState";

    private SharedPreferences mapStatePrefs;

    public MarkerStateManager(Context context) {
        mapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveMapState(LatLng customLocation) {
        SharedPreferences.Editor editor = mapStatePrefs.edit();


        //store lat and lon of marker
        editor.putFloat(LATITUDE, (float) customLocation.latitude);
        editor.putFloat(LONGITUDE, (float) customLocation.longitude);
        editor.commit();
    }

    public MarkerOptions getSavedMarkerPosition() {

        double latitude = mapStatePrefs.getFloat(LATITUDE, 0);
        if (latitude == 0) {
            return null;
        }
        double longitude = mapStatePrefs.getFloat(LONGITUDE, 0);
        LatLng target = new LatLng(latitude, longitude);


        //make marker and return it for use
        MarkerOptions options = new MarkerOptions()
                .draggable(true)
                .position(target)
                .title("You");
        return options;
    }

    public void delete() {
       //delete copy of marker
        SharedPreferences.Editor editor = mapStatePrefs.edit();
        editor.clear();
        editor.commit();
    }
}

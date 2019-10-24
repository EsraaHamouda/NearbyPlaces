package com.example.nearbylocations;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String TAG ="Logger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Log.i(TAG, latitude+","+longitude);
        APIHandler apiHandler = new APIHandler(this);
        apiHandler.callNearbyLocationsAPI( latitude+","+longitude);
    }


}

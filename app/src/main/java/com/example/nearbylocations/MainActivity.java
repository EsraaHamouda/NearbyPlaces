package com.example.nearbylocations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity implements APIHandlerInterface {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private String TAG = "Logger";
    private ProgressDialog loadingDialog;
    private VenueRecyclerAdapter venuesRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private ImageView errorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.venues_recycler_view);
        errorImage = findViewById(R.id.error_image);

        if (checkPermission()) {
            getNearbyLocations();
        } else {
            requestPermission();
        }
    }

    private void getNearbyLocations() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Log.i(TAG, latitude + "," + longitude);
        APIHandler apiHandler = new APIHandler(this);
        apiHandler.callNearbyLocationsAPI(this, latitude, longitude);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reload:
                getNearbyLocations();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE);

    }

    public void inflateData(ArrayList<VenueModel> venuesList) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        venuesRecyclerAdapter = new VenueRecyclerAdapter(venuesList, MainActivity.this);
        mRecyclerView.setAdapter(venuesRecyclerAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {
                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void startRequest() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading");
        loadingDialog.setIndeterminate(false);
        loadingDialog.setCancelable(false);

        loadingDialog.show();
    }

    @Override
    public void handleSuccessfulRequest(ArrayList<VenueModel> list) {
        if (list.size() == 0) {
            errorImage.setVisibility(View.VISIBLE);
            errorImage.setBackground(getResources().getDrawable(R.drawable.ic_error_occured));
            mRecyclerView.setVisibility(View.GONE);

        } else {
            errorImage.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        inflateData(list);
        loadingDialog.dismiss();
    }

    @Override
    public void handleFailedRequest() {

        errorImage.setVisibility(View.VISIBLE);
        errorImage.setBackground(getResources().getDrawable(R.drawable.ic_no_data_found));
        mRecyclerView.setVisibility(View.GONE);

        loadingDialog.dismiss();
    }
}

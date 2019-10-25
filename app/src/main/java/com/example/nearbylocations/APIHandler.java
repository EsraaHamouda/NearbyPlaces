package com.example.nearbylocations;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class APIHandler {
    private String CLIENT_ID = "2SC4BFKQTZ1DTDHM2ZGEIJEKRWSTZJ2232ESN43LNYFW1KSS";
    private String CLIENT_SECRET = "KZCIKUYMEMK0G10OU3TSQRQQVRIF3MIS2PJZ4WKDBT4RFZ0S";
    private Context context;
    private ArrayList<VenueModel> venuesList = new ArrayList<>();

    public APIHandler(Context context) {
        this.context = context;
    }

    public void callNearbyLocationsAPI(final APIHandlerInterface apiHandlerInterface, double lat, double longi) {
        apiHandlerInterface.startRequest();
        Date currentDay = new Date();

        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDateStr = sdf.format(currentDay);

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        String url = " https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&v=" + currentDateStr + "%20" +
                "&ll=" + lat + "," + longi;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                venuesList.clear();

                try {
                    JSONArray items = response.getJSONObject("response").getJSONArray("venues");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject venuesJson = items.getJSONObject(i);//.getJSONObject("venue");
                        venuesList.add(new VenueModel(
                                venuesJson.getString("name"),
                                venuesJson.getJSONObject("location").getJSONArray
                                        ("formattedAddress").getString(0),
                                venuesJson.getJSONArray("categories")
                        ));
                    }
                    apiHandlerInterface.handleSuccessfulRequest(venuesList);
                } catch (Exception e) {

                    apiHandlerInterface.handleFailedRequest();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiHandlerInterface.handleFailedRequest();
            }
        });
        mRequestQueue.add(req);
    }
}

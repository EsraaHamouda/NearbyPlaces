package com.example.nearbylocations;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class APIHandler {
    private String CLIENT_ID = "2SC4BFKQTZ1DTDHM2ZGEIJEKRWSTZJ2232ESN43LNYFW1KSS";
    private String CLIENT_SECRET = "KZCIKUYMEMK0G10OU3TSQRQQVRIF3MIS2PJZ4WKDBT4RFZ0S";
    private Context context;

    public APIHandler(Context context) {
        this.context = context;
    }

    public void callNearbyLocationsAPI(String latLong) {

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JSONObject postparams = new JSONObject();

        try {
            postparams.put("client_id", CLIENT_ID);
            postparams.put("client_secret", CLIENT_SECRET);
            postparams.put("ll",  latLong);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "https://api.foursquare.com/v2/venues/explore";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                postparams, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("Logger", response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Logger", error.toString());
            }
        });

        mRequestQueue.add(req);
    }
}

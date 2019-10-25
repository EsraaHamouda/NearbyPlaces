package com.example.nearbylocations;

import org.json.JSONArray;
import org.json.JSONException;

public class VenueModel {

  private String venueName;
  private String venueAddress;
  private String venueIcon;
  private final static String venueIconSize = "64";

  public VenueModel(String venueName, String venueAddress, JSONArray venueCategories) {
    this.venueName = venueName;
    this.venueAddress = venueAddress;
    this.venueIcon = buildIconUri(venueCategories);
  }

  private String buildIconUri(JSONArray venueCategories) {

    try {
      return venueCategories.getJSONObject(0).getJSONObject("icon").getString("prefix") + venueIconSize
          + venueCategories.getJSONObject(0).getJSONObject("icon").getString("suffix");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getVenueName() {
    return venueName;
  }

  public String getVenueAddress() {
    return venueAddress;
  }


  public String getVenueIcon() {
    return venueIcon;
  }

}
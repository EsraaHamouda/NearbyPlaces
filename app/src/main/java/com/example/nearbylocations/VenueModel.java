package pt.luiscunha.product_developer_test;

import org.json.JSONArray;
import org.json.JSONException;

public class VenueModel {

  private String venueName;
  private String venueRating;
  private String venueIcon;
  private final static String venueIconSize = "64";

  public VenueModel(String venueName, String venueRating, JSONArray venueCategories) {
    this.venueName = venueName;
    this.venueRating = venueRating;
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

  public String getVenueRating() {
    return venueRating;
  }


  public String getVenueIcon() {
    return venueIcon;
  }

}
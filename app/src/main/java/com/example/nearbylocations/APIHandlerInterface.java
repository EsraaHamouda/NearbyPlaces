package com.example.nearbylocations;

import java.util.ArrayList;

public interface APIHandlerInterface {
    void startRequest();
    void handleSuccessfulRequest(ArrayList<VenueModel> list);
    void handleFailedRequest();


}

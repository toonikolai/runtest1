package com.example.nikolaistakheiko.runtest1;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nikolaistakheiko on 2017-10-10.
 */

public class PushData {

//    LatLng latlng;
    String username;
    int distance;
    String timeofday;
    int pace;
    int group;
    String i_d;

    public PushData() {

    }

    public PushData(/*LatLng latlng,*/ String username, int distance, String timeofday, int pace, int group, String i_d) {
//        this.latlng = latlng;
        this.username = username;
        this.distance = distance;
        this.timeofday = timeofday;
        this.pace = pace;
        this.group = group;
        this.i_d = i_d;
    }

//    public LatLng getLatlng() {
//        return latlng;
//    }

    public String getUsername() {
        return username;
    }

    public int getDistance() {
        return distance;
    }

    public String getTimeofday() {
        return timeofday;
    }

    public int getPace() {
        return pace;
    }

    public int getGroup() {
        return group;
    }

    public String getI_d() {
        return i_d;
    }
}

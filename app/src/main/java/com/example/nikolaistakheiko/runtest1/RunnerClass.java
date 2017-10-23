package com.example.nikolaistakheiko.runtest1;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nikolaistakheiko on 2017-10-10.
 */

public class RunnerClass {

    Double lat;
    Double lng;
    String username;
    int distance;
    String timeofday;
    int pace;
    int group;
    String pic_url;
    String i_d;

    public RunnerClass() {

    }

    public RunnerClass(Double lat, Double lng, String username, int distance, String timeofday, int pace, int group, String pic_url, String i_d) {
        this.lat = lat;
        this.lng = lng;
        this.username = username;
        this.distance = distance;
        this.timeofday = timeofday;
        this.pace = pace;
        this.group = group;
        this.pic_url = pic_url;
        this.i_d = i_d;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

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

    public String getPic_url() {
        return pic_url;
    }
}

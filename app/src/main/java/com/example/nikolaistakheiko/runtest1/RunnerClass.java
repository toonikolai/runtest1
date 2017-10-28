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
    String ui_d;
    String i_d;

    public RunnerClass() {

    }

    public RunnerClass(Double lat, Double lng, String username, int distance, String timeofday, int pace, int group, String pic_url, String ui_d, String i_d) {
        this.lat = lat;
        this.lng = lng;
        this.username = username;
        this.distance = distance;
        this.timeofday = timeofday;
        this.pace = pace;
        this.group = group;
        this.pic_url = pic_url;
        this.ui_d = ui_d;
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

    public String getPic_url() {
        return pic_url;
    }

    public String getUi_d() {
        return ui_d;
    }

    public String getI_d() {
        return i_d;
    }
}

package com.example.nikolaistakheiko.runtest1;

/**
 * Created by nikolaistakheiko on 2017-10-29.
 */

public class RunRequestClass {

    Double lat;
    Double lng;
    String username;
    int distance;
    String timeofday;
    int pace;
    int group;
    String pic_url;
    String id;
    String uid;

    public RunRequestClass() {

    }

    public RunRequestClass(Double lat, Double lng, String username, int distance, String timeofday, int pace, int group, String pic_url, String uid, String id) {
        this.lat = lat;
        this.lng = lng;
        this.username = username;
        this.distance = distance;
        this.timeofday = timeofday;
        this.pace = pace;
        this.group = group;
        this.pic_url = pic_url;
        this.id = id;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
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
}

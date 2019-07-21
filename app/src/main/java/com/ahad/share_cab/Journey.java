package com.ahad.share_cab;

public class Journey {

    private String date,time,pushid,route;



    public Journey(String date, String time, String pushid, String route) {
        this.date = date;
        this.time = time;
        this.pushid = pushid;
        this.route= route ;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }
}

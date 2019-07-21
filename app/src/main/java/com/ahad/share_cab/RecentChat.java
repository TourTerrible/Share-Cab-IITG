package com.ahad.share_cab;

public class RecentChat {
    private String name,userid,last_msg_single,time_last_msg;

    public RecentChat(String name, String userid,String last_msg_single, String time_last_msg) {
        this.name = name;
        this.userid = userid;
        this.last_msg_single= last_msg_single;
        this.time_last_msg=time_last_msg;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime_last_msg() {
        return time_last_msg;
    }

    public void setTime_last_msg(String time_last_msg) {
        this.time_last_msg = time_last_msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_msg_single() {
        return last_msg_single;
    }

    public void setLast_msg_single(String last_msg_single) {
        this.last_msg_single = last_msg_single;
    }

}

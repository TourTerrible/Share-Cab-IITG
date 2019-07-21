package com.ahad.share_cab;

public class Person {


    private  String name;
    private  String surname, time_single;
    private  String userid;

    public Person(String name, String surname,String userid, String time_single) {
        this.name = name;
        this.surname = surname;
        this.userid=userid;
        this.time_single= time_single;
    }

    public String getTime_single() {
        return time_single;
    }

    public void setTime_single(String time_single) {
        this.time_single = time_single;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.name = userid;
    }
}

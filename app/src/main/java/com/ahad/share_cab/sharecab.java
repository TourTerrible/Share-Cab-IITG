package com.ahad.share_cab;

import android.app.Application;

import  com.firebase.client.Firebase;

public class sharecab extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}



package com.example.music.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by xu on 2016/9/13.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}

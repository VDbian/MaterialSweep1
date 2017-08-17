package com.example.fei.materialsweep.application;

import android.app.Application;

import com.example.fei.materialsweep.utils.CrashHander;

/**
 * Created by fei on 2017/7/19.
 */

public class MyApplication extends Application {

    public static String USER_ID = "";
    public static String USER_NAME = "";
    public static final String Error_DIR_NAME = "crash";
    public static final String TAG_OCCURRED_ERROR = "crashed";

    public String versionApp;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHander crashHander =CrashHander.getInstance();
//        crashHander.init(this);
    }

}

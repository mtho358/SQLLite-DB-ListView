package com.coolcats.sqlitedatabaselistview.util;

import android.util.Log;

public class Logger {


    private static final String TAG = "TAG_M";

    public static void logMessage(String message){
        Log.d(TAG, message);
    }
}

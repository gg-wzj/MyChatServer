package com.example.mychatserver;

import android.app.Application;
import android.content.Context;

/**
 * Created by wzj on 2017/9/23.
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        MyApplication.mContext = mContext;
    }
}

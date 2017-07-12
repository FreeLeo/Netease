package com.xiongan.develop.news.volleyplus;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.xiongan.develop.news.BuildConfig;

public class VolleyPlus {
    private static RequestQueue mRequestQueue;
    public static final String CODE = "code";
    public static final String MSG = "message";
    public static final String DATA = "info";

    private VolleyPlus() {
    }

    public static void openLogInfo() {
        VolleyLog.DEBUG = BuildConfig.DEBUG;
    }

    // 程序入口初始化一次
    public static void init(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void cancelAll(Context context) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(context);
        }
    }
}

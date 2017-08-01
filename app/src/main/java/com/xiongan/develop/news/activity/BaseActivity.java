package com.xiongan.develop.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiongan.develop.news.volleyplus.VolleyPlus;

public class BaseActivity extends AppCompatActivity {
    public final String TAG = BaseActivity.class.getSimpleName() + hashCode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyPlus.getRequestQueue().cancelAll(TAG);
    }
}

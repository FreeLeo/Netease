package com.xiongan.develop.news.activity;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.xiongan.develop.news.volleyplus.VolleyPlus;

public class BaseActivity extends com.unbelievable.library.android.app.BaseActivity {
    public final String TAG = BaseActivity.class.getSimpleName() + hashCode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getVolleyTag() {
        return TAG;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyPlus.getRequestQueue().cancelAll(TAG);
    }
}

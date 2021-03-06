package com.xiongan.develop.news.activity;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.unbelievable.library.android.volleyplus.VolleyPlus;

public class BaseActivity extends com.unbelievable.library.android.app.BaseActivity {
    public final String TAG = BaseActivity.class.getSimpleName() + hashCode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
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

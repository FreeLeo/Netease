package com.xiongan.develop.news;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.unbelievable.library.android.utils.Logger;
import com.unbelievable.library.android.utils.PreferencesUtil;
import com.xiongan.develop.news.util.ScreenUtil;

/**
 * Created by HHX on 15/9/12.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;
    public static int width = 0;
    public static int height = 0;
    public static float density = 0;
    public void onCreate() {
        super.onCreate();
        width = ScreenUtil.getWidth(this);
        height = ScreenUtil.getHeight(this);
        density = ScreenUtil.getDensity(this);
        mInstance = this;

        com.unbelievable.library.android.volleyplus.VolleyPlus.init(this);
        Fresco.initialize(this);
        PreferencesUtil.initialize(this);
//        initPush();
    }

    public static Context getContext(){
        return mInstance;
    }

    private void initPush(){
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.d("MyApplication",deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
}

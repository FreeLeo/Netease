package com.xiongan.develop.news.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.unbelievable.library.android.utils.Logger;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.transcation.NewsChannelsTranscation;

import rx.Subscriber;

/**
 * Created by apple on 16/4/17.
 */
public class SplashActivity extends BaseActivity {
    private final String TAG = SplashActivity.class.getSimpleName();
    private View rootView;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        rootView = View.inflate(this, R.layout.fragment_splash, null);
        setContentView(rootView);
        getNewsChannel();
        if (Build.VERSION.SDK_INT >= 23) {
            CheckPermission();
        } else {
            initStartAnimation();
        }
    }

    private void CheckPermission() {
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.CAMERA)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e(TAG,"RxPermissions: Complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG,"RxPermissions: Error: " + e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                        } else {
                        }
                        initStartAnimation();
                    }
                });
    }

    private void initStartAnimation() {
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1500);
        rootView.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                gotoNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });
    }

    private void getNewsChannel(){
        new NewsChannelsTranscation(TAG,null).excute();
    }

    // 执行跳转
    private void gotoNext() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}

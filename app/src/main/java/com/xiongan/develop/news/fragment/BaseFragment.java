package com.xiongan.develop.news.fragment;

import android.support.v4.app.Fragment;

import com.xiongan.develop.news.volleyplus.VolleyPlus;

/**
 * Created by admin on 2017/7/28.
 */

public class BaseFragment extends Fragment{
    public final String TAG = BaseFragment.class.getSimpleName() + hashCode();

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyPlus.getRequestQueue().cancelAll(TAG);
    }
}

package com.xiongan.develop.news.transcation;

import com.google.gson.Gson;
import com.unbelievable.library.android.utils.DeviceUtil;
import com.unbelievable.library.android.volleyplus.BaseJsonTransaction;
import com.unbelievable.library.android.volleyplus.HttpCallback;
import com.unbelievable.library.android.volleyplus.ResponseEntity;
import com.xiongan.develop.news.MyApplication;
import com.xiongan.develop.news.bean.newstext.NewsID;
import com.xiongan.develop.news.config.URLs;

import org.json.JSONException;

/**
 * Created by admin on 2017/5/23.
 */

public class NewsDetailTranscation extends BaseJsonTransaction {
    private String nid;
    public NewsDetailTranscation(String nid,String tag, HttpCallback callback) {
        super(callback,tag);
        this.nid = nid;
    }

    @Override
    public void prepareRequestOther() {
        setShouldCache(true);
        setParam("deviceId", DeviceUtil.getDeviceId(MyApplication.getContext()));
    }

    @Override
    public ResponseEntity parseData(ResponseEntity entity) throws JSONException {
        String resultJson = entity.getInfo();
        Gson gson = new Gson();
        entity.setData(gson.fromJson(resultJson, NewsID.class));
        return entity;
    }

    @Override
    public String getApiUrl() {
        return URLs.NEWS_DETAIL + nid;
    }
}

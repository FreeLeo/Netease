package com.xiongan.develop.news.transcation;

import com.google.gson.Gson;
import com.xiongan.develop.news.bean.UpgradeBean;
import com.xiongan.develop.news.config.URLs;
import com.xiongan.develop.news.volleyplus.BaseJsonTransaction;
import com.xiongan.develop.news.volleyplus.HttpCallback;
import com.xiongan.develop.news.volleyplus.ResponseEntity;

import org.json.JSONException;

/**
 * Created by admin on 2017/5/23.
 */

public class UpgradeTranscation extends BaseJsonTransaction{
    public UpgradeTranscation(String tag, HttpCallback callback) {
        super(callback,tag);
    }

    @Override
    public void prepareRequestOther() {
        setShouldCache(true);
    }

    @Override
    public ResponseEntity parseData(ResponseEntity entity) throws JSONException {
        String resultJson = entity.getInfo();
        Gson gson = new Gson();
        entity.setData(gson.fromJson(resultJson, UpgradeBean.class));
        return entity;
    }

    @Override
    public String getApiUrl() {
        return URLs.VERSION_UPGRADE;
    }
}

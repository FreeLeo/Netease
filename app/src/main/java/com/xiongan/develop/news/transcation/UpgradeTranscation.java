package com.xiongan.develop.news.transcation;

import com.google.gson.Gson;
import com.unbelievable.library.android.volleyplus.BaseJsonTransaction;
import com.unbelievable.library.android.volleyplus.HttpCallback;
import com.unbelievable.library.android.volleyplus.ResponseEntity;
import com.xiongan.develop.news.bean.UpgradeBean;
import com.xiongan.develop.news.config.URLs;

import org.json.JSONException;

/**
 * Created by admin on 2017/5/23.
 */

public class UpgradeTranscation extends BaseJsonTransaction {
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

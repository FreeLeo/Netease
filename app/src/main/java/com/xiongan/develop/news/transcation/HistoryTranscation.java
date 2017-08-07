package com.xiongan.develop.news.transcation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unbelievable.library.android.utils.DeviceUtil;
import com.xiongan.develop.news.MyApplication;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.config.URLs;
import com.xiongan.develop.news.volleyplus.BaseJsonTransaction;
import com.xiongan.develop.news.volleyplus.HttpCallback;
import com.xiongan.develop.news.volleyplus.ResponseEntity;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by admin on 2017/5/23.
 */

public class HistoryTranscation extends BaseJsonTransaction{
    private int page;
    public HistoryTranscation(int page, String tag, HttpCallback callback) {
        super(callback,tag);
        this.page = page;
    }

    @Override
    public void prepareRequestOther() {
        setShouldCache(false);
        setParam("start",page+"");
    }

    @Override
    public ResponseEntity parseData(ResponseEntity entity) throws JSONException {
        String resultJson = entity.getInfo();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<OneNewsItemBean>>(){}.getType();
        entity.setData(gson.fromJson(resultJson, listType));
        return entity;
    }

    @Override
    public String getApiUrl() {
        return URLs.NEWS_HISTORY + DeviceUtil.getDeviceId(MyApplication.getContext()) + "/xa-news-histories";
    }
}


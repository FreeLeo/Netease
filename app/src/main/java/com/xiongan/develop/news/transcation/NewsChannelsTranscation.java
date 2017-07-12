package com.xiongan.develop.news.transcation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiongan.develop.news.bean.NewsChannel;
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

public class NewsChannelsTranscation extends BaseJsonTransaction{

    public NewsChannelsTranscation(HttpCallback callback) {
        super(callback);
    }

    @Override
    public void prepareRequestOther() {
        setShouldCache(false);
    }

    @Override
    public ResponseEntity parseData(ResponseEntity entity) throws JSONException {
        String resultJson = entity.getInfo();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NewsChannel>>(){}.getType();
        entity.setData(gson.fromJson(resultJson, listType));
        return entity;
    }


    @Override
    public String getApiUrl() {
        return URLs.NEWS_CHANNELS;
    }
}

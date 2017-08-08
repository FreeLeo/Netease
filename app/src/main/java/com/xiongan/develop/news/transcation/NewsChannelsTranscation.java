package com.xiongan.develop.news.transcation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unbelievable.library.android.utils.PreferencesUtil;
import com.unbelievable.library.android.volleyplus.BaseJsonTransaction;
import com.unbelievable.library.android.volleyplus.HttpCallback;
import com.unbelievable.library.android.volleyplus.ResponseEntity;
import com.xiongan.develop.news.bean.NewsChannel;
import com.xiongan.develop.news.config.URLs;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by admin on 2017/5/23.
 */

public class NewsChannelsTranscation extends BaseJsonTransaction {

    public NewsChannelsTranscation(String tag,HttpCallback callback) {
        super(callback,tag);
    }

    @Override
    public void prepareRequestOther() {
        setShouldCache(false);
    }

    @Override
    public ResponseEntity parseData(ResponseEntity entity) throws JSONException {
        String resultJson = entity.getInfo();
        PreferencesUtil.put("NewsChannel",resultJson);
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

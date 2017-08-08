package com.xiongan.develop.news.transcation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unbelievable.library.android.volleyplus.BaseJsonTransaction;
import com.unbelievable.library.android.volleyplus.HttpCallback;
import com.unbelievable.library.android.volleyplus.ResponseEntity;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.config.URLs;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by admin on 2017/5/23.
 */

public class NewsListTranscation extends BaseJsonTransaction {
    private String tid;
    private int page;
    public NewsListTranscation(String tid,int page,String tag,HttpCallback callback) {
        super(callback,tag);
        this.tid = tid;
        this.page = page;
    }

    @Override
    public void prepareRequestOther() {
        setShouldCache(false);
        setParam("tid",tid);
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
        return URLs.NEWS_LIST;
    }
}


package com.xiongan.develop.news.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.newstext.Img;
import com.xiongan.develop.news.bean.newstext.NewRoot;
import com.xiongan.develop.news.bean.newstext.NewsID;
import com.xiongan.develop.news.config.Global;
import com.xiongan.develop.news.factory.RequestSingletonFactory;
import com.xiongan.develop.news.util.NeteaseURLParse;
import com.xiongan.develop.news.util.URLImageParser;
import com.xiongan.develop.news.vollley.MySingleton;
import com.xiongan.develop.news.widget.PicassoImageGetter;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsDetailActivity extends AppCompatActivity {
    private TextView newsContentTv;
    private final String template = "<p><img src='LINK'/></p>";
    private String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initView();
        setup();
    }

    private void setup() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            link = extras.getString("NEWS_LINK");
        }
        getNews(link);
    }

    private void initView() {
        newsContentTv = (TextView) findViewById(R.id.news_content_tv);
    }

    private void getNews(final String link) {
        MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(
                RequestSingletonFactory.getInstance().getGETStringRequest(this, link,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                JSONObject obj;
                                try {
                                    String id = NeteaseURLParse.getNewsID(link);
                                    String hold = response.toString().replace(id, "newsID");
                                    obj = new JSONObject(hold.toString());

                                    NewRoot newRoot = new Gson().fromJson(obj.toString(), Global.NewRoot);

                                    Log.i("RVA", "response: " + response.toString());
                                    Log.i("RVA", "newRoot: " + newRoot.toString());

                                    updateViewFromJSON(newRoot);

                                } catch (JSONException | JsonParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }));

    }

    private void updateViewFromJSON(NewRoot newRoot) {
        NewsID hold = newRoot.getNewsID();
        //设置标题

        //设置作者和时间
        int first = hold.getPtime().indexOf("-");
        int last = hold.getPtime().lastIndexOf(":");

        //设置正文
        String body = hold.getBody();
        for (Img img : hold.getImg()) {
            body = body.replace(img.getRef(), template.replace("LINK", img.getSrc()));
        }

        newsContentTv.setText(Html.fromHtml(body, new PicassoImageGetter(newsContentTv), null));
        newsContentTv.setTextSize(18);


    }
}

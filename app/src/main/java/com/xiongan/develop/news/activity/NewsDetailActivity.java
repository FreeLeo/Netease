package com.xiongan.develop.news.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.newstext.Img;
import com.xiongan.develop.news.bean.newstext.NewRoot;
import com.xiongan.develop.news.bean.newstext.NewsID;
import com.xiongan.develop.news.widget.PicassoImageGetter;

public class NewsDetailActivity extends BaseActivity {
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

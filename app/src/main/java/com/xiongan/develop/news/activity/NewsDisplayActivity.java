package com.xiongan.develop.news.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.newstext.Img;
import com.xiongan.develop.news.bean.newstext.NewRoot;
import com.xiongan.develop.news.bean.newstext.NewsID;
import com.xiongan.develop.news.config.Global;
import com.xiongan.develop.news.factory.RequestSingletonFactory;
import com.xiongan.develop.news.util.NeteaseURLParse;
import com.xiongan.develop.news.vollley.MySingleton;
import com.xiongan.develop.news.widget.PicassoImageGetter;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by HHX on 15/9/10.
 */
public class NewsDisplayActivity extends AppCompatActivity {
    private final String TAG = NewsDisplayActivity.class.getSimpleName();
    @BindView(R.id.vote_et)
    EditText voteEt;
    @BindView(R.id.vote_num_tv)
    TextView voteNumTv;
    @BindView(R.id.vote_send_tv)
    TextView voteSendTv;
    @BindView(R.id.content_view)
    LinearLayout contentView;
    private SystemBarTintManager tintManager;
    private Context context;
    private TextView content;
    private TextView title;
    private TextView authorAndTime;
    private String link;
    private final String template = "<p><img src='LINK'/></p>";
    private String voteStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_news_display);
        ButterKnife.bind(this);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(" ");
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }


        String html = "Hello \n" +
                "<img src='http://ww1.sinaimg.cn/mw600/4dc7b570jw1drn1o8mrp0j.jpg' />" +
                " This is a test \n" +
                "<img src='http://att.bbs.duowan.com/forum/201311/01/0950172al0qkazlh20hh9n.png'/>";

        String htmlTest = "<p>　　在众多安卓手机中，Nexus系列一贯被视为Google的“亲儿子”，但其实只有设计来自Google，代工生产还是交给其他厂商，包括LG、HTC、三星、华为、摩托罗拉等等。<\\/p>" +
                "<p>　　不过有传闻称，Google打算完全自己玩儿了，因为一则iPhone在高端市场上不断蚕食市场份额，二则Nexus现在本身的表现也越来越不好：销售渠道过于狭窄，缺乏运营商合作，新的Nexus 6P/5X定位太高影响销售……<\\/p>" +
                "<p>　　Google CEO Sundar Pichai已经向员工和一些外部人士透露，计划将Nexus系列完全掌控在自己手中，从设计到生产都一手负责，不再依赖其他手机厂商，就像Pixel C笔记本那样变成纯粹的Google产品。<\\/p>" +
                "<p>　　这样一来，Nexus设备也不会再冠以其他厂商的牌子，只打Google自己的标识。<\\/p>" +
                "<p>　　虽然Google没有透露该计划的具体细节和执行时间，但是据了解，HTC内部人士对于Google的这种做法并不意外，HTC也可能成为最后一个代工Nexus的第三方厂商。<\\/p>" +
                "<p>　　此前有消息称，HTC今年将独自代工两款Nexus手机，分别为5.0英寸、5.5英寸。<\\/p><!--IMG#0-->";

        String body = htmlTest.replace("<!--IMG#0-->", template.replace("LINK", "http://img1.cache.netease.com/catchpic/5/59/59F9EB30B047D22DAD5F12B14DB4682E.jpg"));


        content = (TextView) findViewById(R.id.tv_content);
        title = (TextView) findViewById(R.id.tv_newstitle);
        authorAndTime = (TextView) findViewById(R.id.tv_author_time);

//        URLImageParser p = new URLImageParser(content, this);
//        Spanned htmlSpan = Html.fromHtml(body, p, null);
//        content.setText(htmlSpan);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            link = extras.getString("NEWS_LINK");
        }
        getNews(link);
        setVoteView();
    }

    private void getNews(final String link) {
        MySingleton.getInstance(context.getApplicationContext()).getRequestQueue().add(
                RequestSingletonFactory.getInstance().getGETStringRequest(context, link,
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

    private void setVoteView() {
        voteEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.requestFocus();
                    showSoftKeyboard(v);
                }
                return false;
            }
        });
        voteEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    voteSendTv.setEnabled(false);
                    voteSendTv.setTextColor(getResources().getColor(R.color._999));
                } else {
                    voteSendTv.setEnabled(true);
                    voteSendTv.setTextColor(getResources().getColor(R.color.vote_send_enable));
                }
            }
        });
        voteEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG,"voteEt focus : "+hasFocus);
                if(hasFocus){
                    voteEt.setText(voteStr);
                    voteNumTv.setVisibility(View.GONE);
                    voteSendTv.setVisibility(View.VISIBLE);
                }else{
                    voteStr = voteEt.getText().toString();
                    voteEt.setText("");
                    voteNumTv.setVisibility(View.VISIBLE);
                    voteSendTv.setVisibility(View.GONE);
                }
            }
        });
    }


    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.tab_top_background));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateViewFromJSON(NewRoot newRoot) {
        NewsID hold = newRoot.getNewsID();
        //设置标题
        title.setText(hold.getTitle());

        //设置作者和时间
        int first = hold.getPtime().indexOf("-");
        int last = hold.getPtime().lastIndexOf(":");
        authorAndTime.setText(hold.getSource() + "    " + hold.getPtime().substring(first + 1, last));

        //设置正文
        String body = hold.getBody();
        for (Img img : hold.getImg()) {
            body = body.replace(img.getRef(), template.replace("LINK", img.getSrc()));
        }

        Log.i("RVA", "设置body： " + body);
//        URLImageParser p = new URLImageParser(content, this);
//        Spanned htmlSpan = Html.fromHtml(body, p, null);
//        content.setText(htmlSpan);
        content.setText(Html.fromHtml(body, new PicassoImageGetter(content), null));
        content.setTextSize(16);

        voteNumTv.setText(hold.getThreadVote()+"");
    }

    @OnClick({R.id.vote_num_tv, R.id.vote_send_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.vote_num_tv:
                break;
            case R.id.vote_send_tv:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //如果点击除EditText以外的其他VIew，键盘回收
            if (v instanceof EditText) {
                View nextFocus = findViewFocus(contentView, event);
                if (nextFocus != null && nextFocus instanceof EditText) {
                    return super.dispatchTouchEvent(event);
                }
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    hideSoftKeyboard(contentView);
                    contentView.requestFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void hideSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    private void showSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }

    private View findViewFocus(ViewGroup viewGroup, MotionEvent event) {
        View view = null;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            view = viewGroup.getChildAt(i);
            Rect outRect = new Rect();
            view.getGlobalVisibleRect(outRect);
            if (outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                if (view instanceof ViewGroup) {
                    return findViewFocus((ViewGroup) view, event);
                } else {
                    return view;
                }
            }
        }
        return null;
    }
}

package com.xiongan.develop.news.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.unbelievable.library.android.utils.ToastUtils;
import com.unbelievable.library.android.volleyplus.HttpCallback;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.newstext.Img;
import com.xiongan.develop.news.bean.newstext.NewsID;
import com.xiongan.develop.news.transcation.NewsDetailTranscation;
import com.xiongan.develop.news.util.Time2String;
import com.xiongan.develop.news.widget.PicassoImageGetter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiongan.develop.news.R.id.vote_num_tv;


/**
 * Created by HHX on 15/9/10.
 */
public class NewsDisplayActivity extends BaseActivity {
    private final String TAG = NewsDisplayActivity.class.getSimpleName();
    @BindView(R.id.vote_et)
    EditText voteEt;
    @BindView(vote_num_tv)
    TextView voteNumTv;
    @BindView(R.id.vote_send_tv)
    TextView voteSendTv;
    @BindView(R.id.content_view)
    LinearLayout contentView;
    @BindView(R.id.tv_vote)
    TextView voteTv;
    @BindView(R.id.tv_author)
    TextView authorTv;
    @BindView(R.id.tv_time)
    TextView timeTv;
    private SystemBarTintManager tintManager;
    private TextView content;
    private TextView title;
    private final String template = "<p><img src='LINK'/></p>";
    private String voteStr;
    private String nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_news_display);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(" ");
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }
        content = (TextView) findViewById(R.id.tv_content);
        title = (TextView) findViewById(R.id.tv_newstitle);

        if (getIntent() != null) {
            nid = getIntent().getStringExtra("nid");
        }
        getNews(nid);
        setVoteView();
    }

    private void getNews(final String nid) {
        createProgressDialog(getString(R.string.loading));
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                destroyProgressDialog();
                updateViewFromJSON((NewsID) data);
            }

            @Override
            public void onFailure(int code, String msg, Object data) {
                destroyProgressDialog();
                ToastUtils.toastS(NewsDisplayActivity.this,msg);
            }
        };
        new NewsDetailTranscation(nid,TAG, callback).excute();
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
                Log.e(TAG, "voteEt focus : " + hasFocus);
                if (hasFocus) {
                    voteEt.setText(voteStr);
                    voteNumTv.setVisibility(View.GONE);
                    voteSendTv.setVisibility(View.VISIBLE);
                } else {
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
            case R.id.action_refresh:
                getNews(nid);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateViewFromJSON(NewsID hold) {
        //设置标题
        title.setText(hold.getTitle());

        //设置作者和时间
        authorTv.setText(hold.getSource());
        timeTv.setText(Time2String.getStrTime(Long.valueOf(hold.getPtime()+"")));

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

        voteTv.setText(hold.readCount + getString(R.string.read_count));
        voteNumTv.setText("跟帖：" + hold.getThreadVote() + "");
    }

    @OnClick({vote_num_tv, R.id.vote_send_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case vote_num_tv:
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

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    private void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
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

    @OnClick(R.id.tv_vote)
    public void onViewClicked() {
//        Intent intent = new Intent(this, VoteActivity.class);
//        startActivity(intent);
    }
}

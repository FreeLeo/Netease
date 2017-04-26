package com.xiongan.develop.news.fragment;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shizhefei.fragment.LazyFragment;
import com.xiongan.develop.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import de.greenrobot.event.EventBus;

/**
 * Created by apple on 16/3/15.
 */
public class MeFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View login_layout, forget_layout;
    public SwipeRefreshLayout mRefreshLayout;
    //判断是否正在刷新
    public boolean isRefreshing = false;
    private ImageView iv_me_head_portrait;
    private ImageView me_setting, iv_register_return;
    private ImageView me_smallbell;
    private TextView tv_me_checkin;
    private TextView tv_general_name;
    private RelativeLayout rl_me_balance_record;
    private RelativeLayout rl_me_winning;
    private RelativeLayout rl_me_the_sun;
    private RelativeLayout rl_me_recharge_record;
    private RelativeLayout rl_me_feedback;
    private RelativeLayout rl_me_distribution;
    private RelativeLayout rl_me_expressive;
    private TextView tv_me_user_name;
    private TextView tv_me_money, forget_password, reg_condions;
    private LinearLayout ll_me_login;
    private ScrollView register_layout;

    private ImageView luck_bag;
    private int val = 0;
    private int val1 = 0;
    private EditText link_name_ed, email_ed, password_ed, password_again_ed, login_name_ed, login_password_ed, forget_email_ed;
    private Button reg_btn, reg_login_btn, login_btn, forget_submin_btn;
    private int visFlag = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.me_fragment);
        init();
    }

    public void init() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        mRefreshLayout.setOnRefreshListener(this);
        // 顶部刷新的样式
//        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.holo_light_blue), ContextCompat.getColor(this, R.color.holo_green), ContextCompat.getColor(this, R.color.holo_pink),
//                ContextCompat.getColor(this, R.color.holo_colored));
        mRefreshLayout.setRefreshing(false);
        iv_me_head_portrait = (ImageView) findViewById(R.id.iv_me_head_portrait);
        me_setting = (ImageView) findViewById(R.id.me_setting);
        me_smallbell = (ImageView) findViewById(R.id.me_smallbell);

        tv_me_checkin = (TextView) findViewById(R.id.tv_me_checkin);
        tv_general_name = (TextView) findViewById(R.id.tv_general_name);
        rl_me_balance_record = (RelativeLayout) findViewById(R.id.rl_me_balance_record);
        rl_me_winning = (RelativeLayout) findViewById(R.id.rl_me_winning);
        rl_me_the_sun = (RelativeLayout) findViewById(R.id.rl_me_the_sun);
        rl_me_recharge_record = (RelativeLayout) findViewById(R.id.rl_me_recharge_record);
        rl_me_expressive = (RelativeLayout) findViewById(R.id.rl_me_expressive);
        rl_me_distribution = (RelativeLayout) findViewById(R.id.rl_me_distribution);
        rl_me_feedback = (RelativeLayout) findViewById(R.id.rl_me_feedback);
        luck_bag = (ImageView) findViewById(R.id.luck_bag);

        tv_me_user_name = (TextView) findViewById(R.id.tv_me_user_name);
        tv_me_money = (TextView) findViewById(R.id.tv_me_money);
        ll_me_login = (LinearLayout) findViewById(R.id.ll_me_login);

        register_layout = (ScrollView) findViewById(R.id.register_layout);
        link_name_ed = (EditText) findViewById(R.id.link_name_ed);
        email_ed = (EditText) findViewById(R.id.email_ed);
        password_ed = (EditText) findViewById(R.id.password_ed);
        password_again_ed = (EditText) findViewById(R.id.password_again_ed);
        reg_condions = (TextView) findViewById(R.id.reg_condions);
        reg_btn = (Button) findViewById(R.id.reg_btn);
        reg_login_btn = (Button) findViewById(R.id.reg_login_btn);


        login_layout = findViewById(R.id.login_layout);
        iv_register_return = (ImageView) findViewById(R.id.iv_register_return);
        login_name_ed = (EditText) findViewById(R.id.login_name_ed);
        login_password_ed = (EditText) findViewById(R.id.login_password_ed);
        login_btn = (Button) findViewById(R.id.login_btn);
        forget_password = (TextView) findViewById(R.id.forget_password);

        forget_layout = findViewById(R.id.forget_layout);
        forget_email_ed = (EditText) findViewById(R.id.forget_email_ed);
        forget_submin_btn = (Button) findViewById(R.id.forget_submin_btn);


        findViewById(R.id.google_login).setOnClickListener(MeClickListener);
        findViewById(R.id.facebook_login).setOnClickListener(MeClickListener);
        findViewById(R.id.google_login1).setOnClickListener(MeClickListener);
        findViewById(R.id.facebook_login1).setOnClickListener(MeClickListener);

        tv_me_checkin.setOnClickListener(MeClickListener);
        iv_me_head_portrait.setOnClickListener(MeClickListener);
        rl_me_balance_record.setOnClickListener(MeClickListener);
        me_setting.setOnClickListener(MeClickListener);
        me_smallbell.setOnClickListener(MeClickListener);
        rl_me_winning.setOnClickListener(MeClickListener);
        rl_me_the_sun.setOnClickListener(MeClickListener);
        rl_me_recharge_record.setOnClickListener(MeClickListener);
        rl_me_expressive.setOnClickListener(MeClickListener);
        rl_me_distribution.setOnClickListener(MeClickListener);
        rl_me_feedback.setOnClickListener(MeClickListener);
        mRefreshLayout.setOnClickListener(MeClickListener);
        ll_me_login.setOnClickListener(MeClickListener);
        luck_bag.setOnClickListener(MeClickListener);

        forget_password.setOnClickListener(MeClickListener);
        reg_btn.setOnClickListener(MeClickListener);
        reg_login_btn.setOnClickListener(MeClickListener);
        iv_register_return.setOnClickListener(MeClickListener);
        login_btn.setOnClickListener(MeClickListener);
        forget_submin_btn.setOnClickListener(MeClickListener);
//        reg_condions.setOnClickListener(MeClickListener);
    }

    public View.OnClickListener MeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {}
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private ObjectAnimator startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder)
                .setDuration(duration);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
    }
}

package com.xiongan.develop.news.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;
import com.unbelievable.library.android.volleyplus.HttpCallback;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.UpgradeBean;
import com.xiongan.develop.news.dialog.CommonDialog;
import com.xiongan.develop.news.fragment.FirstLayerFragment;
import com.xiongan.develop.news.fragment.MeFragment;
import com.xiongan.develop.news.fragment.SecondLayerFragment;
import com.xiongan.develop.news.fragment.VideoFragment;
import com.xiongan.develop.news.transcation.UpgradeTranscation;

public class MainActivity extends BaseActivity {
    private IndicatorViewPager indicatorViewPager;
    private SystemBarTintManager tintManager;
    private Toolbar toolbar;

    private String[] tabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_main);

        SViewPager viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        // 禁止viewpager的滑动事件
        viewPager.setCanScroll(false);
        // 设置viewpager保留界面不重新加载的页面数量
        viewPager.setOffscreenPageLimit(4);

        tabTitles = getResources().getStringArray(R.array.tabs_titles);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.index_name);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();

        indicatorViewPager.setOnIndicatorPageChangeListener(new IndicatorViewPager.OnIndicatorPageChangeListener() {
            @Override
            public void onIndicatorPageChange(int i, int i1) {
                setToolbar(i1);
                toolbar.setTitle(tabTitles[i1]);
            }
        });
        getUpgradeInfo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = getResources().getStringArray(R.array.tabs);
        private int[] tabIcons = {R.drawable.maintab_1_selector, R.drawable.maintab_2_selector, R.drawable.maintab_5_selector};
        private LayoutInflater inflater;

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tab_main, container, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_content);
            textView.setText(tabNames[position]);

            ImageView image = (ImageView) convertView.findViewById(R.id.iv_tab_img);
            image.setImageResource(tabIcons[position]);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {

            Fragment mainFragment;
            if (position == 0) {
                mainFragment = new FirstLayerFragment();

                Bundle bundle = new Bundle();
                bundle.putString(FirstLayerFragment.INTENT_STRING_TABNAME, tabNames[position]);
                bundle.putInt(FirstLayerFragment.INTENT_INT_INDEX, position);
                mainFragment.setArguments(bundle);
            } else if(position == 1) {
                mainFragment = new SecondLayerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(SecondLayerFragment.INTENT_STRING_TABNAME, "要闻");
                bundle.putString(SecondLayerFragment.INTENT_STRING_TID, "6");
                mainFragment.setArguments(bundle);
            } else if(position == 2){
                mainFragment = new MeFragment();
            }else {
                mainFragment = new VideoFragment();
            }

            return mainFragment;
        }
    }

    private void setToolbar(int position){
        if(position == 3){
            toolbar.setVisibility(View.GONE);
        }else{
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    private void getUpgradeInfo(){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                CommonDialog.showUpgrade(MainActivity.this, (UpgradeBean) data);
            }

            @Override
            public void onFailure(int code, String msg, Object data) {
            }
        };
        new UpgradeTranscation(getVolleyTag(),callback).excute();
    }
}

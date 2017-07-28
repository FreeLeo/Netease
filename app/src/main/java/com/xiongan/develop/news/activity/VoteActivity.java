package com.xiongan.develop.news.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.xiongan.develop.news.R;
import com.xiongan.develop.news.adapter.VoteAdapter;
import com.xiongan.develop.news.bean.VoteBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoteActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private VoteAdapter mVoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        List<VoteBean> list = new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(new VoteBean());
        }
        mVoteAdapter = new VoteAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        recyclerView.setAdapter(mVoteAdapter);
    }

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh() {
        swiperefreshlayout.setRefreshing(false);
    }
}

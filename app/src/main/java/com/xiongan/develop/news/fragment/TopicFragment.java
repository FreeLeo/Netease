package com.xiongan.develop.news.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.shizhefei.fragment.LazyFragment;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.adapter.NormalRecyclerViewAdapter;
import com.xiongan.develop.news.adapter.TopicRecyclerViewAdapter;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.config.Global;
import com.xiongan.develop.news.config.URLs;
import com.xiongan.develop.news.factory.RequestSingletonFactory;
import com.xiongan.develop.news.vollley.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopicFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener{
    private ArrayList<OneNewsItemBean> mOneNewsItemList;
	private TopicRecyclerViewAdapter topicRecyclerViewAdapter;

	private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabmain_item);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview
		topicRecyclerViewAdapter = new TopicRecyclerViewAdapter(getActivity(), mOneNewsItemList, mRecyclerView);
        mRecyclerView.setAdapter(topicRecyclerViewAdapter);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
		mSwipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	protected void onResumeLazy() {
		super.onResumeLazy();
	}

	@Override
	protected void onDestroyViewLazy() {
		super.onDestroyViewLazy();
	}

	@Override
	public void onRefresh() {
		mSwipeRefreshLayout.setRefreshing(false);
	}
}

package com.xiongan.develop.news.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.adapter.NormalRecyclerViewAdapter;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.config.Global;
import com.xiongan.develop.news.config.URLs;
import com.xiongan.develop.news.factory.RequestSingletonFactory;
import com.xiongan.develop.news.vollley.MySingleton;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondLayerFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener{
	public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
	public static final String INTENT_INT_POSITION = "intent_int_position";
	private String tabName;
	private TextView textView;
    private ArrayList<OneNewsItemBean> mOneNewsItemList = new ArrayList<>();
	private NormalRecyclerViewAdapter normalRecyclerViewAdapter;

	private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tabName = getArguments().getString(INTENT_STRING_TABNAME);
		setContentView(R.layout.fragment_tabmain_item);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview
		normalRecyclerViewAdapter = new NormalRecyclerViewAdapter(getActivity(), mOneNewsItemList, mRecyclerView);
        mRecyclerView.setAdapter(normalRecyclerViewAdapter);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
		mSwipeRefreshLayout.setOnRefreshListener(this);
	}


	@Override
	protected void onResumeLazy() {
		super.onResumeLazy();
		getIndexNews();
	}

	@Override
	protected void onDestroyViewLazy() {
		super.onDestroyViewLazy();
	}


	private void getIndexNews() {
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().add(
                RequestSingletonFactory.getInstance().getGETStringRequest(getActivity(), URLs.getUrl(tabName), new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONObject obj;
                        try {
							mSwipeRefreshLayout.setRefreshing(false);
                            mOneNewsItemList.clear();
                            obj = new JSONObject(response.toString());
                            JSONArray itemArray = obj.getJSONArray(URLs.getUrlTag(tabName));
                            ArrayList<OneNewsItemBean> newsList = new Gson().fromJson(itemArray.toString(), Global.NewsItemType);
                            mOneNewsItemList.addAll(newsList);
							normalRecyclerViewAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }));
	}

	@Override
	public void onRefresh() {
		getIndexNews();
	}
}
package com.xiongan.develop.news.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shizhefei.fragment.LazyFragment;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.adapter.NormalRecyclerViewAdapter;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.transcation.NewsListTranscation;
import com.xiongan.develop.news.volleyplus.HttpCallback;

import java.util.ArrayList;

public class SecondLayerFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener{
	public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
	public static final String INTENT_INT_POSITION = "intent_int_position";
	public static final String INTENT_STRING_TID = "tid";
	private String tid;
	private String tabName;
	private TextView textView;
    private ArrayList<OneNewsItemBean> mOneNewsItemList = new ArrayList<>();
	private NormalRecyclerViewAdapter normalRecyclerViewAdapter;

	private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tid = getArguments().getString(INTENT_STRING_TID);
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
		HttpCallback callback = new HttpCallback() {
			@Override
			public void onSuccess(int code, String msg, Object data) {
				ArrayList<OneNewsItemBean> newsList = (ArrayList<OneNewsItemBean>) data;
				if(newsList != null) {
					mOneNewsItemList.clear();
					mOneNewsItemList.addAll(newsList);
				}
				normalRecyclerViewAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int code, String msg, Object data) {

			}
		};
		new NewsListTranscation(tid,callback).excute();
//        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().add(
//                RequestSingletonFactory.getInstance().getGETStringRequest(getActivity(), URLs.getUrl(tabName), new Response.Listener() {
//                    @Override
//                    public void onResponse(Object response) {
//                        JSONObject obj;
//                        try {
//							mSwipeRefreshLayout.setRefreshing(false);
//                            mOneNewsItemList.clear();
//                            obj = new JSONObject(response.toString());
//                            JSONArray itemArray = obj.getJSONArray(URLs.getUrlTag(tabName));
//                            ArrayList<OneNewsItemBean> newsList = new Gson().fromJson(itemArray.toString(), Global.NewsItemType);
//                            mOneNewsItemList.addAll(newsList);
//							normalRecyclerViewAdapter.notifyDataSetChanged();
//						} catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }));
	}

	@Override
	public void onRefresh() {
		getIndexNews();
	}
}

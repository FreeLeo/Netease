package com.xiongan.develop.news.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiongan.develop.news.R;
import com.xiongan.develop.news.adapter.NormalRecyclerViewAdapter;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.transcation.NewsListTranscation;
import com.xiongan.develop.news.volleyplus.HttpCallback;

import java.util.ArrayList;

public class SecondLayerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
	public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
	public static final String INTENT_STRING_TID = "tid";
	private String tid;
    private ArrayList<OneNewsItemBean> mOneNewsItemList = new ArrayList<>();
	private NormalRecyclerViewAdapter normalRecyclerViewAdapter;

	private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
	private ImageView emptyIv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tabmain_item, container, false);
		tid = getArguments().getString(INTENT_STRING_TID);
		emptyIv = (ImageView) view.findViewById(R.id.empty_iv);
		mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview

		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		getIndexNews();
		return view;
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
				if(normalRecyclerViewAdapter == null) {
                    if(mOneNewsItemList == null || mOneNewsItemList.size() == 0){
                        emptyIv.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }else {
                        emptyIv.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        normalRecyclerViewAdapter = new NormalRecyclerViewAdapter(getActivity(), mOneNewsItemList);
                        mRecyclerView.setAdapter(normalRecyclerViewAdapter);
                    }
				}else {
					normalRecyclerViewAdapter.notifyDataSetChanged();
				}
				mSwipeRefreshLayout.setRefreshing(false);
			}

			@Override
			public void onFailure(int code, String msg, Object data) {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		};
		new NewsListTranscation(tid,callback).excute();
	}

	@Override
	public void onRefresh() {
		getIndexNews();
	}
}

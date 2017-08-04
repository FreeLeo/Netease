package com.xiongan.develop.news.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.xiongan.develop.news.widget.SwipeRefreshView;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.adapter.NormalRecyclerViewAdapter;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.transcation.NewsListTranscation;
import com.xiongan.develop.news.volleyplus.HttpCallback;

import java.util.ArrayList;

public class SecondLayerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,SwipeRefreshView.OnLoadMoreListener{
	public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
	public static final String INTENT_STRING_TID = "tid";
	private String tid;
    private int page = 0;
    private ArrayList<OneNewsItemBean> mOneNewsItemList = new ArrayList<>();
    private SmartRecyclerAdapter mSmartRecyclerAdapter;

	private SwipeRefreshView mSwipeRefreshLayout;
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

		mSwipeRefreshLayout = (SwipeRefreshView) view.findViewById(R.id.swiperefreshlayout);
		mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setOnLoadMoreListener(this);
		mSwipeRefreshLayout.measure(0, 0);
		mSwipeRefreshLayout.setRefreshing(true);
		getIndexNews(0);
		return view;
	}

	private void getIndexNews(final int page) {
		HttpCallback callback = new HttpCallback() {
			@Override
			public void onSuccess(int code, String msg, Object data) {
                SecondLayerFragment.this.page = page + 1;
				ArrayList<OneNewsItemBean> newsList = (ArrayList<OneNewsItemBean>) data;
                if(page == 0){
                    mOneNewsItemList.clear();
                }
                if(newsList != null) {
                    mOneNewsItemList.addAll(newsList);
                }else{
                    if(newsList.size() < 20){
                        mSwipeRefreshLayout.setCanLoadMore(false);
                    }
                }
				if(mSmartRecyclerAdapter == null) {
                    if(mOneNewsItemList == null || mOneNewsItemList.size() == 0){
                        emptyIv.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }else {
                        emptyIv.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        NormalRecyclerViewAdapter normalRecyclerViewAdapter = new NormalRecyclerViewAdapter(getActivity(), mOneNewsItemList);
                        mSmartRecyclerAdapter = new SmartRecyclerAdapter(normalRecyclerViewAdapter);
                        mRecyclerView.setAdapter(mSmartRecyclerAdapter);
                        mSwipeRefreshLayout.init(20,5,true);
                    }
				}else {
                    mSmartRecyclerAdapter.notifyDataSetChanged();
				}
				mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setLoading(false);
			}

			@Override
			public void onFailure(int code, String msg, Object data) {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		};
		new NewsListTranscation(tid,page,TAG,callback).excute();
	}

	@Override
	public void onRefresh() {
		getIndexNews(0);
	}

    @Override
    public void onLoadMore() {
        getIndexNews(page);
    }
}

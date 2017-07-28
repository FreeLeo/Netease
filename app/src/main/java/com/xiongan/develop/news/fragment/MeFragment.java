package com.xiongan.develop.news.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.NewsChannel;
import com.xiongan.develop.news.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends BaseFragment {
	private IndicatorViewPager indicatorViewPager;
	private LayoutInflater inflate;
    private List<NewsChannel> channelList;
	private final int textPadding = 20;//dp
	private final int barWidth = 42;//dp

	public static final String INTENT_STRING_TABNAME = "intent_String_tabname";
	public static final String INTENT_INT_INDEX = "intent_int_index";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tabmain, container, false);
		Resources res = getResources();

		ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_tabmain_viewPager);
		ScrollIndicatorView indicator = (ScrollIndicatorView) view.findViewById(R.id.fragment_tabmain_indicator);
		ColorBar colorBar = new ColorBar(getContext(), res.getColor(R.color.tab_top_text_2), 5);
		colorBar.setWidth(ScreenUtil.dp2px(getActivity(), barWidth));
		indicator.setScrollBar(colorBar);

		float unSelectSize = 14;
		float selectSize = unSelectSize * 1f;//keep same

		int selectColor = res.getColor(R.color.tab_top_text_2);
		int unSelectColor = res.getColor(R.color.tab_top_text_1);
		indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));

		viewPager.setOffscreenPageLimit(4);

		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		inflate = LayoutInflater.from(getContext());

		setChannelsView();
		return view;
	}

    private void setChannelsView(){
		if(channelList != null){
			channelList.clear();
		}else{
            channelList = new ArrayList<>();
        }
		NewsChannel recommendChannel = new NewsChannel();
		recommendChannel.tid = "4";
		recommendChannel.name = "推荐";
		channelList.add(recommendChannel);
		NewsChannel historyChannel = new NewsChannel();
		historyChannel.tid = "6";
		historyChannel.name = "历史";
		channelList.add(historyChannel);
		indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager(),channelList));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("cccc", "Fragment 所在的Activity onDestroy " + this);
	}

	private class MyAdapter extends IndicatorFragmentPagerAdapter {
		private List<NewsChannel> channelList;

		public MyAdapter(FragmentManager fragmentManager,List<NewsChannel> channels) {
			super(fragmentManager);
			this.channelList = channels;
		}

		@Override
		public int getCount() {
			return channelList.size();
		}

		@Override
		public View getViewForTab(int position, View convertView, ViewGroup container) {
			if (convertView == null) {
				convertView = inflate.inflate(R.layout.tab_top, container, false);
			}
			TextView textView = (TextView) convertView;
			textView.setText(channelList.get(position).name);
			textView.setPadding(ScreenUtil.dp2px(getActivity(), textPadding), 0, ScreenUtil.dp2px(getActivity(), textPadding), 0);
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {

			SecondLayerFragment mainFragment = new SecondLayerFragment();
			Bundle bundle = new Bundle();
			bundle.putString(SecondLayerFragment.INTENT_STRING_TABNAME, channelList.get(position).name);
			bundle.putString(SecondLayerFragment.INTENT_STRING_TID, channelList.get(position).tid);
			mainFragment.setArguments(bundle);
			return mainFragment;
		}
	}

}

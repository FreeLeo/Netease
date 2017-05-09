package com.xiongan.develop.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wx.goodview.GoodView;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.VoteBean;

import java.util.List;

/**
 * Created by admin on 2017/5/8.
 */

public class VoteAdapter extends HeroBaseAdapter<VoteBean> {
    private GoodView mGoodView;
    public VoteAdapter(Context context, List<VoteBean> beans) {
        super(context, beans);
        mGoodView = new GoodView(context);
    }

    protected void onBindDataToView(HeroCommonViewHolder holder, VoteBean bean, int position) {
        ImageView goodIv = holder.getView(R.id.good);
        goodIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                good(v);
            }
        });
    }

    @Override
    public int getItemLayoutID(int viewType) {
        return R.layout.item_vote;
    }

    private void good(View view) {
        ((ImageView) view).setImageResource(R.mipmap.good_checked);
        mGoodView.setText("+1");
        mGoodView.show(view);
    }
}

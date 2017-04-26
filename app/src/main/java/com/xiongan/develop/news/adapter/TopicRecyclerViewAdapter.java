package com.xiongan.develop.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.activity.ImageDisplayActivity;
import com.xiongan.develop.news.activity.NewsDisplayActivity;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.bean.imageextra.PhotoSet;
import com.xiongan.develop.news.config.Global;
import com.xiongan.develop.news.factory.RequestSingletonFactory;
import com.xiongan.develop.news.util.NeteaseURLParse;
import com.xiongan.develop.news.vollley.MySingleton;
import com.xiongan.develop.news.widget.MyRecyclerView;
import com.xiongan.develop.news.widget.SwitchImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HHX on 15/9/9.
 */
public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private RecyclerView recyclerView;
    private String[] mTitles;
    private ArrayList<OneNewsItemBean> listItem;
    int defaultImage = R.drawable.load_fail;
    int failImage = R.drawable.load_fail;
    private int[] defaultImages = new int[]{defaultImage};
    private PhotoSet photoSet;
    final String TAG = getClass().getSimpleName();

    public TopicRecyclerViewAdapter(Context context, ArrayList<OneNewsItemBean> listItem, RecyclerView recyclerView) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        this.recyclerView = recyclerView;
        this.listItem = listItem;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
       return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SingleImageViewHolder(mLayoutInflater.inflate(R.layout.item_topic_single_image,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listItem == null ? 10 : listItem.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class BannerViewHold extends RecyclerView.ViewHolder {
        SwitchImage mSwitchImage;

        public BannerViewHold(View itemView) {
            super(itemView);
            mSwitchImage = (SwitchImage) itemView.findViewById(R.id.si_banner_image);
        }
    }


    private void setNetworkImageView(NetworkImageView networkImageView, String url) {
        networkImageView.setDefaultImageResId(defaultImage);
        networkImageView.setErrorImageResId(defaultImage);
        networkImageView.setImageUrl(url,
                MySingleton.getInstance(mContext.getApplicationContext()).getImageLoader());
    }

    public static class SingleImageViewHolder extends RecyclerView.ViewHolder {
        ImageView portraitIv;
        TextView desTv;
        ImageView desIv;
        TextView titleTv;
        TextView contentTv;
        TextView commentTv;
        public SingleImageViewHolder(View itemView) {
            super(itemView);
            portraitIv = (ImageView) itemView.findViewById(R.id.portrait_tv);
            desTv = (TextView) itemView.findViewById(R.id.des_tv);
            desIv = (ImageView) itemView.findViewById(R.id.des_iv);
            titleTv = (TextView) itemView.findViewById(R.id.title_tv);
            contentTv = (TextView) itemView.findViewById(R.id.content_tv);
            commentTv = (TextView) itemView.findViewById(R.id.comment_tv);
        }
    }
}
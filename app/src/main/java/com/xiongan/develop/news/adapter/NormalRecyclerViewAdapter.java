package com.xiongan.develop.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.activity.ImageDisplayActivity;
import com.xiongan.develop.news.activity.NewsDisplayActivity;
import com.xiongan.develop.news.bean.OneNewsItemBean;
import com.xiongan.develop.news.bean.imageextra.PhotoSet;
import com.xiongan.develop.news.bean.imageextra.photos;
import com.xiongan.develop.news.bean.imgextra;
import com.xiongan.develop.news.widget.MyRecyclerView;
import com.xiongan.develop.news.widget.SwitchImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HHX on 15/9/9.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<OneNewsItemBean> listItem;
    int defaultImage = R.drawable.load_fail;
    int failImage = R.drawable.load_fail;
    private int[] defaultImages = new int[]{defaultImage};
    ImageViewHolderListener mImageViewHolderListener = new ImageViewHolderListener();
    final String TAG = getClass().getSimpleName();

    public enum ITEM_TYPE {
        ITEM_TYPE_BANNER,
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_TEXT
    }

    public NormalRecyclerViewAdapter(Context context, ArrayList<OneNewsItemBean> listItem) {
        mContext = context;
        this.listItem = listItem;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (listItem.get(position).getOrder() == 1)
            return ITEM_TYPE.ITEM_TYPE_BANNER.ordinal();
        else if (listItem.get(position).getImgextra() != null && listItem.get(position).getImgextra().size() > 1 && listItem.get(position).getSkipType().equals("photoset"))
            return ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal();
        else
            return ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()) {
            View hold = mLayoutInflater.inflate(R.layout.item_image, parent, false);
            hold.setOnClickListener(mImageViewHolderListener);
            return new ImageViewHolder(hold);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_TEXT.ordinal()) {
            View hold = mLayoutInflater.inflate(R.layout.item_text, parent, false);
            return new TextViewHolder(hold);
        } else {
            return new BannerViewHold(mLayoutInflater.inflate(R.layout.item_banner, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            setTextViewHolder(holder,position);
        } else if (holder instanceof ImageViewHolder) {
            setImageViewHolder(holder,position);
        } else if (holder instanceof BannerViewHold) {
            setBannerViewHolder(holder,position);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof TextViewHolder) {
            if (((TextViewHolder)holder).mImageView.getController() != null) {
                ((TextViewHolder)holder).mImageView.getController().onDetach();
            }
            if (((TextViewHolder)holder).mImageView.getTopLevelDrawable() != null) {
                ((TextViewHolder)holder).mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        } else if (holder instanceof ImageViewHolder) {
        } else if (holder instanceof BannerViewHold) {
        }
    }

    private void setTextViewHolder(final RecyclerView.ViewHolder holder, int position){
        OneNewsItemBean bean = listItem.get(position);
        if (listItem.size() - 1 >= position) {
            ((TextViewHolder) holder).mTitle.setText(listItem.get(position).getTitle());
            ((TextViewHolder) holder).mSubTitle.setText(listItem.get(position).getSource());
            ((TextViewHolder) holder).mVote.setText(listItem.get(position).votecount + "跟帖");
            SimpleDraweeView draweeView = (SimpleDraweeView) ((TextViewHolder) holder).mImageView;
            if(bean.getImgextra() != null && bean.getImgextra().size() > 0) {
                Uri uri = Uri.parse(bean.getImgextra().get(0).getImgsrc());
                draweeView.setImageURI(uri);
            }else{
                draweeView.setImageResource(R.drawable.load_fail);
            }

            ((TextViewHolder) holder).v.setOnClickListener(new TextViewHolderListener(position));
        }
    }

    private void setImageViewHolder(RecyclerView.ViewHolder holder, int position){
        OneNewsItemBean bean = listItem.get(position);
        ((ImageViewHolder) holder).mTextView.setText(listItem.get(position).getTitle());
        ((ImageViewHolder) holder).mVote.setText(listItem.get(position).votecount + "跟帖");

        MyRecyclerView hold = ((ImageViewHolder) holder).mRecyclerView;
        //设置水平适配器
        Log.i(TAG, "设置水平适配器");
        hold.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        List<photos> photos = new ArrayList<>();
        if(bean.getImgextra() != null){
            for(imgextra item:bean.getImgextra()){
                photos p = new photos();
                p.setImgurl(item.getImgsrc());
                photos.add(p);
            }
        }
        PhotoSet photoSet = new PhotoSet();
        photoSet.setPhotos(photos);
        HorizontalImageRecyclerViewAdapter horizontalImageRecyclerViewAdapter = new HorizontalImageRecyclerViewAdapter(mContext, photoSet, hold);
        hold.setAdapter(horizontalImageRecyclerViewAdapter);
    }

    private void setBannerViewHolder(RecyclerView.ViewHolder holder, int position){
        OneNewsItemBean bean = listItem.get(position);
        //无数据时显示默认值
        if (listItem.size() == 0) {
            String[] urlsStrings = new String[]{"0"};
            ((BannerViewHold) holder).mSwitchImage.initPager(defaultImages, urlsStrings, urlsStrings);
        }
        //正常加载数据
        else {
            if (listItem.size() >= 1 && listItem.get(0).getOrder() == 1) {
                String[] urlsStrings = null;
                int[] defaultImages2 = null;
                String[] textsStrings = null;
                if(bean.getImgextra() != null && bean.getImgextra().size() > 0) {
                    urlsStrings = new String[bean.getImgextra().size()];
                    defaultImages2 = new int[]{defaultImage};
                    textsStrings = new String[bean.getImgextra().size()];
                    for(int i=0;i<bean.getImgextra().size();i++){
                        urlsStrings[i] = bean.getImgextra().get(i).getImgsrc();
                        textsStrings[i] = bean.getTitle();
                    }
                }
                ((BannerViewHold) holder).mSwitchImage.initPager(defaultImages2, urlsStrings, textsStrings);
//                    设置是否滚动
//                  ((BannerViewHold) holder).mSwitchImage.setMove(true, 4000);
                //从网络加载数据
                ((BannerViewHold) holder).mSwitchImage.setAndLoadImage(new SwitchImage.DisplayImageView() {
                    @Override
                    public void displayImageFromURL(SimpleDraweeView view, String url) {
                        view.setImageURI(Uri.parse(url));
                    }
                });
                ((BannerViewHold) holder).mSwitchImage.setOnClickListener(new SwitchImage.SwitchImageOnClick() {
                    @Override
                    public void viewClickedListener(int position) {

                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return listItem == null ? 0 : listItem.size();
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

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        MyRecyclerView mRecyclerView;
        TextView mVote;//跟帖

        ImageViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_title);
            mVote = (TextView) view.findViewById(R.id.tv_vote);
            mRecyclerView = (MyRecyclerView) view.findViewById(R.id.rv_subrecycleview);
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mImageView;
        TextView mTitle;
        TextView mSubTitle;
        TextView mVote;//跟帖
        View v;
        TextViewHolder(View view) {
            super(view);
            v = view;
            mImageView = (SimpleDraweeView) view.findViewById(R.id.iv_left_image);
            mTitle = (TextView) view.findViewById(R.id.list_item_news_title);
            mSubTitle = (TextView) view.findViewById(R.id.list_item_news_subtitle);
            mVote = (TextView) view.findViewById(R.id.list_item_vote);
        }
    }


    class TextViewHolderListener implements View.OnClickListener {
        int position;
        TextViewHolderListener(int i) {
            position = i;
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(mContext, NewsDisplayActivity.class);
            i.putExtra("nid", listItem.get(position).nid);
            mContext.startActivity(i);
        }
    }

    class ImageViewHolderListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(mContext, ImageDisplayActivity.class);
            mContext.startActivity(i);
        }
    }

}

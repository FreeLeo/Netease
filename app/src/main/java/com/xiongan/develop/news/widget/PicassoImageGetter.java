package com.xiongan.develop.news.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiongan.develop.news.MyApplication;
import com.xiongan.develop.news.R;

/**
 * Created by admin on 2017/4/21.
 */

public class PicassoImageGetter implements Html.ImageGetter {
    private TextView textView = null;

    public PicassoImageGetter(TextView target) {
        textView = target;
    }

    @Override
    public Drawable getDrawable(final String source) {
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();

        Picasso.with(MyApplication.getContext())
                .load(source)
                .placeholder(R.drawable.load_fail)
                .error(R.drawable.load_fail)
                .into(drawable);

        return drawable;
    }

    class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int imageWight = MyApplication.width;
            int imageHeight = drawable.getIntrinsicHeight() * MyApplication.width / drawable.getIntrinsicWidth();
            drawable.setBounds(0, 0, imageWight, imageHeight);
            setBounds(0, 0, imageWight, imageHeight);
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(MyApplication.getContext().getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            setDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            setDrawable(placeHolderDrawable);
        }
    }
}
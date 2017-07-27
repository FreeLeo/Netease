
package com.xiongan.develop.news.volleyplus;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;


/**
 * TODO<Volley图片缓存类，三级缓存：内存，本地，网络> 
 * @version:  V1.0 
 */

public class BitmapCache implements ImageLoader.ImageCache {
	
	/**
	 * 单例模式
	 */
	private static class ImageCacheHolder{
		static final BitmapCache BITMAP_CACHE = new BitmapCache();
	}
	/**
	 * 对外获取BitmapCache对象的接口
	 * 
	 * @return BitmapCache对象
	 */
	public static BitmapCache getBitmapCache() {
		return ImageCacheHolder.BITMAP_CACHE;
	}
	
	private BitmapCache() {
		imageFileCache = new ImageFileCache();
		imageMemoryCache = new ImageMemoryCache();
	}
	
	private ImageMemoryCache imageMemoryCache;
	private ImageFileCache imageFileCache;
	
	@Override
	public Bitmap getBitmap(String url) {
		int width = getWidthFromUrl(url);
        int height = getHeightFromUrl(url);
		Bitmap bitmap = imageMemoryCache.getBitmapFromCache(url);
		if (bitmap == null) {
			bitmap = imageFileCache.getBitmapFromCache(url, width, height);
			if (bitmap != null) {
				imageMemoryCache.addBitmapToCache(url, bitmap);
				return bitmap;
			}
		}
		return bitmap;
	}
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		imageMemoryCache.addBitmapToCache(url, bitmap);
		imageFileCache.saveBitmap(bitmap, url);
	}

	private int getWidthFromUrl(String url) {
        int indexWidth = url.lastIndexOf("#W");
        if (indexWidth == -1) {
            return 0;
        }
        int indexHeight = url.lastIndexOf("#H");
        String widthStr = url.substring(indexWidth + 2, indexHeight);
        try {
            return Integer.parseInt(widthStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getHeightFromUrl(String url) {
        int indexHeight = url.lastIndexOf("#H");
        if(indexHeight == -1) {
            return 0;
        }
        String substring = url.substring(indexHeight + 2);
		int urlStart = substring.indexOf("http");
		String heightStr = substring.substring(0, urlStart);
        try {
            return Integer.parseInt(heightStr);
        } catch (NumberFormatException e) {
            //ignore
        }
        return 0;
    }

}

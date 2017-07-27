package com.xiongan.develop.news.volleyplus;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/***
 * 内存 缓存
 * @author Administrator
 *
 */
public class ImageMemoryCache {
	
    /**
     * 从内存读取数据速度是最快的，硬引用缓存不会轻易被回收，用来保存常用数据
     */
	private static final String TAG = "------ImageMemoryCache---------";
    private LruCache<String, Bitmap> mLruCache;  //硬引用缓存
                                                                                          
    public ImageMemoryCache() {
    	final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 8;  //硬引用缓存容量，为系统可用内存的1/8
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (value != null) 
                	return value.getRowBytes() * value.getHeight();
                else
                    return 0;
            }
        };
    }
    /**
	 * 从LruCache缓存获取图片
	 */
    public Bitmap getBitmapFromCache(String url) {
    	synchronized (mLruCache) {
    		return mLruCache.get(url);
    	}
    }
    /**
	 * 将图片存储到LruCache
	 */
    public void addBitmapToCache(String url, Bitmap bitmap) {
    	synchronized (mLruCache) {
    		if (getBitmapFromCache(url) == null) {
    			mLruCache.put(url, bitmap);
    		}
    	}
    }
    
}
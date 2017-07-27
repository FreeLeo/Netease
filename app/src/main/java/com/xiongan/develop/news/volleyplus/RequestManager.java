
package com.xiongan.develop.news.volleyplus;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.xiongan.develop.news.MyApplication;


/**
 * TODO<Volley请求与图片加载单例类>
 */

public class RequestManager {
	
	private static RequestManager requestManager;
	private RequestQueue mRequestQueue ;
	private ImageLoader mImageLoader;

    public RequestManager(Context context) {
    	mRequestQueue = getRequestQueue(context);
    	mImageLoader = new ImageLoader(mRequestQueue, BitmapCache.getBitmapCache());
    }
    /**
     * 获取单例实例
     * @return
     */
    public static synchronized RequestManager getInstance() {
    	if (requestManager == null)
    		requestManager = new RequestManager(MyApplication.getContext());
    	return requestManager;
    }
    /**
     * 初始化 
     * @param context
     */
    public static void init(Context context) {
    	if (requestManager == null)
    		requestManager = new RequestManager(MyApplication.getContext());
    }
    /**
     * 获取请求队列
     * @return
     */
    public RequestQueue getRequestQueue(Context context){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
    /**
     * 添加Volley请求
     * @param request
     * @param tag
     */
    public void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        getRequestQueue().add(request);
    }
    /**
     * 根据Tag删除未完成的请求
     * @param tag
     */
    public void cancelAll(Object tag) {
    	getRequestQueue().cancelAll(tag);
    }
    
    public ImageLoader getImageLoader() {
    	return mImageLoader;
    }
}

package com.xiongan.develop.news.volleyplus;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * JSON对象请求基类
 */
public abstract class BaseGsonArrayTransaction<T> extends BaseTranscation{
	private GsonRequest gsonRequest = null;
	private final Class<T> clazz;

	public BaseGsonArrayTransaction(Class<T> clazz, HttpCallback callback) {
		super();
		this.clazz = clazz;
		this.callback = callback;
		TAG = getClass().getName();
		mQueue = VolleyPlus.getRequestQueue();
	}

	public String getUrl() {
		if(gsonRequest != null){
			return gsonRequest.getUrl();
		}
		return null;
	}
	
	/**
	 * 执行请求动作
	 */
	public Request<JSONObject> process(int method) {
		prepareRequest();
		if(method == Method.GET){
			url = getUrlWithQueryString(url);
		}		
		try {
			gsonRequest = new GsonRequest(url,clazz, params, responseListener, errorListener);
			gsonRequest.setShouldCache(shouldCache);
			if(Method.POST == method){
				gsonRequest.setShouldCache(false);
			}
			mQueue.add(gsonRequest);
			Log.d(TAG, gsonRequest.toString());
		} catch (Exception e) {
			e.printStackTrace();			
			if (callback != null) {
				callback.onFailure(HttpErrorUtil.ERROR_PREPARE_REQUEST_EXCEPTION, e.getMessage(), null);
			}
		}
		return gsonRequest;
	}

	/**
	 * 请求结果回调通知
	 */
	private  Response.Listener<Class<T>> responseListener =   new Response.Listener<Class<T>>()  {

		@Override
		public void onResponse(Class<T> response) {
			Log.d(TAG, response.toString());
			ResponseEntity entity = parseData(response);
			if (entity != null && null != callback){
				int code = entity.getCode();
				if (code ==  HttpErrorUtil.SUCC){
					callback.onSuccess(entity.getCode(), entity.getMsg(), entity.getData());	
				}else{
					callback.onFailure(entity.getCode(), entity.getMsg(), entity.getData());
				}	
			}
		}
	};	
	
	/**
	 * 请求准备相关参数
	 */
	public void prepareRequest() {
		initParams();
		prepareRequestOther();
	}

	private void initParams() {
		params = new HashMap<String, String>();
	}

	protected void setParam(String key, String value) {
		if (params == null) {
			initParams();
		}
		params.put(key, value);
	}

	private String getSortParam(){
		SortedSet<String> allParams = new TreeSet<String>();
		Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
		while (it.hasNext() ) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			try {
				allParams.add(key + "=" + URLEncoder.encode(value, "utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		return GameUtil.getSig(allParams);
		return "";
	}
	
	protected void removeParam(String param) {
		params.remove(param);
	}

	public abstract void prepareRequestOther();	

	/**
	 * 解析JSON对象
	 * @param content
	 * @return
	 */
	public abstract ResponseEntity parseData(Class<T> content);
	
	public void get(){
		this.process(Method.GET);
	}
	public void post(){
		this.process(Method.POST);
	}

}

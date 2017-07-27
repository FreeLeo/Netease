package com.xiongan.develop.news.volleyplus;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;

import org.json.JSONException;
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
public abstract class BaseJsonTransaction extends BaseTranscation{
	private int method = Method.GET;
	public BaseJsonTransaction(HttpCallback callback) {
		super();
		this.callback = callback;
		TAG = getClass().getName();
		mQueue = VolleyPlus.getRequestQueue();
	}

	public BaseJsonTransaction(HttpCallback callback,int method) {
		this(callback);
		this.method = method;
	}
	
	private JSONObjectRequest jsonObjectRequest = null;
	
	public String getUrl() {
		if(jsonObjectRequest != null){			
			return jsonObjectRequest.getUrl();
		}
		return null;
	}
	
	/**
	 * 执行请求动作
	 */
	public Request<JSONObject> process(int method) {
		url = getApiUrl();
		prepareRequest();
		if(method == Method.GET){
			url = getUrlWithQueryString(url);
		}
		try {
			jsonObjectRequest = new JSONObjectRequest(method, url, params, responseListener, errorListener);			
			jsonObjectRequest.setShouldCache(shouldCache);
			if(Method.POST == method){
				jsonObjectRequest.setShouldCache(false);
			}
			mQueue.add(jsonObjectRequest);
			Log.d(TAG, jsonObjectRequest.toString());
		} catch (Exception e) {
			e.printStackTrace();			
			if (callback != null) {
				callback.onFailure(HttpErrorUtil.ERROR_PREPARE_REQUEST_EXCEPTION, e.getMessage(), null);
			}
		}
		return jsonObjectRequest; 
	}

	/**
	 * 请求结果回调通知
	 */
	private  Response.Listener<JSONObject> responseListener =   new Response.Listener<JSONObject>()  {

		@Override
		public void onResponse(JSONObject response) {
			Log.d(TAG, response.toString());
			ResponseEntity entity = new ResponseEntity();
			try {
				entity = parseBaseData(response);
				entity = parseData(entity);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				entity.setCode(HttpErrorUtil.ERROR_PARSE_DATA_EXCEPTION);
				entity.setMsg(HttpErrorUtil.getErrorMsg(HttpErrorUtil.ERROR_PARSE_DATA_EXCEPTION));
			}
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
	 * @return
	 */
	public abstract ResponseEntity parseData(ResponseEntity entity) throws JSONException;
	
	public void get(){
		this.process(Method.GET);
	}
	public void post(){
		this.process(Method.POST);
	}
	public void excute(){
		this.process(method);
	}

}

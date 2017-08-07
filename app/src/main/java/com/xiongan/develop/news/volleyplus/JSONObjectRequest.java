package com.xiongan.develop.news.volleyplus;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装JSON对象请求
 */
public class JSONObjectRequest extends Request<JSONObject> {

	private Listener<JSONObject> listener;
	private Map<String, String> params;

	public JSONObjectRequest(String url, Map<String, String> params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
	}

	public JSONObjectRequest(int method, String url, Map<String, String> params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
	}

	/**
	 * POST请求设置参数
	 */
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	};

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,"UTF-8");
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		listener.onResponse(response);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// 支持gzip压缩
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Charset", "UTF-8");
		// headers.put("Content-Type", "*/*"); 这句代码有问题
		headers.put("Accept-Encoding", "gzip,deflate");
		return headers;
	}

	@Override
	public void deliverError(VolleyError error) {
		if(error instanceof NoConnectionError)
		{
			Cache.Entry entry = this.getCacheEntry();
			if (entry != null)
			{
				Response<JSONObject> response = parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
				deliverResponse(response.result);
				return ;
			}
		}
		super.deliverError(error);
	}
}

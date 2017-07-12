package com.xiongan.develop.news.volleyplus;

import org.json.JSONObject;

public class ResponseEntity {

	
	private int code  = HttpErrorUtil.SUCC;
	
	private String msg;
	
	private Object data;

	private String info;

	private JSONObject resultJson;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public JSONObject getResultJson() {
		if(resultJson == null){
			resultJson = new JSONObject();
		}
		return resultJson;
	}

	public void setResultJson(JSONObject resultJson) {
		this.resultJson = resultJson;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}

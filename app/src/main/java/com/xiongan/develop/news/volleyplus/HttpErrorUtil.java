package com.xiongan.develop.news.volleyplus;

public class HttpErrorUtil {	
	// 成功
	public static final int SUCC = 200;	
	public static final int ERROR_404 = 404;	
	// 未知错误
	public static final int ERROR_UNKNOW = -1;
	// http请求发起失败
	public static final int ERROR_PREPARE_REQUEST_EXCEPTION = -2;
	// 数据解析错误
	public static final int ERROR_PARSE_DATA_EXCEPTION = -3;
	// 连接失败
	public static final int ERROR_CONN_ERROR = -4;
	// 网络未连接
	public static final int ERROR_NETWORK_ERROR = -5;	
	// 服务器出错
	public static final int ERROR_SERVER_ERROR = -6;
	// 根据错误码返回错误信息
	public static String getErrorMsg(int code){	
		if(code == SUCC){
			return "请求成功";
		}else if(code == ERROR_CONN_ERROR){
			return "连接服务器失败";
		}else if(code == ERROR_NETWORK_ERROR){
			return "网络未连接";
		}else if(code == ERROR_PARSE_DATA_EXCEPTION){
			return "数据解析错误";
		}else if(code == ERROR_404){
			return "数据未找到";
		}else if(code == ERROR_UNKNOW){
			return "ERROR_UNKNOW";
		}		
		return "请求失败";
	}
}

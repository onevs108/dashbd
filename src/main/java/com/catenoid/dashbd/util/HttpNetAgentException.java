package com.catenoid.dashbd.util;

/**
 * http 통신 에러 정의 <br>
 * error code <br>
 * HTTP_HAEDER_NOT_SUCCESS 	= 10000 <br>
 * HTTP_NET_ERROR	     	= 10001 <br>
 */
public class HttpNetAgentException extends Exception{

    public static final int HTTP_HAEDER_NOT_SUCCESS = 10000;
    public static final int HTTP_NET_ERROR	     	= 10001;

 	private int code;
	
	public HttpNetAgentException(int code, String msg){
		super(msg);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}

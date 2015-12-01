package com.catenoid.dashbd.util;

import org.springframework.http.HttpStatus;

public class MyException extends Exception {
	private HttpStatus errCode = HttpStatus.OK;
	private String errMsg;
	
	public MyException(HttpStatus errCd, String errMsg) {
		super(errMsg);
		this.errCode = errCd;
		this.errMsg = errMsg;
	}
	
	public HttpStatus getErrcode() {
		return errCode;
	}
}

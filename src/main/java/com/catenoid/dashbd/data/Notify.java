package com.catenoid.dashbd.data;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.Expose;

public class Notify {
	
	@Expose
	private String code;
	
	@Expose
	private String message;
	
	@Expose
	private NotifyParams params;
	
	public String getCode() {
		return code;
	}
	@XmlElement(name="code")
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}
	public NotifyParams getParams() {
		return params;
	}
	@XmlElement(name="params")
	public void setParams(NotifyParams params) {
		this.params = params;
	}
}

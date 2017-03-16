package com.catenoid.dashbd.dao.model;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.catenoid.dashbd.util.Utils;

public class Log {
	private int sysId;
	private String targetId;
	private String reqType;
	private String reqSubType;
	private String reqUrl;
	private String reqCode;
	private String reqMsg;
	private String insertAt;
	private Date createdAt;
	
	public int getSysId() {
		return sysId;
	}
	public void setSysId(int sysId) {
		this.sysId = sysId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getReqSubType() {
		return reqSubType;
	}
	public void setReqSubType(String reqSubType) {
		this.reqSubType = reqSubType;
	}
	public String getReqUrl() {
		return reqUrl;
	}
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	public String getReqCode() {
		return reqCode;
	}
	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}
	public String getReqMsg() {
		return reqMsg;
	}
	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}
	public String getInsertAt() {
		return insertAt;
	}
	public void setInsertAt(String insertAt) {
		this.insertAt = insertAt;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public JSONObject toJSONObject() {
    	JSONObject jsonResult = new JSONObject();
    	jsonResult.put("sysId", sysId);
    	jsonResult.put("targetId", targetId);
    	jsonResult.put("reqType", reqType);
    	jsonResult.put("reqSubType", reqSubType);
    	jsonResult.put("reqUrl", reqUrl);
    	jsonResult.put("reqCode", reqCode);
    	jsonResult.put("reqMsg", reqMsg);
    	jsonResult.put("insertAt", insertAt);
    	jsonResult.put("createdAt", createdAt);
    	
    	return jsonResult;
    }
}
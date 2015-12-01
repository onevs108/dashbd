package com.catenoid.dashbd.util;

import java.util.ArrayList;

public class ErrorCode {
	
	private int code;
	private String message;
	private String description;
	
	private static ArrayList<ErrorCodeRange> codes;
	
	static  {
		codes = new ArrayList<ErrorCodeRange>();
		codes.add(new ErrorCodeRange(100,100, "OK", "Successful transaction"));
		codes.add(new ErrorCodeRange(200,299, "General Error", "Agent key is not match."));
		codes.add(new ErrorCodeRange(300,399, "System Error", "System, Capacity, OAM, Configuration missed"));
		codes.add(new ErrorCodeRange(400,499, "Provision XML Error", "Invalid schema, Non-permitted attribute"));
		codes.add(new ErrorCodeRange(500,599, "MPD Error", "Invalid Element / Attribute"));
		codes.add(new ErrorCodeRange(600,699, "Service Error", "Service ID, FEC, Service Area ID, GBR, Content"));
		codes.add(new ErrorCodeRange(700,799, "Schedule Error", "Overlap issue, Schedule time, Duplicated, Undefined"));
		codes.add(new ErrorCodeRange(800,899, "Request time Error", "Session Start/Stop guard time, File Start guard time, Last session stop time..."));
		codes.add(new ErrorCodeRange(900,999, "Status notify", "Session management, Content, System"));
	}

	public int getLastCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		for(ErrorCodeRange e : codes) {
			if(e.checkMinMax(code)) {
				setMessage(e.getMessage());
				setDescription(e.getDescription());
				return;
			}
		}
	}

	public String getLastMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public String getLastDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	};	
	
}

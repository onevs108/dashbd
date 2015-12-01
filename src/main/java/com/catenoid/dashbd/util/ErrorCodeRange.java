package com.catenoid.dashbd.util;

public class ErrorCodeRange {
	
	private int min;
	private int max;
	private String message;
	private String description;
	
	public ErrorCodeRange(int _min, int _max, String  _message, String _description) {
		min = _min;
		max = _max;
		message = _message;
		description = _description;
	}
	
	public boolean checkMinMax(int errorCode) {
		if(errorCode >= min && errorCode <= max) {
			return true;
		}
		return false;
	}

	public String getMessage() {
		return message;
	}
	
	public String getDescription() {
		return description;
	}
}
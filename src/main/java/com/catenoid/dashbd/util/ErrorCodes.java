package com.catenoid.dashbd.util;

public enum ErrorCodes {
	DATA_DUPLICATION(7700, "Data duplication"),
	OVER_BANDWIDTH(7701, "Over bandwidth"),
	DEL_DATA_CONSTRAINT(7702, "Delete data is constraint"),	
    UNKNOWN_ERROR(9999, "Unknown error");

    private final int code;
    private final String msg;

    ErrorCodes(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

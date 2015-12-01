package com.catenoid.dashbd.dao.model;

public class StatusNotifyWithBLOBs extends StatusNotify {
    private String message;

    private String saillst;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getSaillst() {
        return saillst;
    }

    public void setSaillst(String saillst) {
        this.saillst = saillst == null ? null : saillst.trim();
    }
}
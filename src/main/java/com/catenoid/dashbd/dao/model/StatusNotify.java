package com.catenoid.dashbd.dao.model;

public class StatusNotify {
    private Integer id;

    private Integer transactionId;

    private String agentKey;

    private Integer code;

    private Integer serviceId;

    private Integer bmscId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getAgentKey() {
        return agentKey;
    }

    public void setAgentKey(String agentKey) {
        this.agentKey = agentKey == null ? null : agentKey.trim();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getBmscId() {
        return bmscId;
    }

    public void setBmscId(Integer bmscId) {
        this.bmscId = bmscId;
    }
}
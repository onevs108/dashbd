package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class ServiceAreaCount {
    private Integer bmscId;

    private String city;
    
    private Integer count;

    public Integer getBmscId() {
        return bmscId;
    }

    public void setBmscId(Integer bmscId) {
        this.bmscId = bmscId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class OperatorBmsc {
    private Integer operatorId;
    
    private String operatorName;

    private Integer bmscId;
    
    private String bmscName;
    
    private String bmscCircle;

    private Date createdAt;

    private Date updatedAt;

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getBmscId() {
        return bmscId;
    }

    public void setBmscId(Integer bmscId) {
        this.bmscId = bmscId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

	public String getBmscName() {
		return bmscName;
	}

	public void setBmscName(String bmscName) {
		this.bmscName = bmscName;
	}

	public String getBmscCircle() {
		return bmscCircle;
	}

	public void setBmscCircle(String bmsCircle) {
		this.bmscCircle = bmsCircle;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
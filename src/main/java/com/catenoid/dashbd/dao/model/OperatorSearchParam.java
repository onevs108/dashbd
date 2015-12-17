package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class OperatorSearchParam {
    private Integer operatorId;
    
    private String operatorName;

    private String description;
    
    private Integer bmscId;
    
    private String bmscName;
    
    private String bmscCircle;

    private Date createdAt;

    private Date updatedAt;
    
    private Integer page;
    
    private Integer perPage;

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
	
	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
    
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }
}
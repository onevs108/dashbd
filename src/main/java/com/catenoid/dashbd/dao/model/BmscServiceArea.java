package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class BmscServiceArea {
    private Integer bmscId;

    private String bmscName;
	
	private String bmscCircle;
	
	private Integer serviceAreaId;
	
	private Integer serviceAreaBandwidth;
	
	private String serviceAreaName;
	
	private String serviceAreaCity;

    private Date createdAt;

    private Date updatedAt;

    public Integer getBmscId() {
        return bmscId;
    }

    public void setBmscId(Integer bmscId) {
        this.bmscId = bmscId;
    }

    public Integer getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(Integer serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
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

	public void setBmscCircle(String bmscCircle) {
		this.bmscCircle = bmscCircle;
	}

	public String getServiceAreaName() {
		return serviceAreaName;
	}

	public void setServiceAreaName(String serviceAreaName) {
		this.serviceAreaName = serviceAreaName;
	}

	public String getServiceAreaCity() {
		return serviceAreaCity;
	}

	public void setServiceAreaCity(String serviceAreaCity) {
		this.serviceAreaCity = serviceAreaCity;
	}

	public Integer getServiceAreaBandwidth() {
		return serviceAreaBandwidth;
	}

	public void setServiceAreaBandwidth(Integer serviceAreaBandwidth) {
		this.serviceAreaBandwidth = serviceAreaBandwidth;
	}
}
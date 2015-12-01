package com.catenoid.dashbd.dao.model;

import java.math.BigDecimal;

public class BmscServiceAreaEnbAp {
	private Integer bmscId;
	
	private String bmscName;
	
	private String bmscCircle;
	
	private Integer serviceAreaId;
	
	private Integer serviceAreaBandwidth;
	
	private String serviceAreaName;
	
	private String serviceAreaCity;
	
	private Integer enbApId;
	
	private String enbApName;
	
	private BigDecimal longitude;

    private BigDecimal latitude;
    
    public Integer getBmscId() {
    	return bmscId;
    }
    
    public void setBmscId(Integer bmscId) {
    	this.bmscId = bmscId;
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
    
    public Integer getServiceAreaId() {
    	return serviceAreaId;
    }
    
    public void setServiceAreaId(Integer serviceAreaId) {
    	this.serviceAreaId = serviceAreaId;
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
    
    public Integer getEnbApId() {
    	return enbApId;
    }
    
    public void setEnbApId(Integer enbApId) {
    	this.enbApId = enbApId;
    }

	public String getEnbApName() {
		return enbApName;
	}

	public void setEnbApName(String enbApName) {
		this.enbApName = enbApName;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public Integer getServiceAreaBandwidth() {
		return serviceAreaBandwidth;
	}

	public void setServiceAreaBandwidth(Integer serviceAreaBandwidth) {
		this.serviceAreaBandwidth = serviceAreaBandwidth;
	}
}

package com.catenoid.dashbd.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class ServiceAreaEnbAp {
    private Integer serviceAreaId;
    
    private Integer serviceAreaBandwidth;

    private String serviceAreaName;
	
	private String serviceAreaCity;
	
	private Integer enbApId;
	
	private String enbApName;
	
	private BigDecimal longitude;

    private BigDecimal latitude;
    
    private String plmn;
    
    private String mbsfn;

    private Date createdAt;

    private Date updatedAt;

    public Integer getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(Integer serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }

    public Integer getEnbApId() {
        return enbApId;
    }

    public void setEnbApId(Integer enbApId) {
        this.enbApId = enbApId;
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

	public String getPlmn() {
		return plmn;
	}

	public void setPlmn(String plmn) {
		this.plmn = plmn;
	}

	public String getMbsfn() {
		return mbsfn;
	}

	public void setMbsfn(String mbsfn) {
		this.mbsfn = mbsfn;
	}
}
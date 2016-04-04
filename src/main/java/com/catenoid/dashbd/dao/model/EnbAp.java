package com.catenoid.dashbd.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class EnbAp {
    private Integer id;

    private String name;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String plmn;

    private String circle;

    private String circleName;

    private Integer clusterId;

    private String ipaddress;

    private String earfcn;

    private String mbsfn;

    private Integer mbmsServiceAreaId;

    private Date createdAt;

    private Date updatedAt;
    
    private String city;
    
    private String bandwidth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public String getPlmn() {
        return plmn;
    }

    public void setPlmn(String plmn) {
        this.plmn = plmn == null ? null : plmn.trim();
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle == null ? null : circle.trim();
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName == null ? null : circleName.trim();
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress == null ? null : ipaddress.trim();
    }

    public String getEarfcn() {
        return earfcn;
    }

    public void setEarfcn(String earfcn) {
        this.earfcn = earfcn == null ? null : earfcn.trim();
    }

    public String getMbsfn() {
        return mbsfn;
    }

    public void setMbsfn(String mbsfn) {
        this.mbsfn = mbsfn == null ? null : mbsfn.trim();
    }

    public Integer getMbmsServiceAreaId() {
        return mbmsServiceAreaId;
    }

    public void setMbmsServiceAreaId(Integer mbmsServiceAreaId) {
        this.mbmsServiceAreaId = mbmsServiceAreaId;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
    
}
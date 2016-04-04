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
    
    private String circle;

    private String circleName;

    private Integer clusterId;

    private String ipaddress;

    private String earfcn;
    
    private Integer mbmsServiceAreaId;

    private Date createdAt;

    private Date updatedAt;
    
    private Integer totalCount;
    
    private Long bandwidth;
    
    private String city;
    
    private Integer operatorId;
    
    private Integer bmscId;
    
    private String mapCity;

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
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
	public Long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Long bandwidth) {
        this.bandwidth = bandwidth;
    }
    
    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
    
    public Integer getMbmsServiceAreaId() {
        return mbmsServiceAreaId;
    }

    public void setMbmsServiceAreaId(Integer mbmsServiceAreaId) {
        this.mbmsServiceAreaId = mbmsServiceAreaId;
    }
    
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

	public String getMapCity() {
		return mapCity;
	}

	public void setMapCity(String mapCity) {
		this.mapCity = mapCity;
	}
    
}
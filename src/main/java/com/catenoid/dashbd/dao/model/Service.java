package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class Service {
    private Integer id;

    private Integer servicesId;

    private Integer serviceTypeId;
    
    private String serviceTypeName;

    private String serviceStrId;

    private String serviceClass;

    private Integer retrieveInterval;
    
    private Integer cancelled;
    
    private Integer qosGBR;

    private Integer qosQCI;

    private Integer arpLevel;

    private Integer arpPreEmptionCapability;

    private Integer arpPreEmptionVulnerability;

    private String fecType;

    private Integer fecRatio;

    private Integer segmentAvailableOffset;
    
    private Date createdAt;

    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServicesId() {
        return servicesId;
    }

    public void setServicesId(Integer servicesId) {
        this.servicesId = servicesId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceStrId() {
        return serviceStrId;
    }

    public void setServiceStrId(String serviceStrId) {
        this.serviceStrId = serviceStrId == null ? null : serviceStrId.trim();
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass == null ? null : serviceClass.trim();
    }

    public Integer getRetrieveInterval() {
        return retrieveInterval;
    }

    public void setRetrieveInterval(Integer retrieveInterval) {
        this.retrieveInterval = retrieveInterval;
    }

	public Integer getQosGBR() {
		return qosGBR;
	}

	public void setQosGBR(Integer qosGBR) {
		this.qosGBR = qosGBR;
	}

	public Integer getQosQCI() {
		return qosQCI;
	}

	public void setQosQCI(Integer qosQCI) {
		this.qosQCI = qosQCI;
	}

	public Integer getArpLevel() {
		return arpLevel;
	}

	public void setArpLevel(Integer arpLevel) {
		this.arpLevel = arpLevel;
	}

	public Integer getArpPreEmptionCapability() {
		return arpPreEmptionCapability;
	}

	public void setArpPreEmptionCapability(Integer arpPreEmptionCapability) {
		this.arpPreEmptionCapability = arpPreEmptionCapability;
	}

	public Integer getArpPreEmptionVulnerability() {
		return arpPreEmptionVulnerability;
	}

	public void setArpPreEmptionVulnerability(Integer arpPreEmptionVulnerability) {
		this.arpPreEmptionVulnerability = arpPreEmptionVulnerability;
	}

	public String getFecType() {
		return fecType;
	}

	public void setFecType(String fecType) {
		this.fecType = fecType;
	}

	public Integer getFecRatio() {
		return fecRatio;
	}

	public void setFecRatio(Integer fecRatio) {
		this.fecRatio = fecRatio;
	}

	public Integer getSegmentAvailableOffset() {
		return segmentAvailableOffset;
	}

	public void setSegmentAvailableOffset(Integer segmentAvailableOffset) {
		this.segmentAvailableOffset = segmentAvailableOffset;
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

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public Integer getCancelled() {
		return cancelled;
	}

	public void setCancelled(Integer cancelled) {
		this.cancelled = cancelled;
	}
}
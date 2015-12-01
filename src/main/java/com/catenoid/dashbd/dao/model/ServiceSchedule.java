package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class ServiceSchedule {
    private Integer serviceId;

    private Integer scheduleId;
    
    private Integer cancelled;
    
    private Date	startDate;
    
    private Date	endDate;
    
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getCancelled() {
		return cancelled;
	}

	public void setCancelled(Integer cancelled) {
		this.cancelled = cancelled;
	}	
}
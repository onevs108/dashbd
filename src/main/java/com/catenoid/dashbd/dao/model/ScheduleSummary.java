package com.catenoid.dashbd.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class ScheduleSummary {
    private Integer scheduleId;
    private Integer serviceAreaId;
    private Integer contentId;
    private Integer bcId;
    private String scheduleName;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private Date updatedAt;
    private String delYn;
    private String thumbnail;
    private BigDecimal progressRate;
    private Integer page;
    private Integer perPage;
    private Integer totalCount;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    public Integer getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(Integer serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }
    
    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }
    
    public Integer getBcId() {
        return bcId;
    }

    public void setBcId(Integer bcId) {
        this.bcId = bcId;
    }
    
    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName == null ? null : scheduleName.trim();
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

    public String getDelYn() {
        return delYn;
    }

    public void setDelYn(String delYn) {
        this.delYn = delYn == null ? null : delYn.trim();
    }
    
	public String getThumbnail() {
		return thumbnail;
	}
	
	public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }
	
	public BigDecimal getProgressRate() {
        return progressRate;
    }
	
	public void setProgressRate(BigDecimal progressRate) {
		this.progressRate = progressRate;
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
	
	public Integer getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
package com.catenoid.dashbd.dao.model;

import java.util.Date;

public class ScheduleContents {
	private Integer serviceAreaId;
	
    private Integer scheduleId;

    private Integer contentId;
    
    private Integer cancelled;
    
    private Integer changed;

    private Date startTime;
    
    private Date endTime;
    
    private Date fromTime;
    
    private Date toTime;
    
    private String contentType;
    
    private String title;
    
    private String category;

    private String director;

    private String actors;
    
    private String contentProvider;
    
    private String fileFormat;

    private Integer ageRestriction;

    private String url;
    
    private Integer duration;
    
    private Integer bitrate;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(String contentProvider) {
		this.contentProvider = contentProvider;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public Integer getAgeRestriction() {
		return ageRestriction;
	}

	public void setAgeRestriction(Integer ageRestriction) {
		this.ageRestriction = ageRestriction;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getCancelled() {
		return cancelled;
	}

	public void setCancelled(Integer cancelled) {
		this.cancelled = cancelled;
	}

	public Integer getChanged() {
		return changed;
	}

	public void setChanged(Integer changed) {
		this.changed = changed;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getServiceAreaId() {
		return serviceAreaId;
	}

	public void setServiceAreaId(Integer serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}
}
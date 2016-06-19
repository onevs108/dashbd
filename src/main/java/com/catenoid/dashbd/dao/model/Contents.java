package com.catenoid.dashbd.dao.model;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.catenoid.dashbd.util.Utils;

public class Contents {
    private Integer id;
    
    private String type;

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

    private Date createdAt;

    private Date updatedAt;

    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }
    
    public String getCategory() {
    	return category;
    }
    
    public void setCategory(String category) {
    	this.category = category == null ? null : category.trim();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director == null ? null : director.trim();
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors == null ? null : actors.trim();
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
        this.url = url == null ? null : url.trim();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
    @SuppressWarnings("unchecked")    
    public JSONObject toJSONObject() {
    	JSONObject jsonResult = new JSONObject();
    	jsonResult.put("id", id);
    	jsonResult.put("title", title);
    	jsonResult.put("category", category);
    	jsonResult.put("duration", duration);
    	jsonResult.put("fileFormat", fileFormat);
    	jsonResult.put("description", description);
    	
    	/*
    	JSONArray jsonArray = new JSONArray();
    	for (Permission perms : permissions)
    		jsonArray.add(perms.toJSONObject());
    	jsonResult.put("permissions", jsonArray);
    	*/
    	jsonResult.put("createdAt", Utils.getFormatDateTime(createdAt, "yyyy-MM-dd HH:mm:ss"));
    	jsonResult.put("updatedAt", Utils.getFormatDateTime(updatedAt, "yyyy-MM-dd HH:mm:ss"));
    	return jsonResult;
    }
}
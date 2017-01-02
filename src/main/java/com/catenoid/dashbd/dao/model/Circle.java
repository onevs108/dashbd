package com.catenoid.dashbd.dao.model;

import java.util.Date;

import org.json.simple.JSONObject;

import com.catenoid.dashbd.util.Utils;

public class Circle {	
    private Integer id;
    private String circle_name;
    private String town_name;
    private String town_code;
    private String latitude;
    private String longitude;
    private String description;
    private String permission;
    private Date createdAt;
    private Date updatedAt;
    private Integer totalCount;

    public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
    
    public String getCircle_name() {
		return circle_name;
	}

	public void setCircle_name(String circle_name) {
		this.circle_name = circle_name;
	}

	public String getTown_name() {
		return town_name;
	}

	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}

	public String getTown_code() {
		return town_code;
	}

	public void setTown_code(String town_code) {
		this.town_code = town_code;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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
    
    public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("id", id);
		jsonResult.put("circle_name", circle_name);
		jsonResult.put("town_name", town_name);
		jsonResult.put("town_code", town_code);
		jsonResult.put("latitude", latitude);
		jsonResult.put("longitude", longitude);
		jsonResult.put("description", description);
		jsonResult.put("createdAt", Utils.getFormatDateTime(createdAt, "yyyy-MM-dd HH:mm:ss"));
		jsonResult.put("updatedAt", Utils.getFormatDateTime(updatedAt, "yyyy-MM-dd HH:mm:ss"));
		return jsonResult;
	}

	@Override
	public String toString() {
		return "Operator [id=" + id + ", circle_name=" + circle_name + ", town_name=" + town_name
				+ ", town_code=" + town_code + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", description=" + description + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
package com.catenoid.dashbd.dao.model;

import java.util.Date;

import org.json.simple.JSONObject;

import com.catenoid.dashbd.util.Utils;

public class Circle {	
    private Integer circle_id;
    private String circle_name;
    private String city_name;
    private String city_code;
    private String latitude;
    private String longitude;
    private String description;
    private String permission;
    private String bandwidth;
    private Date createdAt;
    private Date updatedAt;
    private Integer totalCount;
    
    public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
    public Integer getCircle_id() {
		return circle_id;
	}

	public void setCircle_id(Integer circle_id) {
		this.circle_id = circle_id;
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
		jsonResult.put("circle_id", circle_id);
		jsonResult.put("circle_name", circle_name);
		jsonResult.put("city_name", city_name);
		jsonResult.put("city_code", city_code);
		jsonResult.put("latitude", latitude);
		jsonResult.put("longitude", longitude);
		jsonResult.put("description", description);
		jsonResult.put("permission", permission);
		jsonResult.put("bandwidth", bandwidth);
		jsonResult.put("createdAt", Utils.getFormatDateTime(createdAt, "yyyy-MM-dd HH:mm:ss"));
		jsonResult.put("updatedAt", Utils.getFormatDateTime(updatedAt, "yyyy-MM-dd HH:mm:ss"));
		return jsonResult;
	}

	@Override
	public String toString() {
		return "Operator [circle_id=" + circle_id + ", circle_name=" + circle_name + ", city_name=" + city_name
				+ ", city_code=" + city_code + ", latitude=" + latitude + ", longitude=" + longitude + ", longitude=" + longitude
				+ ", description=" + description + ", permission=" + permission + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
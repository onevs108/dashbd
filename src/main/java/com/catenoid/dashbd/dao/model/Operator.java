package com.catenoid.dashbd.dao.model;

import java.util.Date;

import org.json.simple.JSONObject;

import com.catenoid.dashbd.util.Utils;

public class Operator {
    private Integer id;

    private String name;
    
    private String description;
    
    private String permission;

    private Date createdAt;

    private Date updatedAt;
    
    private Integer totalCount;

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
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
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
		jsonResult.put("name", name);
		jsonResult.put("description", description);
		jsonResult.put("permission", permission);
		jsonResult.put("createdAt", Utils.getFormatDateTime(createdAt, "yyyy-MM-dd HH:mm:ss"));
		jsonResult.put("updatedAt", Utils.getFormatDateTime(updatedAt, "yyyy-MM-dd HH:mm:ss"));
		return jsonResult;
	}

	@Override
	public String toString() {
		return "Operator [id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}
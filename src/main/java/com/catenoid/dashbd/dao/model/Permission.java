package com.catenoid.dashbd.dao.model;

import java.util.Date;

import org.json.simple.JSONObject;

public class Permission {
	
	private Integer id;

	private String role;
	
    private String name;
    
    private String description;

    private Date createdAt;

    private Date updatedAt;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
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
    
    @SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
    	JSONObject jsonResult = new JSONObject();
    	jsonResult.put("id", id);
//    	jsonResult.put("role", role);
    	jsonResult.put("name", name);
    	jsonResult.put("description", description);
    	jsonResult.put("createdAt", createdAt);
    	jsonResult.put("updatedAt", updatedAt);
    	return jsonResult;
    }

}

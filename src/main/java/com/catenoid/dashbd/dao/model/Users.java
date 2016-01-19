package com.catenoid.dashbd.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.catenoid.dashbd.util.Utils;

public class Users {
	private int id;
	
    private String userId;

    private Integer operatorId;

    private String password;

    private String firstName;

    private String lastName;

    private String department;

    private Integer permission;
    
    private Integer grade;

    private Date createdAt;

    private Date updatedAt;
    
    private List<Permission> permissions = new ArrayList<Permission>(); 

    public Users() {}
    
    public Users(String userId, Integer operatorId, String password, String firstName, String lastName, String department, Integer grade) {
		this.userId = userId;
		this.operatorId = operatorId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
		this.grade = grade;
	}

	public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
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
    
    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    
    @SuppressWarnings("unchecked")    
    public JSONObject toJSONObject() {
    	JSONObject jsonResult = new JSONObject();
    	jsonResult.put("id", id);
    	jsonResult.put("password", password);
    	jsonResult.put("userId", userId);
    	jsonResult.put("operatorId", operatorId);
    	jsonResult.put("firstName", firstName);
    	jsonResult.put("lastName", lastName);
    	jsonResult.put("department", department);
    	jsonResult.put("grade", grade);
    	
    	JSONArray jsonArray = new JSONArray();
    	for (Permission perms : permissions)
    		jsonArray.add(perms.toJSONObject());
    	jsonResult.put("permissions", jsonArray);
    	
    	jsonResult.put("createdAt", Utils.getFormatDateTime(createdAt, "yyyy-MM-dd HH:mm:ss"));
    	jsonResult.put("updatedAt", Utils.getFormatDateTime(updatedAt, "yyyy-MM-dd HH:mm:ss"));
    	return jsonResult;
    }

    @Override
	public String toString() {
		return "Users [id=" + id
				+ ", userId=" + userId
				+ ", operatorId=" + operatorId
				+ ", password=" + password
				+ ", firstName=" + firstName
				+ ", lastName=" + lastName
				+ ", department=" + department
				+ ", grade=" + grade
				+ ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt
				+ ", permissionsSize=" + (permissions == null ? null : permissions.size())
				+ "]";
	}
}
package com.catenoid.dashbd.service;

import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.dao.model.Users;

@Service
public interface PermissionService {
	
	public List<Users> getUserList(String searchOperatorId, String searchUserId, long offset, long limit);
	public int getUserListCount(String searchOperatorId, String searchUserId);
	public JSONArray getUserListToJsonArray(String searchOperatorId, String searchUserId, long offset, long limit);
	
	public List<Permission> getPermissionList(String userId);
	public JSONArray getPermissionListToJsonArray(String userId);
	public boolean insertUserPermission(String userId, List<String> permissions);
	
}

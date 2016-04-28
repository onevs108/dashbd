package com.catenoid.dashbd.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.PermissionService;

@Service(value = "permissionServiceImpl")
public class PermissionServiceImpl implements PermissionService {

	private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * userList 리턴
	 */
	@Override
	public List<Users> getUserList(String searchOperatorId, String searchUserId, String sort, String order, long offset, long limit) {
		if (searchOperatorId == null || searchOperatorId.isEmpty()) searchOperatorId = null;
		if (searchUserId == null || searchUserId.isEmpty()) searchUserId = null;
		if (sort == null || sort.isEmpty()) sort = null;
		if (order == null || order.isEmpty()) order = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchOperatorId", searchOperatorId);
		map.put("searchUserId", searchUserId);
		map.put("sort", sort);
		map.put("order", order);
		map.put("start", offset+1);
		map.put("end", offset + limit);
		
		List<Users> userList = new ArrayList<Users>();
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			userList = usersMapper.selectUserListInPermission(map);
			
			for (Users user : userList){
				user.setPermissions(usersMapper.selectPermissionsByUserId(user.getUserId()));
			}

			syslogMap.put("reqType", "Permission Mgmt");
			syslogMap.put("reqSubType", "userlist");
			syslogMap.put("reqUrl", "permission/userlist.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (Exception e) {
			syslogMap.put("reqType", "Permission Mgmt");
			syslogMap.put("reqSubType", "userlist");
			syslogMap.put("reqUrl", "permission/userlist.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ [An error occurred]", e);
		}
		return userList;
	}
	
	/**
	 * userListCount 리턴
	 */
	@Override
	public int getUserListCount(String searchOperatorId, String searchUserId) {
		if (searchOperatorId == null || searchOperatorId.isEmpty()) searchOperatorId = null;
		if (searchUserId == null || searchUserId.isEmpty()) searchUserId = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchOperatorId", searchOperatorId);
		map.put("searchUserId", searchUserId);
		
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		return usersMapper.selectUserListCountInPermission(map);
	}

	/**
	 * userList를 JSONArray 형태로 리턴
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getUserListToJsonArray(String searchOperatorId, String searchUserId, String sort, String order, long offset, long limit) {
		List<Users> userList = getUserList(searchOperatorId, searchUserId, sort, order, offset, limit);
		
		JSONArray jsonArray = new JSONArray();
		for (Users user : userList)
			jsonArray.add(user.toJSONObject());
		
		return jsonArray;
	}

	/**
	 * 퍼미션 리스트 리턴
	 * 
	 * @param userId null 인 경우 모든 퍼미션 리턴
	 */
	@Override
	public List<Permission> getPermissionList(String userId) {
		if (userId == null || userId.isEmpty()) userId = null;
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		List<Permission> permissionList = new ArrayList<Permission>();
		try {
			if (userId == null)
				permissionList = usersMapper.selectPermissionAll();
			else
				permissionList = usersMapper.selectPermissionsByUserId(userId);

			syslogMap.put("reqType", "Permission Mgmt");
			syslogMap.put("reqSubType", "permission");
			syslogMap.put("reqUrl", "resources/permission.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (Exception e) {
			syslogMap.put("reqType", "Permission Mgmt");
			syslogMap.put("reqSubType", "permission");
			syslogMap.put("reqUrl", "resources/permission.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
		}
		
		return permissionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getPermissionListToJsonArray(String userId) {
		List<Permission> permissionList = getPermissionList(userId);
		
		JSONArray jsonArray = new JSONArray();
		for (Permission permission: permissionList)
			jsonArray.add(permission.toJSONObject());
		
		return jsonArray;
	}

	@Override
	public boolean insertUserPermission(String userId, List<String> permissions) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			usersMapper.deletePermissionOfUser(userId);
			
			if (permissions != null && !permissions.isEmpty()) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
				
				Map<String, Object> item = null;
				for (String permissionId : permissions) {
					item = new HashMap<String, Object>();
					item.put("userId", userId);
					item.put("permissionId", permissionId);
					itemList.add(item);
				}
				
				map.put("itemList", itemList);

				syslogMap.put("reqType", "Permission Mgmt");
				syslogMap.put("reqSubType", "insert");
				syslogMap.put("reqUrl", "permission/insert.do");
				syslogMap.put("reqCode", "SUCCESS");
				syslogMap.put("reqMsg", "");
				usersMapper.insertSystemAjaxLog(syslogMap);

				return usersMapper.insertPermissionOfUser(map) > 0;
			}
			
			return true;
		} catch (Exception e) {
			syslogMap.put("reqType", "Permission Mgmt");
			syslogMap.put("reqSubType", "insert");
			syslogMap.put("reqUrl", "permission/insert.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

}

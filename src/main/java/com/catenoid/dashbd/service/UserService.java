package com.catenoid.dashbd.service;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Users;

@Service
public interface UserService {
	
	public List<Users> getUserList(String searchColumn, String searchKeyword, Integer operatorId, String sort, String order, long offset, long limit, String searchCircleId);
	public int getUserListCount(String searchColumn, String searchKeyword, Integer operatorId, String searchCircleId);
	public JSONArray getUserListToJsonArray(String searchColumn, String searchKeyword, Integer operatorId, String sort, String order, long offset, long limit, String searchCircleId);
	public Users getUser(Users user);
	public boolean checkUserId(String userId);
	public boolean insertUser(Users user);
	public boolean deleteUser(Users user);
	public void insertSystemAjaxLog(String reqType, String reqSubType, String reqUrl, String reqCode, String reqMsg);
	public List<Circle> selectTownFromCircle(String circleName);
	public List<Users> selectUserListByCondition(HashMap<String, Object> param);
}

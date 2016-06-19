package com.catenoid.dashbd.service;

import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.Users;

@Service
public interface ContentService {
	
	public List<Contents> getContentList(String searchColumn, String searchKeyword, Integer operatorId, String sort, String order, long offset, long limit);
	public int getContentListCount(String searchColumn, String searchKeyword, Integer operatorId);
	public JSONArray getContentListToJsonArray(String searchColumn, String searchKeyword, Integer operatorId, String sort, String order, long offset, long limit);
	public Users getUser(Users user);
	public boolean checkUserId(String userId);
	public boolean insertUser(Users user);
	public boolean deleteUser(Users user);
	public void insertSystemAjaxLog(String reqType, String reqSubType, String reqUrl, String reqCode, String reqMsg);
}

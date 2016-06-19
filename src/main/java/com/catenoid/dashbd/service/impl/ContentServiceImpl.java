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

import com.catenoid.dashbd.dao.ContentsMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.ContentService;
import com.catenoid.dashbd.service.UserService;

@Service(value = "contentServiceImpl")
public class ContentServiceImpl implements ContentService {

	private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	@Autowired
	private SqlSession sqlSession;	
	
	/**
	 * userList 리턴
	 */
	@Override
	public List<Contents> getContentList(String searchColumn, String searchKeyword, Integer operatorId, String sort, String order, long offset, long limit) {
		if (searchColumn == null || searchColumn.isEmpty()) searchColumn = null;
		if (searchKeyword == null || searchKeyword.isEmpty()) searchKeyword = null;
		if (sort == null || sort.isEmpty()) sort = null;
		if (order == null || order.isEmpty()) order = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchColumn", searchColumn);
		map.put("searchKeyword", searchKeyword);
		map.put("operatorId", operatorId);
		map.put("sort", sort);
		map.put("order", order);
		map.put("start", offset+1);
		map.put("end", offset + limit);
		
		List<Contents> contentList = new ArrayList<Contents>();
		try {
			ContentsMapper contentsMapper = sqlSession.getMapper(ContentsMapper.class);
			contentList = contentsMapper.selectContentList(map);
			/*
			for (Users user : contentList)
				user.setPermissions(contentsMapper.selectPermissionsByUserId(user.getUserId()));
			*/
		} catch (Exception e) {
			logger.error("~~ [An error occurred]", e);
		}
		return contentList;
	}
	
	/**
	 * userListCount 리턴
	 */
	@Override
	public int getContentListCount(String searchColumn, String searchKeyword, Integer operatorId) {
		if (searchColumn == null || searchColumn.isEmpty()) searchColumn = null;
		if (searchKeyword == null || searchKeyword.isEmpty()) searchKeyword = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchColumn", searchColumn);
		map.put("searchKeyword", searchKeyword);
		map.put("operatorId", operatorId);
		
		ContentsMapper contentsMapper = sqlSession.getMapper(ContentsMapper.class);
		return contentsMapper.selectContentListCount(map);
	}

	/**
	 * userList를 JSONArray 형태로 리턴
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getContentListToJsonArray(String searchColumn, String searchKeyword, Integer operatorId, String sort, String order, long offset, long limit) {
		List<Contents> contentList = getContentList(searchColumn, searchKeyword, operatorId, sort, order, offset, limit);
		
		JSONArray jsonArray = new JSONArray();
		for (Contents content : contentList)
			jsonArray.add(content.toJSONObject());
		
		return jsonArray;
	}

	/**
	 * 사용자 정보 리턴
	 */
	@Override
	public Users getUser(Users user) {
		try {
			UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
			Users selectedUser = usersMapper.selectByUserId(user.getUserId());
			selectedUser.setPermissions(usersMapper.selectPermissionsByUserId(user.getUserId()));
			return selectedUser;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return null;
		}
	}
	
	/**
	 * 사용자 ID 중복 확인
	 */
	@Override
	public boolean checkUserId(String userId) {
		try {
			UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
			return usersMapper.selectByUserId(userId) == null ? true : false;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}
	
	/**
	 * 사용자 등록 or 수정
	 */
	@Override
	public boolean insertUser(Users user) {
		try {
			UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
			return usersMapper.insertUser(user) > 0;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	/**
	 * 사용자 삭제
	 */
	@Override
	public boolean deleteUser(Users user) {
		try {
			UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
			usersMapper.deletePermissionOfUser(user.getUserId());
			return usersMapper.deleteUser(user) > 0;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	@Override
	public void insertSystemAjaxLog(String reqType, String reqSubType, String reqUrl, String reqCode, String reqMsg) {
		try {
			UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("reqType", reqType);
			map.put("reqSubType", reqSubType);
			map.put("reqUrl", reqUrl);
			map.put("reqCode", reqCode);
			map.put("reqMsg", reqMsg);
			
			usersMapper.insertSystemAjaxLog(map);
			
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
		}
	}
}

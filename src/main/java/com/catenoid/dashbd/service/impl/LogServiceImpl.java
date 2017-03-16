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

import com.catenoid.dashbd.dao.LogMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.LogService;
import com.catenoid.dashbd.service.UserService;

@Service(value = "logServiceImpl")
public class LogServiceImpl implements LogService {

	private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
	
	@Autowired
	private SqlSession sqlSession;	
	
	@Override
	public List<HashMap<String, Object>> selectLogDate(HashMap<String, Object> param) {
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		return logMapper.selectLogDate(param);
	} 
}

package com.catenoid.dashbd.service;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Users;

@Service
public interface LogService {
	
	public List<HashMap<String, Object>> selectLogDate(HashMap<String, Object> param);
	
}

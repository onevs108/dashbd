package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;

public interface CircleMapper {
	
	List<HashMap<String, String>> selectCity();

	int checkCircleExist(String circleName);

	int insertCircle(HashMap<String, String> param);

	int deleteCircle(String circleId);

	int insertCity(HashMap<String, String> param);

	int checkSAID(String checkSAID);
	
}
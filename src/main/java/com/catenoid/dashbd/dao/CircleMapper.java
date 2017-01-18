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

	List<HashMap<String, String>> selectCityFromCircleId(HashMap<String, String> param);

	int selectCityFromCircleIdCount(HashMap<String, String> param);

	int deleteCity(String cityId);

	int moveCityOtherCircle(HashMap<String, String> param);

	List<HashMap<String, String>> getCircleCityListSearch(HashMap<String, String> param);

	int getCircleCityListSearchCount(HashMap<String, String> param);

}
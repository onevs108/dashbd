package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;

public interface HotSpotMapper {

	List<HashMap<String, String>> selectHotSpotFromCityId(HashMap<String, String> param);

	int selectHotSpotFromCityIdCount(HashMap<String, String> param);

	List<HashMap<String, String>> selectCityFromCircleId(HashMap<String, String> param);

	int deleteHotSpot(String hotspotId);

	int insertHotSpot(HashMap<String, String> param);

}
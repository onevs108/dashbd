package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;

public interface MoodMapper {

	List<HashMap<String, Object>> selectMoodService(HashMap<String, Object> searchParam);

	int selectMoodServiceCount(HashMap<String, Object> searchParam);
    
}
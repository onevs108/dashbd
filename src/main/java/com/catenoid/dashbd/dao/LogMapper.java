package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;

public interface LogMapper {
    List<HashMap<String, Object>> selectLogDate(HashMap<String, Object> param);
}
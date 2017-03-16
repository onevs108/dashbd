package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;

import com.catenoid.dashbd.dao.model.Log;

public interface LogMapper {
    List<Log> selectLogDate(HashMap<String, Object> param);
}
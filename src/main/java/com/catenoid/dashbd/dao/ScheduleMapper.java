package com.catenoid.dashbd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.catenoid.dashbd.dao.model.Schedule;
import com.catenoid.dashbd.dao.model.ScheduleExample;

public interface ScheduleMapper {
	
	/* inbo coding START*/
	int selectSchduleMaxPosition(Map map);
	List<Map> selectSchdule(Map map);
	Map selectSchduleContentURL(Map map);
	Map selectSchduleTime(Map map);
	int addScheduleWithInitContent(Map map);
	int modifyScheduleTime(Map map);
	int insertBroadcastInfo(Map map);
	int insertAddSchedule(Map map);
	int updateBroadcastInfo(Map map);
	int updateSchedule(Map map);
	int updateSchedule4Del(Map map);
	Map selectBroadcast(Map map);
	Map selectGBRSum(Map map);
	/* inbo coding END*/
    int countByExample(ScheduleExample example);

    int deleteByExample(ScheduleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Schedule record);

    int insertSelective(Schedule record);

    List<Schedule> selectByExample(ScheduleExample example);

    Schedule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Schedule record, @Param("example") ScheduleExample example);

    int updateByExample(@Param("record") Schedule record, @Param("example") ScheduleExample example);

    int updateByPrimaryKeySelective(Schedule record);

    int updateByPrimaryKey(Schedule record);
    
	List<Map<String, String>> checkBandwidth(Map<String, String> params);
    
    
}
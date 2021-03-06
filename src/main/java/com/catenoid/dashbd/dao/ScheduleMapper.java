package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.catenoid.dashbd.dao.model.Schedule;
import com.catenoid.dashbd.dao.model.ScheduleExample;

public interface ScheduleMapper {
	
	/* inbo coding START*/
	int selectSchduleMaxPosition(Map map);
	List<Map> selectSchdule(Map map);
	Map<String, String> selectSchduleContentURL(Map<String, String> map);
	Map<String, String> selectSchduleTime(Map<String, String> map);
	int addScheduleWithInitContent(Map map);
	int modifyScheduleTime(Map map);
	int insertBroadcastInfo(Map map);
	int insertAddSchedule(Map map);
	int updateBroadcastInfo(Map map);
	int updateSchedule(Map map);
	int updateSchedule4Del(Map map);
	Map selectBroadcast(Map map);
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
	
	int checkExistSaid(Map<String, String> params);
	
	void insertScheduleContent(HashMap<String, String> param);
	
	List<HashMap<String, String>> getGroupListFromCircleId(HashMap<String, String> param);
	
	List<HashMap<String, String>> getGroupSaidList(HashMap<String, String> param);
	List<HashMap<String, String>> selectBroadcastToday(String searchDate);
	List<Map<String, String>> selectSchduleContentList(Map<String, String> params);
	List<Map<String, String>> selectServiceClassList(HashMap<String, Object> params);
	int selectServiceClassCount(HashMap<String, Object> params);
	int insertServiceClass(HashMap<String, Object> params);
	int selectServiceClass(HashMap<String, Object> params);
	List<Map<String, String>> selectServiceClassAll();
	int editServiceClass(HashMap<String, Object> params);
	int deleteServiceClass(HashMap<String, Object> params);
	List<Map<String, String>> selectServiceIdList(HashMap<String, Object> params);
	int selectServiceIdCount(HashMap<String, Object> params);
	int selectServiceId(HashMap<String, Object> params);
	int insertServiceId(HashMap<String, Object> params);
	List<Map<String, String>> selectServiceIdAll();
	int selectServiceIdIdx();
	int checkServiceId(HashMap<String, Object> params);
	int updateServiceIdIdx();
	void editServiceId(HashMap<String, Object> params);
	void deleteServiceId(HashMap<String, Object> params);
	HashMap<String, String> getEnableBandwidth(Map<String, String> params);
	List<String> selectSaidRange(Map<String, String> map);
	void insertMoodRequest(HashMap<String, String> param);
	List<Map<String, Object>> selectMoodRequestInfo(Map<String, Object> modeLimit);
	Map<String, Object> selectCrsLimit();
	void insertMoodRequestDetail(HashMap<String, String> param);
	String getBcIdFromServiceId(Map<String, String> bcParam);
	Map<String, String> selectSchduleContentList(String string);
	List<String> selectMoodServiceId(Map<String, Object> modeLimit);
	List<HashMap<String, String>> getSendBcSaid(Map<String, Object> modeLimit);
	String getScheduleIdFromBCID(Map<String, String> bcParam);
	HashMap<String, String> getCrsInfo(HashMap<String, String> crsParam);
	String getCrsInfoFromMapping(String string);
	int checkMoodServiceId(HashMap<String, String> param);
	int checkMoodSaId(HashMap<String, String> param);
	List<HashMap<String, String>> getCurrentMoodService(String crsId);
	HashMap<String, String> getMoodRetrieve(HashMap<String, String> reParam);
	void updateSaidMode(HashMap<String, String> hashMap);
	int deleteScheduleContent(HashMap<String, String> param);
	int deleteScheduleContent(Map<String, String> params);
	int updateServiceIdIdx(Map<String, String> params);
	List<HashMap<String, String>> getCurrentMoodServiceOthers(HashMap<String, String> crsParam);
	int insertServiceIdIdx(HashMap<String, Object> params);
	int getServiceIdIdx(HashMap<String, Object> params);
	String selectServiceIdName(HashMap<String, Object> params);
	void editServiceIdIdx(HashMap<String, Object> params);
	HashMap<String, String> getThreashold(String string);
	void insertMoodService(Map<String, String> params);
	List<HashMap<String, String>> getTimeStampMoodService(String crsId);
	int getMoodInterval();
	List<HashMap<String, String>> getBandWidthInfoList(String searchString);
    
}
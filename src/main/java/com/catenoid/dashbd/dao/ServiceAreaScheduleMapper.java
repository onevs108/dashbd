package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceAreaSchedule;
import com.catenoid.dashbd.dao.model.ServiceAreaScheduleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceAreaScheduleMapper {
    int countByExample(ServiceAreaScheduleExample example);

    int deleteByExample(ServiceAreaScheduleExample example);

    int insert(ServiceAreaSchedule record);

    int insertSelective(ServiceAreaSchedule record);

    List<ServiceAreaSchedule> selectByExample(ServiceAreaScheduleExample example);
    
    List<ServiceAreaSchedule> selectServiceAreaSchedule(ServiceAreaSchedule record);

    int updateByExampleSelective(@Param("record") ServiceAreaSchedule record, @Param("example") ServiceAreaScheduleExample example);

    int updateByExample(@Param("record") ServiceAreaSchedule record, @Param("example") ServiceAreaScheduleExample example);
}
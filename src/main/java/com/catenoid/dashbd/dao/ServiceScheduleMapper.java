package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceSchedule;
import com.catenoid.dashbd.dao.model.ServiceScheduleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceScheduleMapper {
    int countByExample(ServiceScheduleExample example);

    int deleteByExample(ServiceScheduleExample example);
    
    int deleteByServiceId(Integer id);

    int insert(ServiceSchedule record);

    int insertSelective(ServiceSchedule record);

    List<ServiceSchedule> selectByExample(ServiceScheduleExample example);
    
    List<ServiceSchedule> selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceSchedule record, @Param("example") ServiceScheduleExample example);

    int updateByExample(@Param("record") ServiceSchedule record, @Param("example") ServiceScheduleExample example);
}
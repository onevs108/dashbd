package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Service;
import com.catenoid.dashbd.dao.model.ServiceExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ServiceMapper {
    int countByExample(ServiceExample example);

    int deleteByExample(ServiceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Service record);

    int insertSelective(Service record);

    List<Service> selectByExample(ServiceExample example);
    
    Service selectTypeTransferConfig(Integer id);

    Service selectByPrimaryKey(Integer id);
    
    Map<String, String> selectBmscAgentKey(Integer id);

    int updateByExampleSelective(@Param("record") Service record, @Param("example") ServiceExample example);

    int updateByExample(@Param("record") Service record, @Param("example") ServiceExample example);

    int updateByPrimaryKeySelective(Service record);

    int updateByPrimaryKey(Service record);
}
package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceServiceArea;
import com.catenoid.dashbd.dao.model.ServiceServiceAreaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceServiceAreaMapper {
    int countByExample(ServiceServiceAreaExample example);

    int deleteByExample(ServiceServiceAreaExample example);
    
    int deleteByServiceId(Integer id);

    int insert(ServiceServiceArea record);

    int insertSelective(ServiceServiceArea record);

    List<ServiceServiceArea> selectByExample(ServiceServiceAreaExample example);
    
    List<ServiceServiceArea> selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceServiceArea record, @Param("example") ServiceServiceAreaExample example);

    int updateByExample(@Param("record") ServiceServiceArea record, @Param("example") ServiceServiceAreaExample example);
}
package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbApExample;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.dao.model.ServiceAreaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceAreaMapper {
    int countByExample(ServiceAreaExample example);

    int deleteByExample(ServiceAreaExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceArea record);

    int insertSelective(ServiceArea record);

    List<ServiceArea> selectByExample(ServiceAreaExample example);
    
    List<ServiceAreaEnbAp> selectServiceAreaEnbAp(ServiceAreaEnbApExample example);
    
    List<ServiceAreaEnbAp> selectServiceAreaEnbApRangeOuterJoin(ServiceAreaEnbApExample example);

    ServiceArea selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceArea record, @Param("example") ServiceAreaExample example);

    int updateByExample(@Param("record") ServiceArea record, @Param("example") ServiceAreaExample example);

    int updateByPrimaryKeySelective(ServiceArea record);

    int updateByPrimaryKey(ServiceArea record);
}
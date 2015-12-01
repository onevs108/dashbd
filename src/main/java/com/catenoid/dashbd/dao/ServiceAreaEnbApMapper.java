package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbApExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceAreaEnbApMapper {
    int countByExample(ServiceAreaEnbApExample example);

    int deleteByExample(ServiceAreaEnbApExample example);

    int insert(ServiceAreaEnbAp record);

    int insertSelective(ServiceAreaEnbAp record);

    List<ServiceAreaEnbAp> selectByExample(ServiceAreaEnbApExample example);

    int updateByExampleSelective(@Param("record") ServiceAreaEnbAp record, @Param("example") ServiceAreaEnbApExample example);

    int updateByExample(@Param("record") ServiceAreaEnbAp record, @Param("example") ServiceAreaEnbApExample example);
}
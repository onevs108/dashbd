package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceType;
import com.catenoid.dashbd.dao.model.ServiceTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceTypeMapper {
    int countByExample(ServiceTypeExample example);

    int deleteByExample(ServiceTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceType record);

    int insertSelective(ServiceType record);

    List<ServiceType> selectByExample(ServiceTypeExample example);

    ServiceType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceType record, @Param("example") ServiceTypeExample example);

    int updateByExample(@Param("record") ServiceType record, @Param("example") ServiceTypeExample example);

    int updateByPrimaryKeySelective(ServiceType record);

    int updateByPrimaryKey(ServiceType record);
}
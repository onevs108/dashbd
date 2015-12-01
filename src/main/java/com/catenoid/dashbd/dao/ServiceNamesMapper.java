package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.ServiceNames;
import com.catenoid.dashbd.dao.model.ServiceNamesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceNamesMapper {
    int countByExample(ServiceNamesExample example);

    int deleteByExample(ServiceNamesExample example);

    int deleteByPrimaryKey(Integer id);
    
    int deleteByServiceId(Integer id);

    int insert(ServiceNames record);

    int insertSelective(ServiceNames record);

    List<ServiceNames> selectByExample(ServiceNamesExample example);

    List<ServiceNames> selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceNames record, @Param("example") ServiceNamesExample example);

    int updateByExample(@Param("record") ServiceNames record, @Param("example") ServiceNamesExample example);

    int updateByPrimaryKeySelective(ServiceNames record);

    int updateByPrimaryKey(ServiceNames record);
}
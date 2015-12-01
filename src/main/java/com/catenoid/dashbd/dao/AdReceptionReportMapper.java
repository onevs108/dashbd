package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.AdReceptionReport;
import com.catenoid.dashbd.dao.model.AdReceptionReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdReceptionReportMapper {
    int countByExample(AdReceptionReportExample example);

    int deleteByExample(AdReceptionReportExample example);

    int deleteByPrimaryKey(Integer serviceId);

    int insert(AdReceptionReport record);

    int insertSelective(AdReceptionReport record);

    List<AdReceptionReport> selectByExample(AdReceptionReportExample example);

    AdReceptionReport selectByPrimaryKey(Integer serviceId);

    int updateByExampleSelective(@Param("record") AdReceptionReport record, @Param("example") AdReceptionReportExample example);

    int updateByExample(@Param("record") AdReceptionReport record, @Param("example") AdReceptionReportExample example);

    int updateByPrimaryKeySelective(AdReceptionReport record);

    int updateByPrimaryKey(AdReceptionReport record);
}
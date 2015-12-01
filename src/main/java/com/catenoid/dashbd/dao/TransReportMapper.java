package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.TransReport;
import com.catenoid.dashbd.dao.model.TransReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransReportMapper {
    int countByExample(TransReportExample example);

    int deleteByExample(TransReportExample example);

    int insert(TransReport record);

    int insertSelective(TransReport record);
    
    TransReport selectByPrimaryKey(Integer id);
    
    int selectCount();

    List<TransReport> selectByExampleWithBLOBs(TransReportExample example);

    List<TransReport> selectByExample(TransReportExample example);
    
    List<TransReport> selectByServiceAreaId(@Param("record") TransReport report);
    
    int selectCountByServiceAreaId(@Param("record") TransReport report);
    
    int updateByExampleSelective(@Param("record") TransReport record, @Param("example") TransReportExample example);

    int updateByExampleWithBLOBs(@Param("record") TransReport record, @Param("example") TransReportExample example);

    int updateByExample(@Param("record") TransReport record, @Param("example") TransReportExample example);
}
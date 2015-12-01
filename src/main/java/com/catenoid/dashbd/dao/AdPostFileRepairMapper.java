package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.AdPostFileRepair;
import com.catenoid.dashbd.dao.model.AdPostFileRepairExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdPostFileRepairMapper {
    int countByExample(AdPostFileRepairExample example);

    int deleteByExample(AdPostFileRepairExample example);

    int deleteByPrimaryKey(Integer serviceId);

    int insert(AdPostFileRepair record);

    int insertSelective(AdPostFileRepair record);

    List<AdPostFileRepair> selectByExample(AdPostFileRepairExample example);

    AdPostFileRepair selectByPrimaryKey(Integer serviceId);

    int updateByExampleSelective(@Param("record") AdPostFileRepair record, @Param("example") AdPostFileRepairExample example);

    int updateByExample(@Param("record") AdPostFileRepair record, @Param("example") AdPostFileRepairExample example);

    int updateByPrimaryKeySelective(AdPostFileRepair record);

    int updateByPrimaryKey(AdPostFileRepair record);
}
package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.StatusNotify;
import com.catenoid.dashbd.dao.model.StatusNotifyExample;
import com.catenoid.dashbd.dao.model.StatusNotifyWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatusNotifyMapper {
    int countByExample(StatusNotifyExample example);

    int deleteByExample(StatusNotifyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StatusNotifyWithBLOBs record);

    int insertSelective(StatusNotifyWithBLOBs record);

    List<StatusNotifyWithBLOBs> selectByExampleWithBLOBs(StatusNotifyExample example);

    List<StatusNotify> selectByExample(StatusNotifyExample example);

    StatusNotifyWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StatusNotifyWithBLOBs record, @Param("example") StatusNotifyExample example);

    int updateByExampleWithBLOBs(@Param("record") StatusNotifyWithBLOBs record, @Param("example") StatusNotifyExample example);

    int updateByExample(@Param("record") StatusNotify record, @Param("example") StatusNotifyExample example);

    int updateByPrimaryKeySelective(StatusNotifyWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(StatusNotifyWithBLOBs record);

    int updateByPrimaryKey(StatusNotify record);
}
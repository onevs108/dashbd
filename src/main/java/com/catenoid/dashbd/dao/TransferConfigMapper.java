package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.TransferConfig;
import com.catenoid.dashbd.dao.model.TransferConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransferConfigMapper {
    int countByExample(TransferConfigExample example);

    int deleteByExample(TransferConfigExample example);
    
    int deleteByServiceId(Integer id);

    int insert(TransferConfig record);

    int insertSelective(TransferConfig record);

    List<TransferConfig> selectByExample(TransferConfigExample example);
    
    TransferConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TransferConfig record, @Param("example") TransferConfigExample example);

    int updateByExample(@Param("record") TransferConfig record, @Param("example") TransferConfigExample example);
    
    int updateByPrimaryKeySelective(TransferConfig record);
}
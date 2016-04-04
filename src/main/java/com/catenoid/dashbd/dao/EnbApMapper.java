package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.EnbAp;
import com.catenoid.dashbd.dao.model.EnbApExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EnbApMapper {
    int countByExample(EnbApExample example);

    int deleteByExample(EnbApExample example);

    int deleteByPrimaryKey(Integer id);;

    int deleteByPrimaryKeyService(Integer id);

    int insert(EnbAp record);

    int insertSelective(EnbAp record);

    List<EnbAp> selectByExample(EnbApExample example);

    EnbAp selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EnbAp record, @Param("example") EnbApExample example);

    int updateByExample(@Param("record") EnbAp record, @Param("example") EnbApExample example);

    int updateByPrimaryKeySelective(EnbAp record);

    int updateByPrimaryKey(EnbAp record);
}
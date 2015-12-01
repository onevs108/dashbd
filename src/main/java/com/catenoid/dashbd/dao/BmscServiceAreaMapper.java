package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.BmscServiceArea;
import com.catenoid.dashbd.dao.model.BmscServiceAreaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmscServiceAreaMapper {
    int countByExample(BmscServiceAreaExample example);

    int deleteByExample(BmscServiceAreaExample example);

    int insert(BmscServiceArea record);

    int insertSelective(BmscServiceArea record);

    List<BmscServiceArea> selectByExample(BmscServiceAreaExample example);

    int updateByExampleSelective(@Param("record") BmscServiceArea record, @Param("example") BmscServiceAreaExample example);

    int updateByExample(@Param("record") BmscServiceArea record, @Param("example") BmscServiceAreaExample example);
}
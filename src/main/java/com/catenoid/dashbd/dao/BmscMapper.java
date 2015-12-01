package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.BmscServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.BmscExample;
import com.catenoid.dashbd.dao.model.BmscServiceArea;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmscMapper {
    int countByExample(BmscExample example);

    int deleteByExample(BmscExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Bmsc record);

    int insertSelective(Bmsc record);

    List<Bmsc> selectByExample(BmscExample example);
    
    List<BmscServiceAreaEnbAp> selectBmscServiceAreaEnbAp(Integer id);
    
    List<BmscServiceArea> selectBmscServiceArea(Integer id);

    Bmsc selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Bmsc record, @Param("example") BmscExample example);

    int updateByExample(@Param("record") Bmsc record, @Param("example") BmscExample example);

    int updateByPrimaryKeySelective(Bmsc record);

    int updateByPrimaryKey(Bmsc record);
}
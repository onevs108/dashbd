package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.OperatorBmsc;
import com.catenoid.dashbd.dao.model.OperatorBmscExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperatorBmscMapper {
    int countByExample(OperatorBmscExample example);

    int deleteByExample(OperatorBmscExample example);

    int insert(OperatorBmsc record);

    int insertSelective(OperatorBmsc record);

    List<OperatorBmsc> selectByExample(OperatorBmscExample example);
    
    List<OperatorBmsc> selectOperatorBmsc(OperatorBmsc record);

    int updateByExampleSelective(@Param("record") OperatorBmsc record, @Param("example") OperatorBmscExample example);

    int updateByExample(@Param("record") OperatorBmsc record, @Param("example") OperatorBmscExample example);
    
    
    List<Integer> selectBmscIdListOfOperator(Integer operatorId);
    
    int insertOperatorBmsc(Bmsc bmsc);
    
    int deleteOperatorBmscOfOperator(Integer operatorId);
    
    int deleteOperatorBmscOfBmsc(Integer bmscId);
}
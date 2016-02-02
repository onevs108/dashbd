package com.catenoid.dashbd.dao;

import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OperatorMapper {
    int countByExample(OperatorExample example);

    int deleteByExample(OperatorExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Operator record);

    int insertSelective(Operator record);

    List<Operator> selectByExample(OperatorExample example);

    Operator selectByPrimaryKey(Integer id);
    
    int updateByExampleSelective(@Param("record") Operator record, @Param("example") OperatorExample example);

    int updateByExample(@Param("record") Operator record, @Param("example") OperatorExample example);

    int updateByPrimaryKeySelective(Operator record);

    int updateByPrimaryKey(Operator record);
    

    List<Operator> selectOperatorListAll();
    
    List<Operator> selectOperatorList(Map<String, Object> map);
    
    int selectOperatorListCount();
    
    Operator selectByOperatorName(String operatorName);
    
    int insertOperator(Operator operator);
}
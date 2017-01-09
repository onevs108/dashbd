package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorExample;

public interface OperatorMapper {
	
    int countByExample(OperatorExample example);
    int deleteByExample(OperatorExample example);
    int deleteGrade(Integer id);
    int deleteCircle(Integer id);
    int insert(Operator record);
    int insertSelective(Operator record);
    List<Operator> selectByExample(OperatorExample example);
    Operator selectByPrimaryKey(Integer id);
    int updateByExampleSelective(@Param("record") Operator record, @Param("example") OperatorExample example);
    int updateByExample(@Param("record") Operator record, @Param("example") OperatorExample example);
    int updateByPrimaryKeySelective(Operator record);
    int updateByPrimaryKey(Operator record);
    List<Circle> selectOperatorListAll();
    List<Operator> selectGradeListAll();
    List<Operator> selectOperatorList(Map<String, Object> map);
    int selectOperatorListCount();
    int insertOperator(Operator operator);
	int getGradeListCount();
	int insertGrade(Operator operator);
	Operator selectByGradeName(String operatorName);
	Operator selectByOperatorName(String operatorName);
	List<Circle> selectCircleListAll();
	List<Circle> selectTownFromCircle(HashMap<String, String> param);
	int selectTownFromCircleCount(HashMap<String, String> param);
	List<Operator> selectGradeList(Map<String, Object> map);
	List<Circle> selectTownListAll();
	
}
package com.catenoid.dashbd.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorExample;
import com.catenoid.dashbd.dao.model.Users;

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
	int selectOperatorFromCircleCount(HashMap<String, String> param);
	List<Operator> selectGradeList(Map<String, Object> map);
	List<Circle> selectTownListAll();
	List<HashMap<String, String>> selectOperatorFromCircle(HashMap<String, String> param);
	List<HashMap<String, String>> selectCityFromCircle(HashMap<String, String> param);
	
	List<Users> selectMemberList(HashMap<String, Object> param);
	int getMemberListCount(HashMap<String, Object> param);
	
	List<Circle> selectCircleListNameAll();
}
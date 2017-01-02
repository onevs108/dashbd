package com.catenoid.dashbd.service;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Operator;

@Service
public interface OperatorService {
	
	public List<Operator> getOperatorList(String sort, String order, long offset, long limit);
	public List<Operator> getGradeListAll();
	public int getGradeListCount();
	public int getOperatorListCount();
	public JSONArray getOperatorListToJsonArray(String sort, String order, long offset, long limit);
	public Operator getOperator(Integer operatorId);
	public boolean checkGradeName(String operatorName);
	public boolean checkOperatorName(String operatorName);
	public boolean insertGrade(Operator operator);
	public boolean insertOperator(Operator operator);
	public boolean deleteGrade(Integer gradeId);
	public boolean deleteCircle(Integer circleId);
	public List<Circle> getCircleList();
	public List<Circle> getCircleListAll();
	public List<Circle> selectTownFromCircle(HashMap<String,String> param);
	public int selectTownFromCircleCount(HashMap<String, String> param);
	
}

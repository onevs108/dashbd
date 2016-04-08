package com.catenoid.dashbd.service;

import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.model.Operator;

@Service
public interface OperatorService {
	
	public List<Operator> getOperatorListAll();
	
	public List<Operator> getOperatorList(String sort, String order, long offset, long limit);
	public int getOperatorListCount();
	public JSONArray getOperatorListToJsonArray(String sort, String order, long offset, long limit);
	public Operator getOperator(Integer operatorId);
	public boolean checkOperatorName(String operatorName);
	public boolean insertOperator(Operator operator);
	public boolean deleteOperator(Integer operatorId);
	
}

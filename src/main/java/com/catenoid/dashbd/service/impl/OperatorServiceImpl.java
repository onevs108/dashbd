package com.catenoid.dashbd.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.OperatorMapper;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.service.OperatorService;

@Service(value = "operatorServiceImpl")
public class OperatorServiceImpl implements OperatorService {

	private static final Logger logger = LoggerFactory.getLogger(OperatorServiceImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 모든 Operator 리스트 리턴
	 */
	@Override
	public List<Operator> getOperatorListAll() {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectOperatorListAll();
	}

	/**
	 * Operator 리스트 리턴
	 */
	@Override
	public List<Operator> getOperatorList(long offset, long limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", offset);
		map.put("end", offset + limit);
		
		List<Operator> operatorList = new ArrayList<Operator>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			operatorList = operatorMapper.selectOperatorList(map);
		} catch (Exception e) {
			logger.error("~~ [An error occurred]", e);
		}
		return operatorList;
	}

	/**
	 * Operator 리스트 Count 리턴
	 */
	@Override
	public int getOperatorListCount() {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectOperatorListCount();
	}

	/**
	 * Operator 리스트를 JSONArray 형식으로 리턴
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getOperatorListToJsonArray(long offset, long limit) {
		List<Operator> operatorList = getOperatorList(offset, limit);
		
		JSONArray jsonArray = new JSONArray();
		for (Operator operator : operatorList)
			jsonArray.add(operator.toJSONObject());
		
		return jsonArray;
	}

	/**
	 * Operator 리턴
	 */
	@Override
	public Operator getOperator(Integer operatorId) {
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			return operatorMapper.selectByPrimaryKey(operatorId);
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return null;
		}
	}

	/**
	 * Operator 등록 or 수정
	 */
	@Override
	public boolean insertOperator(Operator operator) {
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			return operatorMapper.insertOperator(operator) > 0;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	/**
	 * Operator 삭제
	 */
	@Override
	public boolean deleteOperator(Integer operatorId) {
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			return operatorMapper.deleteByPrimaryKey(operatorId) > 0;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

}

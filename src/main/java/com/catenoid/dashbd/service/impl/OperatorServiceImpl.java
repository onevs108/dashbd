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

import com.catenoid.dashbd.dao.BmscMapper;
import com.catenoid.dashbd.dao.OperatorBmscMapper;
import com.catenoid.dashbd.dao.OperatorMapper;
import com.catenoid.dashbd.dao.UsersMapper;
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
	public List<Operator> getOperatorList(String sort, String order, long offset, long limit) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sort", sort);
		map.put("order", order);
		map.put("start", offset+1);
		map.put("end", offset + limit);
		
		List<Operator> operatorList = new ArrayList<Operator>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "getOperatorList");
			syslogMap.put("reqUrl", "operator/list.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			operatorList = operatorMapper.selectOperatorList(map);
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "getOperatorList");
			syslogMap.put("reqUrl", "operator/list.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
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
	public JSONArray getOperatorListToJsonArray(String sort, String order, long offset, long limit) {
		List<Operator> operatorList = getOperatorList(sort, order, offset, limit);
		
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
	 * Operator Name 중복 확인
	 */
	@Override
	public boolean checkOperatorName(String operatorName) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "check");
			syslogMap.put("reqUrl", "operator/check.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.selectByOperatorName(operatorName) == null ? true : false;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "check");
			syslogMap.put("reqUrl", "operator/check.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	/**
	 * Operator 등록 or 수정
	 */
	@Override
	public boolean insertOperator(Operator operator) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "insertOperator");
			syslogMap.put("reqUrl", "operator/insert.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.insertOperator(operator) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "insertOperator");
			syslogMap.put("reqUrl", "operator/insert.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	/**
	 * Operator 삭제
	 */
	@Override
	public boolean deleteOperator(Integer operatorId) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			OperatorBmscMapper operatorBmscMapper = sqlSession.getMapper(OperatorBmscMapper.class);
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);

			// Operator에 딸린 BMSC도 삭제해준다.
			// foreign key 자체가 없어 별도로 지워줘야 한다.
			List<Integer> bmscIdList = operatorBmscMapper.selectBmscIdListOfOperator(operatorId);
			operatorBmscMapper.deleteOperatorBmscOfOperator(operatorId);
			if (bmscIdList != null && !bmscIdList.isEmpty()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bmscIdList", bmscIdList);
				bmscMapper.deleteBmscs(map);
			}

			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "deleteOperator");
			syslogMap.put("reqUrl", "operator/delete.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.deleteByPrimaryKey(operatorId) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "deleteOperator");
			syslogMap.put("reqUrl", "operator/delete.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}
}

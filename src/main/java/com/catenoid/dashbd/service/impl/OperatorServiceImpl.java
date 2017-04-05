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
import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.Users;
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
	public List<Circle> getCircleListAll() {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectCircleListAll();
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
//			usersMapper.insertSystemAjaxLog(syslogMap);
			operatorList = operatorMapper.selectGradeList(map);
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "getOperatorList");
			syslogMap.put("reqUrl", "operator/list.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
//			usersMapper.insertSystemAjaxLog(syslogMap);
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
			operatorName.replaceAll("\n", "").replaceAll("\t", "");
//			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.selectByOperatorName(operatorName) == null ? true : false;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "check");
			syslogMap.put("reqUrl", "operator/check.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
//			usersMapper.insertSystemAjaxLog(syslogMap);
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
//			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.insertOperator(operator) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "insertOperator");
			syslogMap.put("reqUrl", "operator/insert.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
//			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}
	
	/**
	 * Grade 삭제
	 */
	@Override
	public boolean deleteGrade(Integer gradeId) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			OperatorBmscMapper operatorBmscMapper = sqlSession.getMapper(OperatorBmscMapper.class);
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			
			// Grade에 딸린 BMSC도 삭제해준다.
			// foreign key 자체가 없어 별도로 지워줘야 한다.
			List<Integer> bmscIdList = operatorBmscMapper.selectBmscIdListOfOperator(gradeId);
			operatorBmscMapper.deleteOperatorBmscOfOperator(gradeId);
			if (bmscIdList != null && !bmscIdList.isEmpty()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bmscIdList", bmscIdList);
				bmscMapper.deleteBmscs(map);
			}
			syslogMap.put("reqType", "Grade Mgmt");
			syslogMap.put("reqSubType", "deleteGrade");
			syslogMap.put("reqUrl", "grade/delete.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
//			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.deleteGrade(gradeId) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "Grade Mgmt");
			syslogMap.put("reqSubType", "deleteGrade");
			syslogMap.put("reqUrl", "grade/delete.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
//			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	/**
	 * Circle 삭제
	 */
	@Override
	public boolean deleteCircle(Integer circleId) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			OperatorBmscMapper operatorBmscMapper = sqlSession.getMapper(OperatorBmscMapper.class);
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);

			// Operator에 딸린 BMSC도 삭제해준다.
			// foreign key 자체가 없어 별도로 지워줘야 한다.
			List<Integer> bmscIdList = operatorBmscMapper.selectBmscIdListOfOperator(circleId);
			operatorBmscMapper.deleteOperatorBmscOfOperator(circleId);
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
//			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.deleteCircle(circleId) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "deleteOperator");
			syslogMap.put("reqUrl", "operator/delete.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
//			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	@Override
	public List<Circle> getCircleList() {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		List<Circle> circleList = operatorMapper.selectCircleListAll();
		return circleList;
	}

	@Override
	public int getGradeListCount() {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.getGradeListCount();
	}

	@Override
	public boolean insertGrade(Operator operator) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "insertGrade");
			syslogMap.put("reqUrl", "grade/insert.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
//			usersMapper.insertSystemAjaxLog(syslogMap);
			return operatorMapper.insertGrade(operator) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "insertGrade");
			syslogMap.put("reqUrl", "grade/insert.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
//			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	@Override
	public boolean checkGradeName(String groupName) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "check");
			syslogMap.put("reqUrl", "grade/check.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
//			usersMapper.insertSystemAjaxLog(syslogMap);
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("groupName", groupName);
			return operatorMapper.selectByGradeInfo(param) == null ? true : false;
		} catch (Exception e) {
			syslogMap.put("reqType", "Operator Mgmt");
			syslogMap.put("reqSubType", "check");
			syslogMap.put("reqUrl", "grade/check.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
//			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	@Override
	public List<HashMap<String,String>> selectCityFromCircle(HashMap<String,String> param) {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectCityFromCircle(param);
	}

	@Override
	public int selectOperatorFromCircleCount(HashMap<String, String> param) {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectOperatorFromCircleCount(param);
	}

	@Override
	public List<Operator> getGradeListAll() {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectGradeListAll();
	}

	@Override
	public List<HashMap<String, String>> selectOperatorFromCircle(HashMap<String, String> param) {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectOperatorFromCircle(param);
	}
	
	@Override
	public List<Users> selectMemberList(HashMap<String, Object> param) {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectMemberList(param);
	}
	
	@Override
	public Operator selectByGradeInfo(HashMap<String, Object> param) {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectByGradeInfo(param);
	}
	
	@Override
	public Operator selectByOperatorName(String operatorName) {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectByOperatorName(operatorName);
	}
	
	@Override
	public int getMemberListCount(HashMap<String, Object> param) {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.getMemberListCount(param);
	}
	
	/**
	 * 모든 Operator 리스트 리턴
	 */
	@Override
	public List<Circle> getCircleListNameAll() {
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		return operatorMapper.selectCircleListNameAll();
	}
}

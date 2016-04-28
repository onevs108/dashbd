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
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.Embms;
import com.catenoid.dashbd.service.BmscService;

@Service(value = "bmscServiceImpl")
public class BmscServiceImpl implements BmscService {

	private static final Logger logger = LoggerFactory.getLogger(BmscServiceImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * BMSC 리스트 리턴
	 */
	@Override
	public List<Bmsc> getBmscList(Integer operatorId, String sort, String order, long offset, long limit) {
		if (sort == null || sort.isEmpty()) sort = null;
		if (order == null || order.isEmpty()) order = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("operatorId", operatorId);
		map.put("sort", sort);
		map.put("order", order);
		map.put("start", offset+1);
		map.put("end", offset + limit);
		
		List<Bmsc> bmscList = new ArrayList<Bmsc>();
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			bmscList = bmscMapper.selectBmscList(map);
		} catch (Exception e) {
			logger.error("~~ [An error occurred]", e);
		}
		return bmscList;
	}

	/**
	 * BMSC 리스트 개수 리턴
	 */
	@Override
	public int getBmscListCount(Integer operatorId) {
		BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
		return bmscMapper.selectBmscListCount(operatorId);
	}

	/**
	 * BMSC 리스트를 JSONArray 형식으로 리턴
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getBmscListToJsonArray(Integer operatorId, String sort, String order, long offset, long limit) {
		List<Bmsc> bmscList = getBmscList(operatorId, sort, order, offset, limit);
		
		JSONArray jsonArray = new JSONArray();
		for (Bmsc bmsc : bmscList)
			jsonArray.add(bmsc.toJSONObject());
		
		return jsonArray;
	}

	/**
	 * BMSC 리턴
	 */
	@Override
	public Bmsc getBmsc(Integer bmscId) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			syslogMap.put("reqType", "BM-SC Mgmt");
			syslogMap.put("reqSubType", "getBmsc");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return bmscMapper.selectBmsc(bmscId);
		} catch (Exception e) {
			syslogMap.put("reqType", "BM-SC Mgmt");
			syslogMap.put("reqSubType", "getBmsc");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return null;
		}
	}

	/**
	 * BMSC 등록 or 수정
	 */
	@Override
	public boolean insertBmsc(Bmsc bmsc) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			
			if (bmscMapper.insertBmsc(bmsc) > 0) {
				if (bmsc.getId() != 0) { // 등록인 경우 operator_bmsc에도 매핑시켜준다.
					OperatorBmscMapper operatorBmscMapper = sqlSession.getMapper(OperatorBmscMapper.class);
					syslogMap.put("reqType", "BM-SC Mgmt");
					syslogMap.put("reqSubType", "insertBmsc");
					syslogMap.put("reqUrl", "api/content.do");
					syslogMap.put("reqCode", "SUCCESS");
					syslogMap.put("reqMsg", "");
					usersMapper.insertSystemAjaxLog(syslogMap);
					return operatorBmscMapper.insertOperatorBmsc(bmsc) > 0;
				}
				return true;
			}
				
			return false;
		} catch (Exception e) {
			syslogMap.put("reqType", "BM-SC Mgmt");
			syslogMap.put("reqSubType", "insertBmsc");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	/**
	 * BMSC 삭제
	 */
	@Override
	public boolean deleteBmsc(Integer bmscId) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			OperatorBmscMapper operatorBmscMapper = sqlSession.getMapper(OperatorBmscMapper.class);
			operatorBmscMapper.deleteOperatorBmscOfBmsc(bmscId); // operator_bmsc에서도 제거해준다.
			syslogMap.put("reqType", "BM-SC Mgmt");
			syslogMap.put("reqSubType", "deleteBmsc");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return bmscMapper.deleteBmsc(bmscId) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "BM-SC Mgmt");
			syslogMap.put("reqSubType", "deleteBmsc");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	@Override
	public boolean insertEmbms(Embms embms) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			
			if (bmscMapper.insertEmbms(embms) > 0) {
				syslogMap.put("reqType", "eNB Mgmt");
				syslogMap.put("reqSubType", "embmsInsert");
				syslogMap.put("reqUrl", "bmsc/embmsInsert.do");
				syslogMap.put("reqCode", "SUCCESS");
				syslogMap.put("reqMsg", "");
				usersMapper.insertSystemAjaxLog(syslogMap);
				return true;
			}
				
			return false;
		} catch (Exception e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "embmsInsert");
			syslogMap.put("reqUrl", "bmsc/embmsInsert.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	
	}

	@Override
	public boolean postEmbmsUpdate(Embms embms) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			
			if (bmscMapper.postEmbmsUpdate(embms) > 0) {
				syslogMap.put("reqType", "eNB Mgmt");
				syslogMap.put("reqSubType", "embmsUpdate");
				syslogMap.put("reqUrl", "bmsc/embmsUpdate.do");
				syslogMap.put("reqCode", "SUCCESS");
				syslogMap.put("reqMsg", "");
				usersMapper.insertSystemAjaxLog(syslogMap);
				return true;
			}
				
			return false;
		} catch (Exception e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "embmsUpdate");
			syslogMap.put("reqUrl", "bmsc/embmsUpdate.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	
	}

	@Override
	public boolean deleteEmbms(Integer embmsId) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "embmsDel");
			syslogMap.put("reqUrl", "bmsc/embmsDel.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return bmscMapper.deleteEmbms(embmsId) > 0;
		} catch (Exception e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "embmsDel");
			syslogMap.put("reqUrl", "bmsc/embmsDel.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}
}

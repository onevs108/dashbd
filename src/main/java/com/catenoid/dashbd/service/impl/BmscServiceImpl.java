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
		map.put("start", offset);
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
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			return bmscMapper.selectBmsc(bmscId);
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return null;
		}
	}

	/**
	 * BMSC 등록 or 수정
	 */
	@Override
	public boolean insertBmsc(Bmsc bmsc) {
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			
			if (bmscMapper.insertBmsc(bmsc) > 0) {
				if (bmsc.getId() != 0) { // 등록인 경우 operator_bmsc에도 매핑시켜준다.
					OperatorBmscMapper operatorBmscMapper = sqlSession.getMapper(OperatorBmscMapper.class);
					return operatorBmscMapper.insertOperatorBmsc(bmsc) > 0;
				}
				return true;
			}
				
			return false;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	/**
	 * BMSC 삭제
	 */
	@Override
	public boolean deleteBmsc(Integer bmscId) {
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			OperatorBmscMapper operatorBmscMapper = sqlSession.getMapper(OperatorBmscMapper.class);
			operatorBmscMapper.deleteOperatorBmscOfBmsc(bmscId); // operator_bmsc에서도 제거해준다.
			return bmscMapper.deleteBmsc(bmscId) > 0;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}

	@Override
	public boolean insertEmbms(Embms embms) {
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			
			if (bmscMapper.insertEmbms(embms) > 0) {
				return true;
			}
				
			return false;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	
	}

	@Override
	public boolean deleteEmbms(Integer embmsId) {
		try {
			BmscMapper bmscMapper = sqlSession.getMapper(BmscMapper.class);
			return bmscMapper.deleteEmbms(embmsId) > 0;
		} catch (Exception e) {
			logger.error("~~ An error occurred!", e);
			return false;
		}
	}
}

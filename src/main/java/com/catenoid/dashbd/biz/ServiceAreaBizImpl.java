package com.catenoid.dashbd.biz;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.model.ServiceArea;

/**
 * Handles requests for the application Service Area Management.
 */

public class ServiceAreaBizImpl implements ServiceAreaBiz {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<ServiceArea> getServiceAreaByBmSc(String searchParam)
	{
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);

		List<ServiceArea> result = mapper.getSeviceAreaByBmSc(searchParam);
		
		return result;
	}
	
}

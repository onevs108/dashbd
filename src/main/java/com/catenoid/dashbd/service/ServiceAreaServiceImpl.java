package com.catenoid.dashbd.service;

import java.util.List;

import com.catenoid.dashbd.biz.ServiceAreaBiz;
import com.catenoid.dashbd.biz.ServiceAreaBizImpl;
import com.catenoid.dashbd.dao.model.ServiceArea;

/**
 * Handles requests for the application Service Area Management.
 */

public class ServiceAreaServiceImpl implements ServiceAreaService {
	
	private ServiceAreaBiz biz;
	
	public List<ServiceArea> getServiceAreaByBmSc(String searchParam)
	{
		try 
		{
			biz = new ServiceAreaBizImpl();
			List<ServiceArea> result = biz.getServiceAreaByBmSc(searchParam);
			
			return result;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
}

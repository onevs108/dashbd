package com.catenoid.dashbd.biz;

import java.util.List;

import com.catenoid.dashbd.dao.model.ServiceArea;

/**
 * Handles requests for the application Service Area Management.
 */
public interface ServiceAreaBiz {
	
	public List<ServiceArea> getServiceAreaByBmSc(String searchParam);
	
}

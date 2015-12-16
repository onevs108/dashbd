package com.catenoid.dashbd.service;

import java.util.List;

import com.catenoid.dashbd.dao.model.ServiceArea;

/**
 * Handles requests for the application Service Area Management.
 */
public interface ServiceAreaService  {
	
	public List<ServiceArea> getServiceAreaByBmSc(String searchParam);
	
}

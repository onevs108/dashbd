package com.catenoid.dashbd.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.Embms;

@Service
public interface BmscService {
	
	public List<Bmsc> getBmscList(Integer operatorId, String sort, String order, long offset, long limit);
	public int getBmscListCount(Integer operatorId);
	public JSONArray getBmscListToJsonArray(Integer operatorId, String sort, String order, long offset, long limit);
	public Bmsc getBmsc(Integer bmscId);
	public boolean insertBmsc(Bmsc bmsc);
	public boolean deleteBmsc(Integer bmscId);
	
	public boolean insertEmbms(Embms bmsc);
	public boolean postEmbmsUpdate(Embms bmsc);
	public boolean deleteEmbms(Integer embmsId);
	
}

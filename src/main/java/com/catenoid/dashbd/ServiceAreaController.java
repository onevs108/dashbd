package com.catenoid.dashbd;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.dao.ServiceAreaEnbApMapper;
import com.catenoid.dashbd.dao.ServiceAreaScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.model.BmscServiceArea;
import com.catenoid.dashbd.dao.model.BmscServiceAreaSearchParam;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbApExample;
import com.catenoid.dashbd.dao.model.ServiceAreaSchedule;
import com.catenoid.dashbd.dao.model.ServiceAreaScheduleExample;
import com.catenoid.dashbd.dao.model.ServiceAreaSearchParam;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class ServiceAreaController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceAreaController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "api/service_area.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doServiceArea(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readServiceAreaByID(request);
		}
		else if(request_type.equalsIgnoreCase("read_enb_ap")) {
			return readEnbAp(request);
		}
		else if(request_type.equalsIgnoreCase("read_range_enb_ap")) {
			return readRangeEnbAp(request);
		}
		else if(request_type.equalsIgnoreCase("read_schedule")) {
			return readServiceAreaSchedule(request);
		}
		else if(request_type.equalsIgnoreCase("create")) {
			return createServiceArea(request);
		}
		else if(request_type.equalsIgnoreCase("update_metadata")) {
			return updateServiceAreaByID(request);
		}
		else if(request_type.equalsIgnoreCase("delete")) {
			return deleteServiceAreaByID(request);
		}
		else if(request_type.equalsIgnoreCase("add_enb")) {
			return addServiceAreaAtEnbAp(request);
		}
		else if(request_type.equalsIgnoreCase("remove_enb")) {
			return removeServiceAreaAtEnbAp(request);
		}
		else if(request_type.equalsIgnoreCase("add_schedule")) {
			return addServiceAreaAtSchedule(request);
		}
		else if(request_type.equalsIgnoreCase("remove_schedule")) {
			return removeServiceAreaAtSchedule(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readServiceAreaByID(HttpServletRequest request) {
		try {			
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			ServiceArea data = mapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(data == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("id", data.getId());
			obj.put("bandwidth", data.getBandwidth());
			obj.put("name", data.getName());
			obj.put("city", data.getCity());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			root.put("result", obj);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> readEnbAp(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			ServiceAreaEnbApExample exm = new ServiceAreaEnbApExample();
			exm.and().andServiceAreaIdEqualTo(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("plmn"))) exm.and().andPlmnEqualTo(request.getParameter("plmn"));
			if(valueCheck(request.getParameter("mbsfn"))) {
				String[] mbsfns = request.getParameter("mbsfn").split(",");
				for(int i = 0; i < mbsfns.length; i++) {
					String mbsfn = mbsfns[i];
					if(mbsfn.trim().length() == 0) continue;
					exm.and().andMbsfnLike("%," + mbsfn + ",%");
				}
			}
			exm.setOrderByClause("longitude, latitude asc");
			List<ServiceAreaEnbAp> datas = mapper.selectServiceAreaEnbAp(exm);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					ServiceAreaEnbAp data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getServiceAreaId());
					obj.put("bandwidth", data.getServiceAreaBandwidth());
					obj.put("name", data.getServiceAreaName());
					obj.put("city", data.getServiceAreaCity());
					obj.put("enb_ap_id", data.getEnbApId());
					obj.put("enb_ap_name", data.getEnbApName());
					obj.put("longitude", data.getLongitude());
					obj.put("latitude", data.getLatitude());
					obj.put("plmn", data.getPlmn());
					String mbsfn = data.getMbsfn();
					if(mbsfn.startsWith(",")) mbsfn = mbsfn.substring(1);
					if(mbsfn.endsWith(",")) mbsfn = mbsfn.substring(0, mbsfn.length() - 1);
					obj.put("mbsfn", mbsfn);
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				ServiceAreaEnbAp data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getServiceAreaId());
				obj.put("bandwidth", data.getServiceAreaBandwidth());
				obj.put("name", data.getServiceAreaName());
				obj.put("city", data.getServiceAreaCity());
				obj.put("enb_ap_id", data.getEnbApId());
				obj.put("enb_ap_name", data.getEnbApName());
				obj.put("longitude", data.getLongitude());
				obj.put("latitude", data.getLatitude());
				obj.put("plmn", data.getPlmn());
				String mbsfn = data.getMbsfn();
				if(mbsfn.startsWith(",")) mbsfn = mbsfn.substring(1);
				if(mbsfn.endsWith(",")) mbsfn = mbsfn.substring(0, mbsfn.length() - 1);
				obj.put("mbsfn", mbsfn);
				obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
				
				root.put("result", obj);
			}
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> readRangeEnbAp(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("lt_longitude"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("lt_latitude"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("rb_longitude"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("rb_latitude"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			ServiceAreaEnbApExample exm = new ServiceAreaEnbApExample();
			exm.and().andLongitudeBetween(BigDecimal.valueOf(Double.parseDouble(request.getParameter("lt_longitude"))), BigDecimal.valueOf(Double.parseDouble(request.getParameter("rb_longitude"))));
			exm.and().andLatitudeBetween(BigDecimal.valueOf(Double.parseDouble(request.getParameter("rb_latitude"))), BigDecimal.valueOf(Double.parseDouble(request.getParameter("lt_latitude"))));
			if(valueCheck(request.getParameter("plmn"))) exm.and().andPlmnEqualTo(request.getParameter("plmn"));
			if(valueCheck(request.getParameter("mbsfn"))) {
				String[] mbsfns = request.getParameter("mbsfn").split(",");
				for(int i = 0; i < mbsfns.length; i++) {
					String mbsfn = mbsfns[i];
					if(mbsfn.trim().length() == 0) continue;
					exm.and().andMbsfnLike("%," + mbsfn + ",%");
				}
			}
			exm.setOrderByClause("longitude, latitude asc");
			List<ServiceAreaEnbAp> datas = mapper.selectServiceAreaEnbApRangeOuterJoin(exm);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					ServiceAreaEnbAp data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getServiceAreaId());
					obj.put("bandwidth", data.getServiceAreaBandwidth());
					obj.put("name", data.getServiceAreaName());
					obj.put("city", data.getServiceAreaCity());
					obj.put("enb_ap_id", data.getEnbApId());
					obj.put("enb_ap_name", data.getEnbApName());
					obj.put("longitude", data.getLongitude());
					obj.put("latitude", data.getLatitude());
					obj.put("plmn", data.getPlmn());
					String mbsfn = data.getMbsfn();
					if(mbsfn.startsWith(",")) mbsfn = mbsfn.substring(1);
					if(mbsfn.endsWith(",")) mbsfn = mbsfn.substring(0, mbsfn.length() - 1);
					obj.put("mbsfn", mbsfn);
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				ServiceAreaEnbAp data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getServiceAreaId());
				obj.put("bandwidth", data.getServiceAreaBandwidth());
				obj.put("name", data.getServiceAreaName());
				obj.put("city", data.getServiceAreaCity());
				obj.put("enb_ap_id", data.getEnbApId());
				obj.put("enb_ap_name", data.getEnbApName());
				obj.put("longitude", data.getLongitude());
				obj.put("latitude", data.getLatitude());
				obj.put("plmn", data.getPlmn());
				String mbsfn = data.getMbsfn();
				if(mbsfn.startsWith(",")) mbsfn = mbsfn.substring(1);
				if(mbsfn.endsWith(",")) mbsfn = mbsfn.substring(0, mbsfn.length() - 1);
				obj.put("mbsfn", mbsfn);
				obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
				
				root.put("result", obj);
			}
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> readServiceAreaSchedule(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {			
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("start_date"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("end_date"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaSchedule record = new ServiceAreaSchedule();
			record.setServiceAreaId(Integer.parseInt(request.getParameter("id")));		
			try {
				record.setStartDate(getFormatDateTime(request.getParameter("start_date"), "yyyy-MM-dd HH:mm:ss"));
				record.setEndDate(getFormatDateTime(request.getParameter("end_date"), "yyyy-MM-dd HH:mm:ss"));
			}
			catch (ParseException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			ServiceAreaScheduleMapper mapper = sqlSession.getMapper(ServiceAreaScheduleMapper.class);			
			List<ServiceAreaSchedule> datas = mapper.selectServiceAreaSchedule(record);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					ServiceAreaSchedule data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getServiceAreaId());
					obj.put("schedule_id", data.getScheduleId());
					obj.put("start_date", getFormatDateTime(data.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("end_date", getFormatDateTime(data.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				ServiceAreaSchedule data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getServiceAreaId());
				obj.put("schedule_id", data.getScheduleId());
				obj.put("start_date", getFormatDateTime(data.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("end_date", getFormatDateTime(data.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
				
				root.put("result", obj);
			}
			
//			final HttpHeaders httpHeaders= new HttpHeaders();
//		    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//			return new ResponseEntity<String>(root.toJSONString(), httpHeaders, HttpStatus.OK);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> createServiceArea(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("bandwidth"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("name"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			ServiceArea record = new ServiceArea();
			if(valueCheck(request.getParameter("id"))) record.setId(Integer.parseInt(request.getParameter("id")));		
			record.setBandwidth(Integer.parseInt(request.getParameter("bandwidth")));
			record.setName(request.getParameter("name"));
			if(valueCheck(request.getParameter("city"))) record.setCity(request.getParameter("city"));
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			if(valueCheck(request.getParameter("id"))) obj.put("id", Integer.parseInt(request.getParameter("id")));
			obj.put("id", record.getId());
			root.put("result", obj);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
			root.put("code", ErrorCodes.DATA_DUPLICATION.getCode());
			root.put("message", ErrorCodes.DATA_DUPLICATION.getMsg());
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> updateServiceAreaByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			ServiceArea record = new ServiceArea();
			record.setId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("bandwidth"))) record.setBandwidth(Integer.parseInt(request.getParameter("bandwidth")));
			if(valueCheck(request.getParameter("name"))) record.setName(request.getParameter("name"));
			if(valueCheck(request.getParameter("city"))) record.setCity(request.getParameter("city"));
			int res = mapper.updateByPrimaryKeySelective(record);
			logger.info("UPDATE RESULT: " + res);
			if(res == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
			root.put("code", ErrorCodes.DATA_DUPLICATION.getCode());
			root.put("message", ErrorCodes.DATA_DUPLICATION.getMsg());
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> deleteServiceAreaByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);				
			int res = mapper.deleteByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> addServiceAreaAtEnbAp(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("enb_ap_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaEnbApMapper mapper = sqlSession.getMapper(ServiceAreaEnbApMapper.class);			
			ServiceAreaEnbApExample exm = new ServiceAreaEnbApExample();
			exm.and().andEnbApIdEqualTo(Integer.parseInt(request.getParameter("enb_ap_id")));
			List<ServiceAreaEnbAp> datas = mapper.selectByExample(exm);
			if(datas == null || datas.size() == 0) {
				ServiceAreaEnbAp record = new ServiceAreaEnbAp();
				record.setEnbApId(Integer.parseInt(request.getParameter("enb_ap_id")));
				record.setServiceAreaId(Integer.parseInt(request.getParameter("id")));
				int res = mapper.insertSelective(record);
				logger.info("INSERT RESULT: " + res);
			}
			else {
				/// 다른 서비스에어리어에 묶여있는 기지국(enb_ap)일 경우 수신된 서비스에어리어로 재 mapping한다.
				ServiceAreaEnbAp record = new ServiceAreaEnbAp();
				record.setServiceAreaId(Integer.parseInt(request.getParameter("id")));
				int res = mapper.updateByExampleSelective(record, exm);
				logger.info("UPDATE RESULT: " + res);
			}
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("id", Integer.parseInt(request.getParameter("id")));
			root.put("result", obj);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
			root.put("code", ErrorCodes.DATA_DUPLICATION.getCode());
			root.put("message", ErrorCodes.DATA_DUPLICATION.getMsg());			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}		
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> removeServiceAreaAtEnbAp(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("enb_ap_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaEnbApMapper mapper = sqlSession.getMapper(ServiceAreaEnbApMapper.class);
			ServiceAreaEnbApExample exm = new ServiceAreaEnbApExample();
			exm.createCriteria().andServiceAreaIdEqualTo(Integer.parseInt(request.getParameter("id")));
			exm.createCriteria().andEnbApIdEqualTo(Integer.parseInt(request.getParameter("enb_ap_id")));
			int res = mapper.deleteByExample(exm);
			logger.info("DELETE RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}		
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> addServiceAreaAtSchedule(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("schedule_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaScheduleMapper mapper = sqlSession.getMapper(ServiceAreaScheduleMapper.class);
			ServiceAreaSchedule record = new ServiceAreaSchedule();
			record.setServiceAreaId(Integer.parseInt(request.getParameter("id")));
			record.setScheduleId(Integer.parseInt(request.getParameter("schedule_id")));
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("id", Integer.parseInt(request.getParameter("id")));
			root.put("result", obj);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
			root.put("code", ErrorCodes.DATA_DUPLICATION.getCode());
			root.put("message", ErrorCodes.DATA_DUPLICATION.getMsg());
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> removeServiceAreaAtSchedule(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("schedule_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceAreaScheduleMapper mapper = sqlSession.getMapper(ServiceAreaScheduleMapper.class);
			ServiceAreaScheduleExample exm = new ServiceAreaScheduleExample();
			exm.createCriteria().andServiceAreaIdEqualTo(Integer.parseInt(request.getParameter("id")));
			exm.createCriteria().andScheduleIdEqualTo(Integer.parseInt(request.getParameter("schedule_id")));
			int res = mapper.deleteByExample(exm);
			logger.info("DELETE RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}		
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public static Map<String, Object> parameterToMap(HttpServletRequest request) {
		Map<String, Object> prmtMap = new HashMap<String, Object>();
		Enumeration<?> e = request.getParameterNames();
		String prmtNm = null;
		String[] prmtValues = null;
		while(e.hasMoreElements()) {
			prmtNm = (String) e.nextElement();
			prmtValues = request.getParameterValues(prmtNm);
			try {
				if(prmtValues.length > 1) {
					for(int i = 0; i < prmtValues.length; i++) {
						prmtValues[i] = URLDecoder.decode((prmtValues[i].replaceAll("\\%",  "%25")).replaceAll("\\+", "%2B"), "UTF-8");
						if("".equals(prmtValues[i])) {
							prmtValues[i] = null;
						}
					}
					prmtMap.put(prmtNm,  prmtValues);
				} else {
					if("".equals(prmtValues[0])) {
						prmtMap.put(prmtNm, null);
					} else {
						prmtMap.put(prmtNm, URLDecoder.decode((prmtValues[0].replaceAll("\\%", "%25")).replaceAll("\\+", "%2B"), "UTF-8"));
					}					
				}
			}
			catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
		
		return prmtMap;
	}
	
	private String getFormatDateTime(Date date, String format) {
		if(date == null) return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	private Date getFormatDateTime(String date, String format) throws ParseException{		
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		try {
			return sdFormat.parse(date);
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	private boolean valueCheck(String parameter) {
		// TODO Auto-generated method stub
		if(parameter == null || parameter.trim().equals("")) return false;
		return true;
	}
	
	@RequestMapping(value = "/resources/serviceArea.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView getServiceAreaMainOperator(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/service_area_mgmt");
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage(1);
		searchParam.setPerPage(15);
		
		List<Operator> result = mapper.getServiceAreaOperator(searchParam);
		
		mv.addObject("OperatorList", result);
		
		return mv;
	}
	
	@RequestMapping(value = "/resources/api/serviceAreaMainBmSc.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView getServiceAreaMainBmSc(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/service_area_mgmt");
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		BmscServiceAreaSearchParam searchParam = new BmscServiceAreaSearchParam();
		searchParam.setPage(1);
		searchParam.setPerPage(15);
		searchParam.setBmscId(2003);
		
		List<BmscServiceArea> result = mapper.getSeviceAreaByBmSc(searchParam);
		
		mv.addObject("BmcServiceAreaList", result);
		
		return mv;
	}
}

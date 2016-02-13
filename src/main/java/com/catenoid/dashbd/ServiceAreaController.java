package com.catenoid.dashbd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.dao.ServiceAreaEnbApMapper;
import com.catenoid.dashbd.dao.ServiceAreaScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.BmscServiceArea;
import com.catenoid.dashbd.dao.model.BmscServiceAreaSearchParam;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.ScheduleSummary;
import com.catenoid.dashbd.dao.model.ScheduleSummarySearchParam;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.dao.model.ServiceAreaCount;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbApExample;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbSearchParam;
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
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	
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
	public ModelAndView getServiceAreaMain(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("serviceAreaMain");
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		
		List<Operator> result = mapper.getServiceAreaOperator(searchParam);
		Operator initOperator = result.get(0);
		
		searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		searchParam.setOperatorId(initOperator.getId());
		
		List<Bmsc> bmscs = mapper.getSeviceAreaBmSc(searchParam);
		
		mv.addObject("OperatorList", result);
		mv.addObject("BmscList", bmscs);
		
		return mv;
	}
	
	@RequestMapping(value = "/api/serviceAreaBmScByOperator.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaBmScByOperator(HttpServletRequest request, HttpServletResponse response) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 15;
		
		if(request.getParameter("operatorId") == null) return;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		searchParam.setOperatorId(Integer.valueOf(request.getParameter("operatorId")));
		
		List<Bmsc> datas = mapper.getSeviceAreaBmSc(searchParam);
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			Bmsc data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", data.getId());
			obj.put("name", data.getName());
			obj.put("circle", data.getCircle());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/serviceAreaByBmSc.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaMainBmSc(HttpServletRequest request, HttpServletResponse response) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		if(request.getParameter("bmscId") == null) return;
		
		BmscServiceAreaSearchParam searchParam = new BmscServiceAreaSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		searchParam.setBmscId(Integer.valueOf(request.getParameter("bmscId")));
		
		List<BmscServiceArea> datas = mapper.getSeviceAreaByBmSc(searchParam);
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			BmscServiceArea data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("bmscId", data.getBmscId());
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("totalCount", data.getTotalCount());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@RequestMapping(value = "/resources/serviceAreaMgmt.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView getServiceAreaMgmt(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("serviceAreaMgmt");
		
		if(request.getParameter("serviceAreaId") == null) return null;
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap<String, Object> searchParam = new HashMap();
		searchParam.put("serviceAreaId", Integer.valueOf(request.getParameter("serviceAreaId")));
		
		List<ServiceAreaEnbAp> datas = mapper.getServiceAreaEnbAp(searchParam);
		
		mv.addObject("EnbList", datas);
		
		return mv;
	}
	
	@RequestMapping(value = "/api/getServiceAreaEnbAp.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaEnbAp(HttpServletRequest request, HttpServletResponse response) {

		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap<String, Object> searchParam = new HashMap();
		if(request.getParameter("serviceAreaId") != null)
		{
			searchParam.put("serviceAreaId", Integer.valueOf(request.getParameter("serviceAreaId")));
		}
		
		if(request.getParameter("bmscId") != null)
		{
			searchParam.put("bmscId", Integer.valueOf(request.getParameter("bmscId")));
		}
		
		List<ServiceAreaEnbAp> datas = mapper.getServiceAreaEnbAp(searchParam);
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			ServiceAreaEnbAp data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("serviceAreaBandwidth", data.getServiceAreaBandwidth());
			obj.put("serviceAreaName", data.getServiceAreaName());
			obj.put("serviceAreaCity", data.getServiceAreaCity());
			obj.put("enbApId", data.getEnbApId());
			obj.put("enbApName", data.getEnbApName());
			obj.put("longitude", data.getLongitude());
			obj.put("latitude", data.getLatitude());
			obj.put("plmn", data.getPlmn());
			obj.put("mbsfn", data.getMbsfn());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("totalCount", data.getTotalCount());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	    	System.out.println("Exception=[" + e.toString() + "]");
	        e.printStackTrace();
	    }
	}
	
	/**
	 * ENB 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/getEnbsList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getEnbsList(@RequestBody String body) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String operatorIdToStr = (String) requestJson.get("operatorId");
			String bmscIdToStr = (String) requestJson.get("bmscId");
			Integer operatorId = operatorIdToStr == null ? 0 : Integer.parseInt(operatorIdToStr);
			Integer bmscId = bmscIdToStr == null ? 0 : Integer.parseInt(bmscIdToStr);
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			if (sort == null || sort.isEmpty()) sort = null;
			if (order == null || order.isEmpty()) order = null;
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			HashMap<String, Object> searchParam = new HashMap();
			searchParam.put("operatorId", operatorId);
			searchParam.put("bmscId", bmscId);
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset);
			searchParam.put("end", offset + limit);
			
			List<ServiceAreaEnbAp> datas = mapper.getEnbsList(searchParam);
			
			JSONArray rows = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				ServiceAreaEnbAp data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getEnbApId());
				obj.put("name", data.getEnbApName());
				obj.put("longitude", data.getLongitude());
				obj.put("latitude", data.getLatitude());
				obj.put("plmn", data.getPlmn());
				obj.put("mbsfn", data.getMbsfn());
				obj.put("city", data.getCity());
				obj.put("bandwidth", data.getBandwidth());
				obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("totalCount", data.getTotalCount());
				rows.add(obj);
				
				if( i == 0 ) {
					jsonResult.put("total", data.getTotalCount());
				}
			}
			
			jsonResult.put("rows", rows);

		} catch (Exception e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	@RequestMapping(value = "/api/getServiceAreaEnbApWithBounds.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaEnbApWithBounds(HttpServletRequest request, HttpServletResponse response) {

		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap<String, Object> searchParam = new HashMap();
		searchParam.put( "bmscId", Integer.valueOf( request.getParameter( "bmscId" ) ) );
		searchParam.put( "serviceAreaId", Integer.valueOf( request.getParameter( "serviceAreaId" ) ) );
		searchParam.put( "swLat", request.getParameter( "swLat" ) );
		searchParam.put( "swLng", request.getParameter( "swLng" ) );
		searchParam.put( "neLat", request.getParameter( "neLat" ) );
		searchParam.put( "neLng", request.getParameter( "neLng" ) );
		
		List<ServiceAreaEnbAp> datas = mapper.getServiceAreaEnbApWithBounds( searchParam );
		
		JSONArray array = new JSONArray();
		for( int i = 0; i < datas.size(); i++ ) {
			ServiceAreaEnbAp data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("serviceAreaBandwidth", data.getServiceAreaBandwidth());
			obj.put("serviceAreaName", data.getServiceAreaName());
			obj.put("serviceAreaCity", data.getServiceAreaCity());
			obj.put("enbApId", data.getEnbApId());
			obj.put("enbApName", data.getEnbApName());
			obj.put("longitude", data.getLongitude());
			obj.put("latitude", data.getLatitude());
			obj.put("plmn", data.getPlmn());
			obj.put("mbsfn", data.getMbsfn());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("totalCount", data.getTotalCount());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/getServiceAreaEnbApOther.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaEnbApOther(HttpServletRequest request, HttpServletResponse response) {

		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap<String, Object> searchParam = new HashMap();
		searchParam.put("serviceAreaId", Integer.valueOf(request.getParameter("serviceAreaId")));
		searchParam.put("swLat", request.getParameter("swLat"));
		searchParam.put("swLng", request.getParameter("swLng"));
		searchParam.put("neLat", request.getParameter("neLat"));
		searchParam.put("neLng", request.getParameter("neLng"));
		
		List<ServiceAreaEnbAp> datas = mapper.getServiceAreaEnbApOther(searchParam);
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			ServiceAreaEnbAp data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("serviceAreaBandwidth", data.getServiceAreaBandwidth());
			obj.put("serviceAreaName", data.getServiceAreaName());
			obj.put("serviceAreaCity", data.getServiceAreaCity());
			obj.put("enbApId", data.getEnbApId());
			obj.put("enbApName", data.getEnbApName());
			obj.put("longitude", data.getLongitude());
			obj.put("latitude", data.getLatitude());
			obj.put("plmn", data.getPlmn());
			obj.put("mbsfn", data.getMbsfn());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("totalCount", data.getTotalCount());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/resources/main.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView getMain(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("main");
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		
		List<Operator> result = mapper.getServiceAreaOperator(searchParam);
		
		mv.addObject("OperatorList", result);
		
		return mv;
	}
	
	@RequestMapping(value = "/api/serviceAreaByLatLng.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaByLatLng(HttpServletRequest request, HttpServletResponse response) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 15;
		
		ServiceAreaEnbSearchParam searchParam = new ServiceAreaEnbSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		searchParam.setLatitude(BigDecimal.valueOf(Long.valueOf(request.getParameter("lat"))));
		searchParam.setLongitude(BigDecimal.valueOf(Long.valueOf(request.getParameter("lng"))));
		
		List<BmscServiceArea> datas = mapper.getServiceAreaByLatLng(searchParam);
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			BmscServiceArea data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("bmscId", data.getBmscId());
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("totalCount", data.getTotalCount());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/getServiceAreaCountByBmSc.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaCountByBmSc(HttpServletRequest request, HttpServletResponse response) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		BmscServiceAreaSearchParam searchParam = new BmscServiceAreaSearchParam();
		searchParam.setBmscId(Integer.valueOf(request.getParameter("bmscId")));
		
		List<ServiceAreaCount> datas = mapper.getServiceAreaCountByBmSc(searchParam);
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			ServiceAreaCount data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("bmscId", data.getBmscId());
			obj.put("city", data.getCity());
			obj.put("count", data.getCount());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	@RequestMapping(value = "/api/serviceAreaByBmScCity.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaByBmscCity(HttpServletRequest request, HttpServletResponse response) {
		try {
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
			Integer perPage = 1000;
			
			if(request.getParameter("bmscId") == null) return;
	
			BmscServiceAreaSearchParam searchParam = new BmscServiceAreaSearchParam();
			searchParam.setPage((page-1) * perPage);
			searchParam.setPerPage(perPage);
			searchParam.setBmscId(Integer.valueOf(request.getParameter("bmscId")));
			searchParam.setServiceAreaCity(URLDecoder.decode(request.getParameter("city"), "utf-8"));
			
			List<BmscServiceArea> datas = mapper.getSeviceAreaByBmScCity(searchParam);
			
			JSONArray array = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				BmscServiceArea data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("bmscId", data.getBmscId());
				obj.put("serviceAreaId", data.getServiceAreaId());
				obj.put("serviceAreaName", data.getServiceAreaName());
				obj.put("totalCount", data.getTotalCount());
				array.add(obj);
			}

			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/scheduleSummaryByServiceArea.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getScheduleSummaryByServiceArea(HttpServletRequest request, HttpServletResponse response) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		//Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		//Integer perPage = 15;
		
		ScheduleSummarySearchParam searchParam = new ScheduleSummarySearchParam();
		searchParam.setServiceAreaId(Integer.valueOf(request.getParameter("serviceAreaId")));
		
		List<ScheduleSummary> datas = mapper.getScheduleSummaryByServiceArea(searchParam);

		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			ScheduleSummary data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("scheduleId", data.getScheduleId());
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("contentId", data.getContentId());
			obj.put("bcId", data.getBcId());
			obj.put("scheduleName", data.getScheduleName());
			obj.put("startDate", getFormatDateTime(data.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("endDate", getFormatDateTime(data.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("createdAt", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updatedAt", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("delYn", data.getDelYn());
			obj.put("thumbnail", data.getThumbnail());
			obj.put("progressRate", data.getProgressRate());
			obj.put("serviceType", data.getServiceType());
			obj.put("category", data.getCategory());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/scheduleSummaryByBmsc.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getScheduleSummaryByBmsc(HttpServletRequest request, HttpServletResponse response) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		//Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		//Integer perPage = 15;
		
		HashMap<String, Integer> searchParam = new HashMap();
		searchParam.put("bmscId", Integer.valueOf(request.getParameter("bmscId")));
		
		List<ScheduleSummary> datas = mapper.getScheduleSummaryByBmsc(searchParam);

		JSONArray array = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			ScheduleSummary data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("scheduleId", data.getScheduleId());
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("contentId", data.getContentId());
			obj.put("bcId", data.getBcId());
			obj.put("scheduleName", data.getScheduleName());
			obj.put("startDate", getFormatDateTime(data.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("endDate", getFormatDateTime(data.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("createdAt", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updatedAt", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("delYn", data.getDelYn());
			obj.put("thumbnail", data.getThumbnail());
			obj.put("progressRate", data.getProgressRate());
			obj.put("serviceType", data.getServiceType());
			obj.put("category", data.getCategory());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/bandwidthByServiceArea.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getScheduleGBRSum(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 	현재시간 기점으로 GBR 합산 한 값을 리턴하는 메소드 호출하는 샘플코드 
		 */
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap< String, Integer > searchParam = new HashMap();
		if( request.getParameter("bmscId") != null ) {
			searchParam.put("bmscId", Integer.valueOf(request.getParameter("bmscId")));
		}
		
		searchParam.put("serviceAreaId", Integer.valueOf(request.getParameter("serviceAreaId")));
		
		HashMap retmap = mapper.getGBRSum(searchParam);
//System.out.println("==================================>>>>>" + request.getParameter("bmscId"));
		JSONObject obj = new JSONObject();
		if(retmap != null) {
			obj.put("GBRSum", retmap.get("GBRSum"));
		} else {
			obj.put("GBRSum", 0);
		}

		try {
	        response.getWriter().print(obj.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/addToServiceArea.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void addToServiceArea(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap< String, Integer > searchParam;
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer serviceAreaId = Integer.valueOf(request.getParameter("serviceAreaId"));
		int addCount = 0;
		System.out.println(serviceAreaId);
		
		String[] enbIds = request.getParameterValues("enbIds[]");
		for( int i = 0; enbIds != null && i < enbIds.length; i++) {
			searchParam = new HashMap();
			searchParam.put("serviceAreaId", serviceAreaId );
			searchParam.put("enbApId", Integer.valueOf(enbIds[i]));
			System.out.println(Integer.valueOf(enbIds[i]));
			addCount = mapper.addToServiceArea(searchParam);
		}
		
		return;
	}
	
	@RequestMapping(value = "/api/addToServiceAreaManually.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void addToServiceAreaManually(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap< String, Integer > searchParam;
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer serviceAreaId = Integer.valueOf(request.getParameter("serviceAreaId"));
		int addCount = 0;
		//System.out.println(serviceAreaId);
		
		String enbIds = request.getParameter("enbIds");
		
		String[] enbId = enbIds.split( "," );
		
		for( int i = 0; i < enbId.length; i++ ) {
			if( enbId[i].contains( "-" ) || enbId[i].contains( "~" ) ) {
				String[] fromToEnbId = null;
				if( enbId[i].contains( "-" ) ) {
					fromToEnbId = enbId[i].split( "-" );
				} else if( enbId[i].contains( "~" ) ) {
					fromToEnbId = enbId[i].split( "~" );
				}
				
				if( fromToEnbId != null && fromToEnbId.length == 2 ) {
					int fromIdx = Integer.valueOf(fromToEnbId[0].trim()).intValue();
					int toIdx = Integer.valueOf(fromToEnbId[1].trim()).intValue();
					for( int enb = fromIdx; enb <= toIdx; enb++ ) {
						//System.out.println( "enbApId=[" + enb + "]" );
						searchParam = new HashMap();
						searchParam.put("serviceAreaId", serviceAreaId );
						searchParam.put("enbApId", Integer.valueOf(enb));
						addCount += mapper.addToServiceArea(searchParam);
					}
				}
			} else {
				//System.out.println( "enbApId=[" + enbId[i].trim() + "]" );
				
				searchParam = new HashMap();
				searchParam.put("serviceAreaId", serviceAreaId );
				searchParam.put("enbApId", Integer.valueOf(enbId[i].trim()));
				addCount += mapper.addToServiceArea(searchParam);

			}
		}

		return;
	}
	
	@RequestMapping(value = "/api/deleteFromServiceArea.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void deleteFromServiceArea(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap< String, Object > searchParam;
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer serviceAreaId = Integer.valueOf(request.getParameter("serviceAreaId"));
		int delCount = 0;
		System.out.println(serviceAreaId);
		
		String[] enbIds = request.getParameterValues("enbIds[]");
		Integer[] enbApIds = new Integer[enbIds.length];
		for(int i = 0; i < enbIds.length; i++) {
			enbApIds[i] = Integer.valueOf(enbIds[i]);
		}
		searchParam = new HashMap();
		searchParam.put("serviceAreaId", serviceAreaId );
		searchParam.put("enbApIds", enbApIds);
		delCount = mapper.deleteFromServiceArea(searchParam);
		
		return;
	}
	
	@RequestMapping(value = "/resources/eNBMgmt.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView eNBMgmtView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("eNBMgmt");
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		
		List<Operator> result = mapper.getServiceAreaOperator(searchParam);
		
		mv.addObject("OperatorList", result);
		
		return mv;
	}

	@RequestMapping(value = "/resources/eNBsExcelMgmt.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView eNBsExcelMgmtView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/enb/eNBsExcelMgmt");
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		
		List<Operator> result = mapper.getServiceAreaOperator(searchParam);
		
		mv.addObject("OperatorList", result);
		
		return mv;
	}
	
	@RequestMapping(value = "/api/createServiceArea.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void createServiceArea(HttpServletRequest request, HttpServletResponse response) {

		try {
			Integer bmscId = Integer.valueOf(request.getParameter("bmscId"));
			Integer serviceAreaId = Integer.valueOf(request.getParameter("serviceAreaId"));
			Integer bandwidth = request.getParameter("serviceAreaBandwidth") == null ? 0 : Integer.valueOf(request.getParameter("serviceAreaBandwidth"));
			String name = URLDecoder.decode(request.getParameter("serviceAreaName"), "UTF-8");
			String description = URLDecoder.decode(request.getParameter("serviceAreaDescription"));
			String city = request.getParameter("serviceAreaCity");
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			HashMap< String, Object > searchParam = new HashMap();
			searchParam.put("serviceAreaId", serviceAreaId);
			searchParam.put("bandwidth", bandwidth);
			searchParam.put("name", name);
			searchParam.put("description", description);
			searchParam.put("city", city);
			
			System.out.println( "name=" + name );
			int rst = mapper.createServiceArea(searchParam);
			
			if( rst == 1 ) {
				searchParam = new HashMap();
				searchParam.put("bmscId", bmscId);
				searchParam.put("serviceAreaId", serviceAreaId);
				
				rst = mapper.createBmScServiceArea(searchParam);
			}
	
			JSONObject obj = new JSONObject();
			obj.put("count", rst);

		
	        response.getWriter().print(obj.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/createENB.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void createENB(HttpServletRequest request, HttpServletResponse response) {

		try {
			Integer operator = Integer.valueOf( request.getParameter( "operator" ) );
			Integer bmsc = Integer.valueOf( request.getParameter( "bmsc" ) );
			Integer id = Integer.valueOf( request.getParameter( "id" ) );
			String name = request.getParameter( "name" );
			Double longitude = 0.0;
			if( request.getParameter( "longitude" ) != null && request.getParameter( "longitude" ).length() > 0 ) {
				longitude = Double.valueOf( request.getParameter( "longitude" ) );
			}
			Double latitude = 0.0;
			if( request.getParameter( "latitude" ) != null && request.getParameter( "latitude" ).length() > 0 ) {
				latitude = Double.valueOf( request.getParameter( "latitude" ) );
			}
			String plmn = request.getParameter( "plmn" );
			String circle = request.getParameter( "circle" );
			String circleName = request.getParameter( "circleName" );
			Integer clusterId = 0;
			if( request.getParameter( "clusterId" ) != null && request.getParameter( "clusterId" ).length() > 0 ) {
				clusterId = Integer.valueOf( request.getParameter( "clusterId" ) );
			}
			String ipAddress = request.getParameter( "ipaddress" );
			String earfcn = request.getParameter( "earfcn" );
			String mbsfn = request.getParameter( "mbsfn" );
			Integer mbmsServiceAreaId = 0;
			if( request.getParameter( "mbmsServiceAreaId" ) != null && request.getParameter( "mbmsServiceAreaId" ).length() > 0 ) {
				mbmsServiceAreaId = Integer.valueOf( request.getParameter( "mbmsServiceAreaId" ) );
			}
			String city = request.getParameter( "city" );
			Integer bandwidth = 0;
			if( request.getParameter( "bandwidth" ) != null && request.getParameter( "bandwidth" ).length() > 0 ) {
				bandwidth = Integer.valueOf( request.getParameter( "bandwidth" ) );
			}
			
			//System.out.println( operator );
			//System.out.println( bmsc );
			
			int rst = 0;
			ServiceAreaMapper mapper = sqlSession.getMapper( ServiceAreaMapper.class );
			
			HashMap< String, Object > searchParam = new HashMap< String, Object >();
			searchParam.put( "id", id );
			searchParam.put( "name", name );
			searchParam.put( "longitude", longitude );
			searchParam.put( "latitude", latitude );
			searchParam.put( "plmn", plmn );
			searchParam.put( "circle", circle );
			searchParam.put( "circleName", circleName );
			searchParam.put( "clusterId", clusterId );
			searchParam.put( "ipAddress", ipAddress );
			searchParam.put( "earfcn", earfcn );
			searchParam.put( "mbsfn", mbsfn );
			searchParam.put( "mbmsServiceAreaId", mbmsServiceAreaId );
			searchParam.put( "city", city );
			searchParam.put( "bandwidth", bandwidth );
			searchParam.put( "operator", operator );
			searchParam.put( "bmsc", bmsc );
			
			rst += mapper.createENBs( searchParam );
			
			JSONObject obj = new JSONObject();
			obj.put( "count", rst );

	        response.getWriter().print(obj.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/api/uploadENBs.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void uploadENBs(MultipartHttpServletRequest request, HttpServletResponse response) {

		try {
			Integer operator = Integer.valueOf( request.getParameter( "operator" ) );
			Integer bmsc = Integer.valueOf( request.getParameter( "bmsc" ) );
			
			//System.out.println( operator );
			//System.out.println( bmsc );
			
			List list = excelFileUpload( request );
			int rst = 0;
			
			for( int i = 0; i < list.size(); i++ ) {
				HashMap< String, Object > searchParam = (HashMap< String, Object >) list.get( i );
				ServiceAreaMapper mapper = sqlSession.getMapper( ServiceAreaMapper.class );
				searchParam.put( "operator", operator );
				searchParam.put( "bmsc", bmsc );
				
				//System.out.println("i=[" + i + "] [" + searchParam.get("id") + "]");
				rst += mapper.createENBs( searchParam );
			}
			
			JSONObject obj = new JSONObject();
			obj.put( "count", rst );

	        response.getWriter().print(obj.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
    public List excelFileUpload( MultipartHttpServletRequest mReq ) {
        String uploadPath = "";
        String storePath = null, rootPath = null;
        List<HashMap> list = new ArrayList<HashMap>();
        
		logger.info( "OS: " + System.getProperty( "os.name" ) );
		if( System.getProperty( "os.name" ).toUpperCase().startsWith( "WINDOWS" ) ) {
			rootPath = "";
        	storePath = String.format( "%s/%s/%d", fileUploadPath, "excel", System.currentTimeMillis() );
		} else {
			HttpSession session  = mReq.getSession();
        	rootPath = session.getServletContext().getRealPath( "/" );	
        	storePath = String.format( "%s/%s/%d", fileUploadPath, "excel", System.currentTimeMillis() );
		}
		
    	logger.info( "UploadDirectory : " + rootPath + storePath );
    	
    	uploadPath = rootPath + storePath;

        File dir = new File( uploadPath );
        if( !dir.isDirectory() ) {
            dir.mkdirs();
        }
         
        Iterator<String> iter = mReq.getFileNames();
         
        while( iter.hasNext() ) {
            String uploadFileName = iter.next();
            MultipartFile mFile = mReq.getFile(uploadFileName);
            String fileName = mFile.getOriginalFilename();

            System.out.println(fileName);
            if(fileName != null && !fileName.equals("")){
                File file = null;
                try {
                    file = new File( uploadPath + fileName );
                    mFile.transferTo( file );
                     
                    // Excel 파일 읽기!!
                    list = readExcelFileXLSX( file );

                } catch( Exception e ) {
                    e.printStackTrace();
                } finally {
                    if( file != null && file.exists() ) {
                        file.delete();
                    }
                }
            }
        }
        
        return list;
    }
    
    private List<HashMap> readExcelFileXLSX( File excelFile ) {
    	List list = new ArrayList<HashMap>() ;
    	HashMap params = new HashMap();
    	XSSFWorkbook work;
    	
    	try {
			work = new XSSFWorkbook( new FileInputStream( excelFile ) );
		
			int sheetNum = work.getNumberOfSheets();
    	 
			logger.debug("\n# sheet num : " + sheetNum);

			XSSFSheet sheet = work.getSheetAt(0);
    	 
			int rows = sheet.getPhysicalNumberOfRows();
    	 
			logger.debug("\n# sheet rows num : " + rows);
			
			ArrayList<String> columnList = new ArrayList<String>();
			columnList.add( "id" ); //
			columnList.add( "name" );
			columnList.add( "longitude" ); //
			columnList.add( "latitude" ); // 
			columnList.add( "plmn" );
			columnList.add( "circle" );
			columnList.add( "circle_name" );
			columnList.add( "cluster_id" ); //
			columnList.add( "ipaddress" );
			columnList.add( "earfcn" );
			columnList.add( "mbsfn" );
			columnList.add( "mbms_service_area_id" ); //
			columnList.add( "created_at" );
			columnList.add( "updated_at" );
			columnList.add( "city" );
			columnList.add( "bandwidth" ); // 
			columnList.add( "operator_id" ); // 
			columnList.add( "bmsc_id" ); // 
			
    	 
			for( int rownum = 1; rownum < rows; rownum++ ) {
				params = new HashMap();
				XSSFRow row = sheet.getRow( rownum );
    	   
				if( row != null ) {
					int cells = row.getPhysicalNumberOfCells();
    	     
					//logger.debug("\n# row = " + row.getRowNum() + " / cells = " + cells);
    	     
					for( int cellnum = 0; cellnum < columnList.size(); cellnum++ ) {
						XSSFCell cell = row.getCell( cellnum );
    	       
						if( cell != null ) {
    	         
							if( cell.getCellType() == Cell.CELL_TYPE_NUMERIC ) {
								params.put( columnList.get( cellnum ), cell.getNumericCellValue() );
							} else if( cell.getCellType() == Cell.CELL_TYPE_STRING ) {
								params.put( columnList.get( cellnum ), cell.getStringCellValue() );
							} else if( cell.getCellType() == Cell.CELL_TYPE_BOOLEAN ) {
								params.put( columnList.get( cellnum ), cell.getBooleanCellValue() );
							} else if( cell.getCellType() == Cell.CELL_TYPE_BLANK ) {
								params.put( columnList.get( cellnum ), "" );
							} else {
								if( cellnum == 12 || cellnum == 13 ) {
									params.put( columnList.get( cellnum ), cell.getDateCellValue() );
								} else {
									params.put( columnList.get( cellnum ), cell.getStringCellValue() );
								}
							}
							
						}
						//logger.error("\n CELL __ [params ] => " + params.toString());
					}
				}
				
				list.add( params );
			}

    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	return list;
    }
    
    @RequestMapping(value = "/api/getSeviceAreaByBmScId.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getSeviceAreaByBmScId(HttpServletRequest request, HttpServletResponse response) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		//Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		//Integer perPage = 1000;
		
		if( request.getParameter( "bmscId" ) == null ) return;

		BmscServiceAreaSearchParam searchParam = new BmscServiceAreaSearchParam();
		//searchParam.setPage((page-1) * perPage);
		//searchParam.setPerPage(perPage);
		searchParam.setBmscId( Integer.valueOf( request.getParameter( "bmscId" ) ) );
		
		List<BmscServiceArea> datas = mapper.getSeviceAreaByBmScId( searchParam );
		
		JSONArray array = new JSONArray();
		for( int i = 0; i < datas.size(); i++ ) {
			BmscServiceArea data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put( "bmscId", data.getBmscId() );
			obj.put( "serviceAreaId", data.getServiceAreaId() );
			obj.put( "serviceAreaName", data.getServiceAreaName() );
			obj.put( "totalCount", data.getTotalCount() );
			array.add( obj );
		}
		
		try {
			response.setContentType( "application/x-www-form-urlencoded; charset=utf-8" );
	        response.getWriter().print( array.toJSONString() );
	    } catch( Exception e ) {
	        e.printStackTrace();
	    }
	}
    
    @RequestMapping(value = "/api/downloadENBs.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void downloadENBs(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap< String, Object > searchParam;
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		//Integer operator = Integer.valueOf( request.getParameter( "operatorId" ) );
		//Integer bmsc = Integer.valueOf( request.getParameter( "bmscId" ) );
		Integer operator = Integer.valueOf( request.getParameter( "operator_down" ) );
		Integer bmsc = Integer.valueOf( request.getParameter( "bmsc_down" ) );

		int rst = 0;
		
		searchParam = new HashMap();
		searchParam.put( "operatorId", operator );
		searchParam.put( "bmscId", bmsc );
		
		List<HashMap> list = mapper.downloadENBs( searchParam );
		
		ArrayList<String> columnList = new ArrayList<String>();
		columnList.add( "id" );
		columnList.add( "name" );
		columnList.add( "longitude" );
		columnList.add( "latitude" );
		columnList.add( "plmn" );
		columnList.add( "circle" );
		columnList.add( "circle_name" );
		columnList.add( "cluster_id" );
		columnList.add( "ipaddress" );
		columnList.add( "earfcn" );
		columnList.add( "mbsfn" );
		columnList.add( "mbms_service_area_id" );
		columnList.add( "created_at" );
		columnList.add( "updated_at" );
		columnList.add( "city" );
		columnList.add( "bandwidth" );
		columnList.add( "operator_id" );
		columnList.add( "bmsc_id" );

		/*
		//MAP의 KEY값을 담기위함 
		if( list != null && list.size() > 0 ) {
		    //LIST의 첫번째 데이터의 KEY값만 알면 되므로 
		    Map<String,Object>m = list.get( 0 );
		    //MAP의 KEY값을 columnList객체에 ADD 
		    for( String k : m.keySet() ) {
		        columnList.add( k );
		    }
		}
		*/
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyyMMddHHmmss" );
		String today = formatter.format( new java.util.Date() );
		
		String filename = "eNBs_" + today;
		
	    response.setContentType( "application/octet-stream" );
	    response.setHeader ( "Content-Disposition", "attachment; filename=" + filename + ".xlsx;" );
	    
		XSSFWorkbook workbook = downloadExcel( list, columnList );
		
		try {
			OutputStream xlsOut = response.getOutputStream(); //OutputStream으로 엑셀을 저장한다.
			workbook.write( xlsOut );
			xlsOut.close();
	    } catch( Exception e ) {
	        e.printStackTrace();
	    }
		
		return;
	}
    
    @RequestMapping(value = "/api/downloadENBsByServiceAreaId.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void downloadENBsByServiceAreaId(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap< String, Object > searchParam;
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		//Integer operator = Integer.valueOf( request.getParameter( "operatorId" ) );
		//Integer bmsc = Integer.valueOf( request.getParameter( "bmscId" ) );
		Integer operator = Integer.valueOf( request.getParameter( "operator_down" ) );
		Integer bmsc = Integer.valueOf( request.getParameter( "bmsc_down" ) );
		
		String[] arrServiceAreaId = request.getParameterValues("serviceAreaIds");

		Integer[] serviceAreaIds = new Integer[arrServiceAreaId.length];
		for(int i = 0; i < arrServiceAreaId.length; i++) {
			serviceAreaIds[i] = Integer.valueOf( arrServiceAreaId[i] );
		}
		
		int rst = 0;
		
		searchParam = new HashMap();
		searchParam.put( "operatorId", operator );
		searchParam.put( "bmscId", bmsc );
		searchParam.put( "serviceAreaIds", serviceAreaIds );
		
		List<HashMap> list = mapper.downloadENBsByServiceAreaId( searchParam );
		
		ArrayList<String> columnList = new ArrayList<String>();
		columnList.add( "id" );
		columnList.add( "name" );
		columnList.add( "longitude" );
		columnList.add( "latitude" );
		columnList.add( "plmn" );
		columnList.add( "circle" );
		columnList.add( "circle_name" );
		columnList.add( "cluster_id" );
		columnList.add( "ipaddress" );
		columnList.add( "earfcn" );
		columnList.add( "mbsfn" );
		columnList.add( "mbms_service_area_id" );
		columnList.add( "created_at" );
		columnList.add( "updated_at" );
		columnList.add( "city" );
		columnList.add( "bandwidth" );
		columnList.add( "operator_id" );
		columnList.add( "bmsc_id" );

		/*
		//MAP의 KEY값을 담기위함 
		if( list != null && list.size() > 0 ) {
		    //LIST의 첫번째 데이터의 KEY값만 알면 되므로 
		    Map<String,Object>m = list.get( 0 );
		    //MAP의 KEY값을 columnList객체에 ADD 
		    for( String k : m.keySet() ) {
		        columnList.add( k );
		    }
		}
		*/
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyyMMddHHmmss" );
		String today = formatter.format( new java.util.Date() );
		
		String filename = "eNBs_" + today;
		
	    response.setContentType( "application/octet-stream" );
	    response.setHeader ( "Content-Disposition", "attachment; filename=" + filename + ".xlsx;" );
	    
		XSSFWorkbook workbook = downloadExcel( list, columnList );
		
		try {
			OutputStream xlsOut = response.getOutputStream(); //OutputStream으로 엑셀을 저장한다.
			workbook.write( xlsOut );
			xlsOut.close();
	    } catch( Exception e ) {
	        e.printStackTrace();
	    }
		
		return;
	}
    
    public XSSFWorkbook downloadExcel( List<HashMap> list, ArrayList<String> columnList )
    {
    	//1차로 workbook을 생성 
    	XSSFWorkbook workbook = new XSSFWorkbook();
    	//2차는 sheet생성 
    	XSSFSheet sheet = workbook.createSheet( "eNBs" );
    	//엑셀의 행 
    	XSSFRow row = null;
    	//엑셀의 셀 
    	XSSFCell cell = null;
    	
    	row = sheet.createRow( (short)0 );
        if( columnList != null && columnList.size() > 0 ) {
            for( int j = 0; j < columnList.size(); j++ ) {
                //생성된 row에 컬럼을 생성한다 
                cell = row.createCell( j );
                //map에 담긴 데이터를 가져와 cell에 add한다 
                cell.setCellValue( String.valueOf( columnList.get( j ) ) );
            }
        }
        
    	//임의의 DB데이터 조회 
    	if( list != null && list.size() > 0 ) {
    	    int i = 1;
    	    for( HashMap<String,Object>mapobject : list ) {
    	        // 시트에 하나의 행을 생성한다(i 값이 0이면 첫번째 줄에 해당) 
    	        row = sheet.createRow( (short)i );
    	        i++;
    	        if( columnList != null && columnList.size() > 0 ) {
    	            for( int j = 0; j < columnList.size(); j++ ) {
    	                //생성된 row에 컬럼을 생성한다 
    	                cell = row.createCell( j );
    	                //map에 담긴 데이터를 가져와 cell에 add한다 
    	                if( mapobject.get( columnList.get( j ) ) != null ) {
    	                	cell.setCellValue( String.valueOf( mapobject.get( columnList.get( j ) ) ) );
    	                } else {
    	                	cell.setCellValue( "" );
    	                }
    	            }
    	        }
    	    }
    	}
    	
		for( int i = 0; i < columnList.size(); i++ ) {
			sheet.autoSizeColumn(i); //cell 크기 자동 맞춤
		}
		
    	return workbook;
    }
    
    @RequestMapping(value = "/api/getServiceAreaEnbApNotMappedSA.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getServiceAreaEnbApNotMappedSA(HttpServletRequest request, HttpServletResponse response) {

		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap<String, Object> searchParam = new HashMap();
		searchParam.put( "serviceAreaId", Integer.valueOf( request.getParameter( "serviceAreaId" ) ) );
		searchParam.put( "bmscId", Integer.valueOf( request.getParameter( "bmscId" ) ) );
		searchParam.put( "swLat", request.getParameter( "swLat" ) );
		searchParam.put( "swLng", request.getParameter( "swLng" ) );
		searchParam.put( "neLat", request.getParameter( "neLat" ) );
		searchParam.put( "neLng", request.getParameter( "neLng" ) );
		
		List<ServiceAreaEnbAp> datas = mapper.getServiceAreaEnbApNotMappedSA( searchParam );
		
		JSONArray array = new JSONArray();
		for( int i = 0; i < datas.size(); i++ ) {
			ServiceAreaEnbAp data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put("serviceAreaId", data.getServiceAreaId());
			obj.put("serviceAreaBandwidth", data.getServiceAreaBandwidth());
			obj.put("serviceAreaName", data.getServiceAreaName());
			obj.put("serviceAreaCity", data.getServiceAreaCity());
			obj.put("enbApId", data.getEnbApId());
			obj.put("enbApName", data.getEnbApName());
			obj.put("longitude", data.getLongitude());
			obj.put("latitude", data.getLatitude());
			obj.put("plmn", data.getPlmn());
			obj.put("mbsfn", data.getMbsfn());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("totalCount", data.getTotalCount());
			array.add(obj);
		}
		
		try {
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(array.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
    @RequestMapping(value = "/api/getSeviceAreaNotMapped.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void getSeviceAreaNotMapped( HttpServletRequest request, HttpServletResponse response ) {
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap< String, Integer > searchParam = new HashMap();
		searchParam.put( "bmscId", Integer.valueOf( request.getParameter( "bmscId" ) ) );
		
		List<HashMap<String, Object>> datas = mapper.getSeviceAreaNotMapped( searchParam );

		JSONArray array = new JSONArray();
		for( int i = 0; i < datas.size(); i++ ) {
			HashMap data = datas.get(i);
			JSONObject obj = new JSONObject();
			obj.put( "bmscId", data.get( "bmscId" ) );
			obj.put( "serviceAreaId", data.get( "serviceAreaId" ) );
			obj.put( "serviceAreaName", data.get( "serviceAreaName" ) );
			obj.put( "totalCount", data.get( "totalCount" ) );
			array.add( obj );
		}

		try {
			response.setContentType( "application/x-www-form-urlencoded; charset=utf-8" );
	        response.getWriter().print( array.toJSONString() );
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
}

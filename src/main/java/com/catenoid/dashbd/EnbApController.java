package com.catenoid.dashbd;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
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

import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.dao.EnbApMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.EnbAp;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class EnbApController {
	
	private static final Logger logger = LoggerFactory.getLogger(EnbApController.class);
	
	@Resource
	private Environment env;
	
	@Autowired
	private SqlSession sqlSession;	
		
		
	@RequestMapping(value = "/api/enb.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doEnb(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		String enb_id = request.getParameter("enb_id");
		logger.info("request_type: " + (request_type == null ? "null" : request_type) + ", enb_id: " + (enb_id == null ? "null" : enb_id));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readEnbApByID(request);
		}
		else if(request_type.equalsIgnoreCase("create")) {
			return createEnbAp(request);
		}
		else if(request_type.equalsIgnoreCase("update")) {
			return updateEnbApByID(request);
		}
		else if(request_type.equalsIgnoreCase("delete")) {
			return deleteEnbApByID(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
		
	private ResponseEntity<String> readEnbApByID(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {			
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			EnbApMapper mapper = sqlSession.getMapper(EnbApMapper.class);
			EnbAp data = mapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(data == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("id", data.getId());
			obj.put("name", data.getName());
			obj.put("longitude", data.getLongitude());
			obj.put("latitude", data.getLatitude());
			obj.put("plmn", data.getPlmn());
			obj.put("circle", data.getCircle());
			obj.put("circle_name", data.getCircleName());
			obj.put("cluster_id", data.getClusterId());
			obj.put("ipaddress", data.getIpaddress());
			obj.put("earfcn", data.getEarfcn());
			String mbsfn = data.getMbsfn();
			if(mbsfn.startsWith(",")) mbsfn = mbsfn.substring(1);
			if(mbsfn.endsWith(",")) mbsfn = mbsfn.substring(0, mbsfn.length() - 1);
			obj.put("mbsfn", mbsfn);
			obj.put("mbms_service_area_id", data.getMbmsServiceAreaId());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("city", data.getCity());
			obj.put("bandwidth", data.getBandwidth());
			root.put("result", obj);

			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "readEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "readEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> createEnbAp(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject root = new JSONObject();
		try {
			logger.info("name: " + request.getParameter("name") + ", latitude: " + request.getParameter("latitude") + ", longitude: " + request.getParameter("longitude"));			
			if(!valueCheck(request.getParameter("name"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("latitude"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("longitude"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("plmn"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("mbsfn"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("mbms_service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			EnbApMapper mapper = sqlSession.getMapper(EnbApMapper.class);
			EnbAp record = new EnbAp();
			if(valueCheck(request.getParameter("id"))) record.setId(Integer.parseInt(request.getParameter("id")));
			record.setName(request.getParameter("name"));
			record.setLatitude(BigDecimal.valueOf(Double.parseDouble(request.getParameter("latitude"))));
			record.setLongitude(BigDecimal.valueOf(Double.parseDouble(request.getParameter("longitude"))));
			record.setPlmn(request.getParameter("plmn"));			
			if(valueCheck(request.getParameter("circle"))) record.setCircle(request.getParameter("circle"));
			if(valueCheck(request.getParameter("circle_name"))) record.setCircleName(request.getParameter("circle_name"));
			if(valueCheck(request.getParameter("cluster_id"))) record.setClusterId(Integer.parseInt(request.getParameter("cluster_id")));
			if(valueCheck(request.getParameter("ipaddress"))) record.setIpaddress(request.getParameter("ipaddress"));
			if(valueCheck(request.getParameter("earfcn"))) record.setEarfcn(request.getParameter("earfcn"));
			String mbsfn = request.getParameter("mbsfn");
			if(!mbsfn.startsWith(",")) mbsfn = "," + mbsfn;
			if(!mbsfn.endsWith(",")) mbsfn = mbsfn + ",";
			record.setMbsfn(mbsfn);
			record.setMbmsServiceAreaId(Integer.parseInt(request.getParameter("mbms_service_area_id")));
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);	
			
			root.put("code", 1);
			root.put("message", null);			
			
			JSONObject obj = new JSONObject();
			if(valueCheck(request.getParameter("id"))) obj.put("id", Integer.parseInt(request.getParameter("id")));
			else obj.put("id", record.getId());
			root.put("result", obj);
			
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "createEnbAp");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "createEnbAp");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "createEnbAp");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			root.put("code", ErrorCodes.DATA_DUPLICATION.getCode());
			root.put("message", ErrorCodes.DATA_DUPLICATION.getMsg());
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "createEnbAp");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> updateEnbApByID(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			logger.info("ID: " + request.getParameter("id") + ", name: " + request.getParameter("name") + ", latitude: " + request.getParameter("latitude") + ", longitude: " + request.getParameter("longitude"));
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);			
			
			EnbApMapper mapper = sqlSession.getMapper(EnbApMapper.class);
			EnbAp record = new EnbAp();
			record.setId(Integer.parseInt(request.getParameter("id")));			
			if(valueCheck(request.getParameter("name"))) record.setName(request.getParameter("name"));
			if(valueCheck(request.getParameter("latitude"))) record.setLatitude(BigDecimal.valueOf(Double.parseDouble(request.getParameter("latitude"))));
			if(valueCheck(request.getParameter("longitude"))) record.setLongitude(BigDecimal.valueOf(Double.parseDouble(request.getParameter("longitude"))));
			if(valueCheck(request.getParameter("plmn"))) record.setPlmn(request.getParameter("plmn"));
			if(valueCheck(request.getParameter("circle"))) record.setCircle(request.getParameter("circle"));
			if(valueCheck(request.getParameter("circle_name"))) record.setCircleName(request.getParameter("circle_name"));
			if(valueCheck(request.getParameter("cluster_id"))) record.setClusterId(Integer.parseInt(request.getParameter("cluster_id")));
			if(valueCheck(request.getParameter("ipaddress"))) record.setIpaddress(request.getParameter("ipaddress"));
			if(valueCheck(request.getParameter("earfcn"))) record.setEarfcn(request.getParameter("earfcn"));
			if(valueCheck(request.getParameter("mbsfn"))) {
				String mbsfn = request.getParameter("mbsfn");
				if(!mbsfn.startsWith(",")) mbsfn = "," + mbsfn;
				if(!mbsfn.endsWith(",")) mbsfn = mbsfn + ",";
				record.setMbsfn(mbsfn);
			}
			if(valueCheck(request.getParameter("mbms_service_area_id"))) record.setMbmsServiceAreaId(Integer.parseInt(request.getParameter("mbms_service_area_id")));
			int res = mapper.updateByPrimaryKeySelective(record);
			logger.info("UPDATE RESULT: " + res);
			if(res == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", res);
			if(res == 0) root.put("message", "Not found update record");
			else root.put("message", null);
			
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "updateEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "updateEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "updateEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> deleteEnbApByID(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			logger.info("ID: " + request.getParameter("id"));
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			EnbApMapper mapper = sqlSession.getMapper(EnbApMapper.class);	
			int res = mapper.deleteByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			int subres = 0;
			logger.info("DELETE RESULT: " + res);
			if(res > 0){
				subres = mapper.deleteByPrimaryKeyService(Integer.parseInt(request.getParameter("id")));
			}
			JSONObject root = new JSONObject();
			root.put("code", res);
			root.put("subcode", subres);
			if(res == 0) root.put("message", "Not found delete record");
			else root.put("message", null);
			
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "deleteEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "deleteEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			syslogMap.put("reqType", "eNB Mgmt");
			syslogMap.put("reqSubType", "deleteEnbApByID");
			syslogMap.put("reqUrl", "api/enb.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private String getFormatDateTime(Date date, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	private boolean valueCheck(String parameter) {
		// TODO Auto-generated method stub
		if(parameter == null || parameter.trim().equals("")) return false;
		return true;
	}
}

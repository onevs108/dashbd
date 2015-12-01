package com.catenoid.dashbd;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.dao.BmscMapper;
import com.catenoid.dashbd.dao.BmscServiceAreaMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.BmscServiceArea;
import com.catenoid.dashbd.dao.model.BmscServiceAreaExample;
import com.catenoid.dashbd.dao.model.BmscServiceAreaExample.Criteria;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class BmscController {
	
	private static final Logger logger = LoggerFactory.getLogger(BmscController.class);
	
	@Resource
	private Environment env;
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "api/bmsc.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doBmsc(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readBmscByID(request);
		}
		else if(request_type.equalsIgnoreCase("read_service_area")) {
			return readBmscServiceArea(request);
		}
//		else if(request_type.equalsIgnoreCase("read_service_area_enb_ap")) {
//			return readBmscServiceAreaEnbAp(request);
//		}			
		else if(request_type.equalsIgnoreCase("read_by_operator")) {
			return readBmscByOperator(request);
		}
		else if(request_type.equalsIgnoreCase("create")) {
			return createBmsc(request);
		}
		else if(request_type.equalsIgnoreCase("update_metadata")) {
			return updateBmscByID(request);
		}
		else if(request_type.equalsIgnoreCase("delete")) {
			return deleteBmscByID(request);
		}
		else if(request_type.equalsIgnoreCase("add_service_area")) {
			return addBmscAtServiceArea(request);
		}
		else if(request_type.equalsIgnoreCase("remove_service_area")) {
			return removeBmscAtServiceArea(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readBmscByID(HttpServletRequest request) {
		try {			
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);			
			
			BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);
			
			Bmsc data = mapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(data == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("id", data.getId());
			obj.put("name", data.getName());
			obj.put("circle", data.getCircle());
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
	
	private ResponseEntity<String> readBmscServiceArea(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);			
			List<BmscServiceArea> datas = mapper.selectBmscServiceArea(Integer.parseInt(request.getParameter("id")));
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					BmscServiceArea data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("bmsc_id", data.getBmscId());
					obj.put("bmsc_name", data.getBmscName());
					obj.put("bmsc_circle", data.getBmscCircle());
					obj.put("service_area_id", data.getServiceAreaId());
					obj.put("service_area_name", data.getServiceAreaName());
					obj.put("service_area_city", data.getServiceAreaCity());
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				BmscServiceArea data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("bmsc_id", data.getBmscId());
				obj.put("bmsc_name", data.getBmscName());
				obj.put("bmsc_circle", data.getBmscCircle());
				obj.put("service_area_id", data.getServiceAreaId());
				obj.put("service_area_name", data.getServiceAreaName());
				obj.put("service_area_city", data.getServiceAreaCity());
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
	
//	private ResponseEntity<String> readBmscServiceAreaEnbAp(HttpServletRequest request) {
//		JSONObject root = new JSONObject();
//		try {
//			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
//			
//			BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);			
//			List<BmscServiceAreaEnbAp> datas = mapper.selectBmscServiceAreaEnbAp(Integer.parseInt(request.getParameter("id")));
//			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
//			
//			root.put("code", 1);
//			root.put("message", null);
//			
//			if(datas.size() > 1) {
//				JSONArray array = new JSONArray();
//				for(int i = 0; i < datas.size(); i++) {
//					BmscServiceAreaEnbAp data = datas.get(i);
//					JSONObject obj = new JSONObject();
//					obj.put("bmsc_id", data.getBmscId());
//					obj.put("bmsc_name", data.getBmscName());
//					obj.put("bmsc_circle", data.getBmscCircle());
//					obj.put("service_area_id", data.getServiceAreaId());
//					obj.put("service_area_name", data.getServiceAreaName());
//					obj.put("service_area_city", data.getServiceAreaCity());
//					obj.put("enb_ap_id", data.getEnbApId());
//					obj.put("enb_ap_name", data.getEnbApName());
//					obj.put("longitude", data.getLongitude());
//					obj.put("latitude", data.getLatitude());
//					array.add(obj);
//				}
//				root.put("result", array);
//			}
//			else {
//				BmscServiceAreaEnbAp data = datas.get(0);
//				JSONObject obj = new JSONObject();
//				obj.put("bmsc_id", data.getBmscId());
//				obj.put("bmsc_name", data.getBmscName());
//				obj.put("bmsc_circle", data.getBmscCircle());
//				obj.put("service_area_id", data.getServiceAreaId());
//				obj.put("service_area_name", data.getServiceAreaName());
//				obj.put("service_area_city", data.getServiceAreaCity());
//				obj.put("enb_ap_id", data.getEnbApId());
//				obj.put("enb_ap_name", data.getEnbApName());
//				obj.put("longitude", data.getLongitude());
//				obj.put("latitude", data.getLatitude());
//				
//				root.put("result", obj);
//			}
//			
//			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.toString());
//		}
//		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	private ResponseEntity<String> readBmscByOperator(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> createBmsc(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("name"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);
			Bmsc record = new Bmsc();
			if(valueCheck(request.getParameter("id"))) record.setId(Integer.parseInt(request.getParameter("id")));
			record.setName(request.getParameter("name"));
			if(valueCheck(request.getParameter("circle"))) record.setCircle(request.getParameter("circle"));
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			if(valueCheck(request.getParameter("id"))) obj.put("id", Integer.parseInt(request.getParameter("id"))); 
			else obj.put("id", record.getId());
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
	
	private ResponseEntity<String> updateBmscByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);
			Bmsc record = new Bmsc();
			record.setId(Integer.parseInt(request.getParameter("id")));			
			if(valueCheck(request.getParameter("name"))) record.setName(request.getParameter("name"));
			if(valueCheck(request.getParameter("circle"))) record.setCircle(request.getParameter("circle"));
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
	
	private ResponseEntity<String> deleteBmscByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);				
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
	
	private ResponseEntity<String> addBmscAtServiceArea(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			BmscServiceAreaMapper mapper = sqlSession.getMapper(BmscServiceAreaMapper.class);
			BmscServiceArea record = new BmscServiceArea();
			record.setBmscId(Integer.parseInt(request.getParameter("id")));
			record.setServiceAreaId(Integer.parseInt(request.getParameter("service_area_id")));
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);
			
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
	
	private ResponseEntity<String> removeBmscAtServiceArea(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			BmscServiceAreaMapper mapper = sqlSession.getMapper(BmscServiceAreaMapper.class);
			BmscServiceAreaExample exm = new BmscServiceAreaExample();
			Criteria criteria = exm.createCriteria().andBmscIdEqualTo(Integer.parseInt(request.getParameter("id")));
			criteria.andServiceAreaIdEqualTo(Integer.parseInt(request.getParameter("service_area_id")));			
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

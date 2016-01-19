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
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.dao.model.UsersExample;
import com.catenoid.dashbd.dao.model.UsersExample.Criteria;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class UsersController {
	 
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@Resource
	private Environment env;
	
	@Autowired
	private SqlSession sqlSession;	
	
	@RequestMapping(value = "api/user.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doUser(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readUsers(request);
		}
		else if(request_type.equalsIgnoreCase("get_tot_count")) {
			return getTotalCount(request);
		}
		else if(request_type.equalsIgnoreCase("create")) {
			return createUser(request);
		}
		else if(request_type.equalsIgnoreCase("update")) {
			return updateUserByID(request);
		}
		else if(request_type.equalsIgnoreCase("delete")) {
			return deleteUserByID(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readUsers(HttpServletRequest request) {
		try {			
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			UsersExample exm = new UsersExample();
			if(valueCheck(request.getParameter("id"))) exm.and().andIdEqualTo(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("operator_id"))) exm.and().andOperatorIdEqualTo(request.getParameter("operator_id"));
			if(valueCheck(request.getParameter("user_id"))) exm.and().andUserIdEqualTo(request.getParameter("user_id"));
			if(valueCheck(request.getParameter("department"))) exm.and().andDepartmentEqualTo(request.getParameter("department"));
			if(valueCheck(request.getParameter("first_name"))) exm.and().andFirstNameLike("%" + request.getParameter("first_name") + "%");
			if(valueCheck(request.getParameter("last_name"))) exm.and().andLastNameLike("%" + request.getParameter("last_name") + "%");
			if(valueCheck(request.getParameter("order_by"))) exm.setOrderByClause(request.getParameter("order_by"));			
			if(valueCheck(request.getParameter("page"))) {
				if(!valueCheck(request.getParameter("limit"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
				exm.setLimitByClause(((Integer.parseInt(request.getParameter("page")) - 1) * Integer.parseInt(request.getParameter("limit"))) + ", " + request.getParameter("limit"));
			}
			else if(valueCheck(request.getParameter("limit"))) exm.setLimitByClause(request.getParameter("limit"));
			
			List<Criteria> list = exm.getOredCriteria();
			if(!valueCheck(request.getParameter("page")) && list.size() == 0) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			List<Users> datas = mapper.selectByExample(exm);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					Users data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getId());
					obj.put("user_id", data.getUserId());
					obj.put("operator_id", data.getOperatorId());					
					obj.put("password", data.getPassword());
					obj.put("first_name", data.getFirstName());
					obj.put("last_name", data.getLastName());
					obj.put("department", data.getDepartment());
					obj.put("permission", data.getPermission());
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				Users data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getId());
				obj.put("user_id", data.getUserId());
				obj.put("operator_id", data.getOperatorId());				
				obj.put("password", data.getPassword());
				obj.put("first_name", data.getFirstName());
				obj.put("last_name", data.getLastName());
				obj.put("department", data.getDepartment());
				obj.put("permission", data.getPermission());
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
	
	private ResponseEntity<String> getTotalCount(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			UsersExample exm = new UsersExample();
			if(valueCheck(request.getParameter("operator_id"))) exm.and().andOperatorIdEqualTo(request.getParameter("operator_id"));
			if(valueCheck(request.getParameter("user_id"))) exm.and().andUserIdEqualTo(request.getParameter("user_id"));
			if(valueCheck(request.getParameter("department"))) exm.and().andDepartmentEqualTo(request.getParameter("department"));
			if(valueCheck(request.getParameter("first_name"))) exm.and().andFirstNameLike("%" + request.getParameter("first_name") + "%");
			if(valueCheck(request.getParameter("last_name"))) exm.and().andLastNameLike("%" + request.getParameter("last_name") + "%");
			int count = mapper.countByExample(exm);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("count", count);
			root.put("result", obj);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK); 
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<String> createUser(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {			
			if(!valueCheck(request.getParameter("operator_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("user_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("password"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("permission"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			Users record = new Users();
			record.setOperatorId(request.getParameter("operator_id"));
			record.setUserId(request.getParameter("user_id"));
			record.setPassword(request.getParameter("password"));
			if(valueCheck(request.getParameter("first_name"))) record.setFirstName(request.getParameter("first_name"));
			if(valueCheck(request.getParameter("last_name"))) record.setLastName(request.getParameter("last_name"));
			if(valueCheck(request.getParameter("department"))) record.setDepartment(request.getParameter("department"));			
			record.setPermission(Integer.parseInt(request.getParameter("permission")));						
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);			
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
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
	
	private ResponseEntity<String> updateUserByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			Users record = new Users();
			record.setId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("operator_id"))) record.setOperatorId(request.getParameter("operator_id"));			
			if(valueCheck(request.getParameter("password"))) record.setPassword(request.getParameter("password"));
			if(valueCheck(request.getParameter("first_name"))) record.setFirstName(request.getParameter("first_name"));
			if(valueCheck(request.getParameter("last_name"))) record.setLastName(request.getParameter("last_name"));
			if(valueCheck(request.getParameter("department"))) record.setDepartment(request.getParameter("department"));
			record.setPermission(Integer.parseInt(request.getParameter("permission")));			
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
	
	private ResponseEntity<String> deleteUserByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);				
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
		if(parameter == null || parameter.trim().equals("")) return false;
		return true;
	}
}

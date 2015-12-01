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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.OperatorBmscMapper;
import com.catenoid.dashbd.dao.OperatorMapper;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorBmsc;
import com.catenoid.dashbd.dao.model.OperatorExample;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class OperatorController {
	
	private static final Logger logger = LoggerFactory.getLogger(OperatorController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;	
	
	@RequestMapping(value = "api/operator.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doOperator(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readOperator(request);
		}
		else if(request_type.equalsIgnoreCase("read_operator_bmsc")) {
			return readOperatorBmsc(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readOperator(HttpServletRequest request) {
		try {			
			OperatorMapper mapper = sqlSession.getMapper(OperatorMapper.class);
			OperatorExample exm = new OperatorExample();
			
			List<Operator> datas = mapper.selectByExample(exm);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					Operator data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getId());
					obj.put("name", data.getName());
					obj.put("description", data.getDescription());
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				Operator data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getId());
				obj.put("name", data.getName());
				obj.put("description", data.getDescription());
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
	
	private ResponseEntity<String> readOperatorBmsc(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			OperatorBmscMapper mapper = sqlSession.getMapper(OperatorBmscMapper.class);
			OperatorBmsc record = new OperatorBmsc();
			if(valueCheck(request.getParameter("id"))) record.setOperatorId(Integer.parseInt(request.getParameter("id")));
			List<OperatorBmsc>datas = mapper.selectOperatorBmsc(record);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					OperatorBmsc data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("operator_id", data.getOperatorId());
					obj.put("operator_name", data.getOperatorName());
					obj.put("bmsc_id", data.getBmscId());
					obj.put("bmsc_name", data.getBmscName());
					obj.put("bmsc_circle", data.getBmscCircle());
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				OperatorBmsc data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("operator_id", data.getOperatorId());
				obj.put("operator_name", data.getOperatorName());
				obj.put("bmsc_id", data.getBmscId());
				obj.put("bmsc_name", data.getBmscName());
				obj.put("bmsc_circle", data.getBmscCircle());
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

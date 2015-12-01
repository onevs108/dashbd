package com.catenoid.dashbd;

import java.io.UnsupportedEncodingException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.TransReportMapper;
import com.catenoid.dashbd.dao.model.TransReport;
import com.catenoid.dashbd.dao.model.TransReportExample;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class TransactionReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionReportController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "api/trans_report.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doTransactionReport(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readTransReport(request);
		}
		else if(request_type.equalsIgnoreCase("read_count")) {
			return readTransReportCount(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readTransReport(HttpServletRequest request) {
		try {
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("start_datetime"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("end_datetime"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			TransReportMapper mapper = sqlSession.getMapper(TransReportMapper.class);
			TransReport record = new TransReport();
			record.setServiceAreaId(Integer.parseInt(request.getParameter("service_area_id")));
			record.setStartDate(request.getParameter("start_datetime"));
			record.setEndDate(request.getParameter("end_datetime"));
			if(valueCheck(request.getParameter("result_code"))) {
				if(Integer.parseInt(request.getParameter("result_code")) == 0) {
					record.setResultCodeClause("result_code = 0");
				}
				else {
					record.setResultCodeClause("result_code <> 0");
				}
			}
			List<TransReport> datas = mapper.selectByServiceAreaId(record);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() == 1) {				
				TransReport data = datas.get(0);				
				JSONObject obj = new JSONObject();
				obj.put("transaction_id", data.getTransactionId());
				obj.put("service_id", data.getServiceId());
				obj.put("agent_key", data.getAgentKey());
				obj.put("send_dtm", getFormatDateTime(data.getSendDtm(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("result_code", data.getResultCode());
				obj.put("result_msg", data.getResultMsg());
				root.put("result", obj);
			}
			else {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					TransReport data = datas.get(i);					
					JSONObject obj = new JSONObject();
					obj.put("transaction_id", data.getTransactionId());
					obj.put("service_id", data.getServiceId());
					obj.put("agent_key", data.getAgentKey());
					obj.put("send_dtm", getFormatDateTime(data.getSendDtm(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("result_code", data.getResultCode());
					obj.put("result_msg", data.getResultMsg());
					array.add(obj);
				}
				if(array.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
				
				root.put("result", array);
			}
			
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> readTransReportCount(HttpServletRequest request) {
		try {
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("start_datetime"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("end_datetime"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			TransReportMapper mapper = sqlSession.getMapper(TransReportMapper.class);
			TransReport record = new TransReport();
			record.setServiceAreaId(Integer.parseInt(request.getParameter("service_area_id")));
			record.setStartDate(request.getParameter("start_datetime"));
			record.setEndDate(request.getParameter("end_datetime"));
			if(valueCheck(request.getParameter("result_code"))) {
				if(Integer.parseInt(request.getParameter("result_code")) == 0) {
					record.setResultCodeClause("result_code = 0");
				}
				else {
					record.setResultCodeClause("result_code <> 0");
				}
			}
			int count = mapper.selectCountByServiceAreaId(record);
			
			JSONObject root = new JSONObject();
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
}

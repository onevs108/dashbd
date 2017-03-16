package com.catenoid.dashbd;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.model.Log;
import com.catenoid.dashbd.service.LogService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LogController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);
	@Resource(name = "transactionManager") 
	protected DataSourceTransactionManager txManager;

	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private LogService logServiceImpl;
	
	/**
	 * 시스템관리 > 로그 > 로그 관리 페이지 호출
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/resources/logMgmt.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView getServiceAreaMain(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("log/logMgmt");
		
		Calendar cal = Calendar.getInstance(Locale.KOREA);
        String year = cal.get ( Calendar.YEAR ) + ""; 
        String month = ( cal.get ( Calendar.MONTH ) + 1 ) + ""; 
        String date = cal.get ( Calendar.DATE ) + "";
        month = (month.length() == 1)? "0" + month : month;
        date = (date.length() == 1)? "0" + date : date;
        
		HashMap<String, Object> param = new HashMap<String, Object>(); 
		param.put("searchDateFrom", year + "-" + month + "-" + date);
		param.put("searchDateTo", year + "-" + month + "-" + date);
		param.put("reqType", "\'Login\', \'Operator\', \'OperatorGroup\', \'ServiceArea\', \'ServiceAreaGroup\', \'Schedule\', \'Database\'");

		List<Log> resultList = logServiceImpl.selectLogDate(param);
		mv.addObject("resultList", resultList);
		
		return mv;
	}
	
	/**
	 * 시스템관리 > 로그 > 로그데이터 조회
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/selectLogDate.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void serviceAreaProccess(@RequestParam HashMap<String, Object> param, HttpServletResponse response) {
		try {
			JSONObject resultMap = new JSONObject();
			
			String searchDateFrom = param.get("searchDateFrom").toString();
			String searchDateTo = param.get("searchDateTo").toString();
			
			String[] searchDateFromSpliteList = searchDateFrom.split("/");
			String[] searchDateToSpliteList = searchDateTo.split("/");
			
			param.put("searchDateFrom", searchDateFromSpliteList[2] + "-" + searchDateFromSpliteList[0] + "-" + searchDateFromSpliteList[1]);
			param.put("searchDateTo", searchDateToSpliteList[2] + "-" + searchDateToSpliteList[0] + "-" + searchDateToSpliteList[1]);
			
			if(param.get("tabDiv").equals("1")) {
				param.put("reqType", "\'Login\', \'Operator\', \'OperatorGroup\', \'Content\', \'ServiceArea\', \'ServiceAreaGroup\', \'Schedule\', \'Database\'");
			} else if(param.get("tabDiv").equals("2")) {
				param.put("reqType", "\'ServiceArea\'");
			} else if(param.get("tabDiv").equals("3")) {
				param.put("reqType", "\'Schedule\'");
			} else if(param.get("tabDiv").equals("4")) {
				param.put("reqType", "\'Database\'");
			}
			
			List<Log> resultList = logServiceImpl.selectLogDate(param);
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i < resultList.size(); i++) {
				JSONObject jsonObject = resultList.get(i).toJSONObject();
				jsonArray.put(jsonObject);
			}
			
			resultMap.put("resultList", jsonArray);
			
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(resultMap.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
} 

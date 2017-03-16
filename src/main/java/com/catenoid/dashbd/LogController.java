package com.catenoid.dashbd;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
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
		
		HashMap<String, Object> param = new HashMap<String, Object>(); 
		List<HashMap<String, Object>> resultList = logServiceImpl.selectLogDate(param);
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
			
			List<HashMap<String, Object>> resultList = logServiceImpl.selectLogDate(param);
			resultMap.put("resultList", resultList);
			
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(resultMap.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
} 

package com.catenoid.dashbd;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.CircleMapper;
import com.catenoid.dashbd.dao.OperatorMapper;
import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.google.gson.Gson;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class CircleController {
	
	private static final Logger logger = LoggerFactory.getLogger(CircleController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	@Value("#{config['main.contents.max']}")
	private Integer contentMax;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/resources/circle.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView getServiceAreaMain(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("circleMgmt");
		
//		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		
		List<Circle> circleList = operatorMapper.selectCircleListAll();
		List<Circle> townList = operatorMapper.selectTownListAll();
		
		searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		searchParam.setOperatorId(townList.get(0).getCircle_id());
		
		logger.info("First Town ID: {}", searchParam.getOperatorId());
		
		JSONArray circleJsonArray = new JSONArray();
		for (Circle circle : circleList) {
			JSONObject json = new JSONObject();
			json.put("circle_name", circle.getCircle_name());
			json.put("latitude", circle.getLatitude());
			json.put("longitude", circle.getLongitude());
			circleJsonArray.add(json);
		}
		
		mv.addObject("circleList", circleJsonArray);
		mv.addObject("townList", townList);
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/api/getCityFromCircleName.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String addToServiceArea(@RequestParam HashMap<String, String> param, HttpServletRequest request, HttpServletResponse response) {
		
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
//		List<HashMap<String, String>> cityList = circleMapper.selectCityFromCircle(param);
		List<HashMap<String, String>> cityList = circleMapper.selectCity();
		
		JSONObject cityJson = new JSONObject();
		
		Gson gson = new Gson();
		String str = gson.toJson(cityList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		cityJson.put("thisCity", json);
		cityJson.put("noneCity", json);
		cityJson.put("otherCity", json);
		
		return cityJson.toJSONString();
	}
	
}

package com.catenoid.dashbd;

import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
		
		Gson gson = new Gson();
		org.json.JSONArray circleJson = new org.json.JSONArray(gson.toJson(circleList));
		
		mv.addObject("circleList", circleJson);
		mv.addObject("townList", townList);
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/api/getCityFromCircleName.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String addToServiceArea(@RequestParam HashMap<String, String> param, HttpServletRequest request, HttpServletResponse response) {
		Gson gson = new Gson();
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		List<HashMap<String, String>> cityList = circleMapper.selectCity();
		List<HashMap<String, String>> thisCityList = new ArrayList<HashMap<String,String>>();
		List<HashMap<String, String>> noneCityList = new ArrayList<HashMap<String,String>>();
		List<HashMap<String, String>> otherCityList = new ArrayList<HashMap<String,String>>();
		
		JSONObject cityJson = new JSONObject();
		
		for (int i = 0; i < cityList.size(); i++) {
			if(cityList.get(i).get("circle_name").equals(param.get("circleName"))){	//클릭한 서클에 속한 city
				thisCityList.add(cityList.get(i));
			}
			else if (cityList.get(i).get("circle_name").length() < 2) // 아무곳에도 속하지 않은 city
			{
				noneCityList.add(cityList.get(i));
			}
			else // 다른 곳에 속한 city
			{
				otherCityList.add(cityList.get(i));
			}
		}
		
		org.json.JSONArray thisCityJson = new org.json.JSONArray(gson.toJson(thisCityList));
		org.json.JSONArray noneJson = new org.json.JSONArray(gson.toJson(noneCityList));
		org.json.JSONArray otherJson = new org.json.JSONArray(gson.toJson(otherCityList));
		
		cityJson.put("thisCity", thisCityJson);
		cityJson.put("noneCity", noneJson);
		cityJson.put("otherCity", otherJson);
		
		return cityJson.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/checkCircleName.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String checkCircleName(@RequestParam String circleName, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "SUCCESS";
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		int result = circleMapper.checkCircleExist(circleName);
		if(result > 0) {
			returnStr = "EXIST";
		}
		return returnStr;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/insertCircle.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String insertCircle(@RequestParam HashMap<String, String> param, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "SUCCESS";
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		int result = circleMapper.insertCircle(param);
		if(result < 1) {
			returnStr = "FAIL";
		}
		return returnStr;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/insertCity.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String insertCity(@RequestParam HashMap<String, String> param, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "SUCCESS";
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		int result = circleMapper.insertCity(param);
		if(result < 1) {
			returnStr = "FAIL";
		}
		return returnStr;
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/deleteCircle.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String deleteCircle(@RequestParam String circleId, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "SUCCESS";
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		int result = circleMapper.deleteCircle(circleId);
		if(result != 1) {
			returnStr = "FAIL";
		}
		return returnStr;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/deleteCity.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String deleteCity(@RequestParam String cityId, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "SUCCESS";
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		int result = circleMapper.deleteCity(cityId);
		if(result != 1) {
			returnStr = "FAIL";
		}
		return returnStr;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/checkSaId.do", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	public String checkSAID(@RequestParam String checkSaId, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "SUCCESS";
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		int result = circleMapper.checkSAID(checkSaId);
		if(result > 0) {
			returnStr = "EXIST";
		}
		return returnStr;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/moveCityOtherCircle.do", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	public String moveCityOtherCircle(@RequestParam HashMap<String, String> param, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "SUCCESS";
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		int result = circleMapper.moveCityOtherCircle(param);
		if(result > 0) {
			returnStr = "FAIL";
		}
		return returnStr;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/circle/getCityList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getCityList(@RequestBody HashMap<String,String> param) {
		
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		JSONObject jsonResult = new JSONObject();
		
		int offset = Integer.parseInt(String.valueOf(param.get("offset")));
		int limit = Integer.parseInt(String.valueOf(param.get("limit")));
		param.put("start", Integer.toString(offset+1));
		param.put("end", Integer.toString(offset+limit));
		
		List<HashMap<String,String>> cityList = circleMapper.selectCityFromCircleId(param);
		Gson gson = new Gson();
		String str = gson.toJson(cityList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		int total = circleMapper.selectCityFromCircleIdCount(param);
		jsonResult.put("total", total);
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/circle/getCityListSearch.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getCityListSearch(@RequestParam HashMap<String,String> param) {
		
		CircleMapper circleMapper = sqlSession.getMapper(CircleMapper.class);
		JSONObject jsonResult = new JSONObject();
		
		List<HashMap<String,String>> resultList = circleMapper.getCircleCityListSearch(param);
		
		Gson gson = new Gson();
		String str = gson.toJson(resultList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		
		return jsonResult.toString();
		
	}
	
}

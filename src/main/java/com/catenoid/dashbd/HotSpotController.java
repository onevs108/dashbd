package com.catenoid.dashbd;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import com.catenoid.dashbd.dao.HotSpotMapper;
import com.catenoid.dashbd.dao.OperatorMapper;
import com.catenoid.dashbd.dao.model.Circle;
import com.google.gson.Gson;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class HotSpotController {
	
	private static final Logger logger = LoggerFactory.getLogger(HotSpotController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	@Value("#{config['main.contents.max']}")
	private Integer contentMax;
	
	@RequestMapping(value = "/resources/hotspot.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView getServiceAreaMain(HttpServletRequest request) {
		logger.info("/resources/hotspot.do");		
		ModelAndView mv = new ModelAndView("hotspotMgmt");
		
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		List<Circle> circleList = operatorMapper.selectCircleListAll();
		
		mv.addObject("circleList", circleList);
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/hotspot/getCityListFromCircleId.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String getCityListFromCircleId(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		JSONObject jsonResult = new JSONObject();
		
		HotSpotMapper hotSpotMapper = sqlSession.getMapper(HotSpotMapper.class);
		List<HashMap<String,String>> cityList = hotSpotMapper.selectCityFromCircleId(param);
		
		Gson gson = new Gson();
		String str = gson.toJson(cityList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("result", json);
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/hotspot/getHotSpotListFromCityId.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String getHotSpotListFromCityId(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		JSONObject jsonResult = new JSONObject();
		
		HotSpotMapper hotSpotMapper = sqlSession.getMapper(HotSpotMapper.class);
		List<HashMap<String,String>> hotspotList = hotSpotMapper.selectHotSpotFromCityId(param);
		
		Gson gson = new Gson();
		String str = gson.toJson(hotspotList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("result", json);
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/hotspot/getHotSpotList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getHotSpotList(@RequestBody HashMap<String,String> param) {
		
		HotSpotMapper hotSpotMapper = sqlSession.getMapper(HotSpotMapper.class);
		JSONObject jsonResult = new JSONObject();
		
		List<HashMap<String,String>> cityList = hotSpotMapper.selectHotSpotFromCityId(param);
		Gson gson = new Gson();
		String str = gson.toJson(cityList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		int total = hotSpotMapper.selectHotSpotFromCityIdCount(param);
		jsonResult.put("total", total);
		return jsonResult.toString();
		
	}
	
}

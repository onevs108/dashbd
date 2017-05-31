package com.catenoid.dashbd;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.MoodMapper;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.google.gson.Gson;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class MoodController{

//	private static final Logger logger = LoggerFactory.getLogger(MoodController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['b2.database.delete']}")
	private String databaseDelete;

	@RequestMapping(value = "/mood/main.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView systemMgmtView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("moodMain");
		
//		MoodMapper mapper = sqlSession.getMapper(MoodMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		
//		List<Operator> result = mapper.getServiceAreaOperator(searchParam);
		
//		Operator initOperator = result.get(0);
//		
//		searchParam = new OperatorSearchParam();
//		searchParam.setPage((page-1) * perPage);
//		searchParam.setPerPage(perPage);
//		searchParam.setOperatorId(initOperator.getId());
//		
//		List<Bmsc> bmscs = mapper.getSeviceAreaBmSc(searchParam);
//		
//		mv.addObject("OperatorList", result);
//		mv.addObject("BmscList", bmscs);
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mood/searchMoodList.do", method = {RequestMethod.POST}, produces="application/json;charset=UTF-8;")
	@ResponseBody
	public String searchMoodList(@RequestBody String body, HttpServletRequest request) {
//		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		MoodMapper mapper = sqlSession.getMapper(MoodMapper.class);
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);

			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			String searchServiceType = (String) requestJson.get("searchServiceType");
			String searchSchedule = (String) requestJson.get("searchSchedule");
			String searchArea = (String) requestJson.get("searchArea");
			String searchKeyword = (String) requestJson.get("searchKeyword");
			String searchType = (String) requestJson.get("searchType");
			String choiceTreeStr = (String) requestJson.get("choiceTreeStr"); 
			String circle_id = (String) requestJson.get("circle_id");
			String deleveryType = (String) requestJson.get("deleveryType");
			String serviceId = (String) requestJson.get("serviceId");
			
			HashMap<String, Object> searchParam = new HashMap<String, Object>();
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset);
			searchParam.put("end", offset + limit);
			searchParam.put("searchServiceType", searchServiceType);
			searchParam.put("searchSchedule", searchSchedule);
			searchParam.put("searchArea", searchArea);
			searchParam.put("searchType", searchType);
			searchParam.put("searchKeyword", searchKeyword);
			searchParam.put("circle_id", circle_id);
			searchParam.put("deleveryType", deleveryType);
			searchParam.put("serviceId", serviceId);
			
			Gson json = new Gson();
			
			if(!choiceTreeStr.equals("all")) {
				HashMap<String, String> choiceTreeStrList = json.fromJson(choiceTreeStr, HashMap.class);
				
				String circleListStr = choiceTreeStrList.get("circleListStr");
				String cityListStr = choiceTreeStrList.get("cityListStr");
				String hotspotListStr = choiceTreeStrList.get("hotspotListStr");
				
				searchParam.put("circleListStr", circleListStr);
				searchParam.put("cityListStr", cityListStr);
				searchParam.put("hotspotListStr", hotspotListStr);
			}
			
			JSONArray rows = new JSONArray();
			
			List<HashMap<String, Object>> resultList = mapper.selectMoodService(searchParam);
			for(HashMap<String, Object> map : resultList) {
				String tempJsonStr = json.toJson(map);
				rows.add(json.fromJson(tempJsonStr, JSONObject.class));
			}
			
			jsonResult.put("rows", rows);
			
			int total = mapper.selectMoodServiceCount(searchParam);
			jsonResult.put("total", total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult.toString();
	} 
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/mood/getSubMoodData.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String getSubMoodData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultObj = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			MoodMapper mapper = sqlSession.getMapper(MoodMapper.class);
			String layerDiv = request.getParameter("layerDiv");
			String serviceId = request.getParameter("serviceId");
			String psaid = request.getParameter("psaid");
			String searchServiceType = request.getParameter("searchServiceType");
			String searchSchedule = request.getParameter("searchSchedule");
			String searchType = request.getParameter("searchType");
			String searchKeyword = request.getParameter("searchKeyword");
			String choiceTreeStr = request.getParameter("choiceTreeStr");
			
			HashMap< String, Object > searchParam = new HashMap<String, Object>();
			searchParam.put("layerDiv", layerDiv);
			searchParam.put("serviceId", serviceId);
			searchParam.put("psaid", psaid);
			searchParam.put("searchServiceType", searchServiceType);
			searchParam.put("searchSchedule", searchSchedule);
			searchParam.put("searchType", searchType);
			searchParam.put("searchKeyword", searchKeyword);
			
			if(!choiceTreeStr.equals("all")) {
				Gson json = new Gson();
				HashMap<String, String> choiceTreeStrList = json.fromJson(choiceTreeStr, HashMap.class);
				
				String circleListStr = choiceTreeStrList.get("circleListStr");
				String cityListStr = choiceTreeStrList.get("cityListStr");
				String hotspotListStr = choiceTreeStrList.get("hotspotListStr");
				
				searchParam.put("circleListStr", circleListStr);
				searchParam.put("cityListStr", cityListStr);
				searchParam.put("hotspotListStr", hotspotListStr);
			}
			
			List<HashMap<String, Object>> resultList = mapper.selectMoodService(searchParam);
			
			for (int i = 0; i < resultList.size(); i++) {
				org.json.JSONObject temp = new org.json.JSONObject();
				String area = "";
				temp.put("serviceId", resultList.get(i).get("serviceId"));
				temp.put("said", resultList.get(i).get("said"));
				temp.put("serviceName", resultList.get(i).get("service_name"));
				if(resultList.get(i).get("circle") != null) {
					area = resultList.get(i).get("circle").toString();
				} else {
					if(resultList.get(i).get("city") != null) {
						area = resultList.get(i).get("city").toString();
					} else {
						area = resultList.get(i).get("hotspot").toString();
					}
				}
				temp.put("area", area);
				temp.put("serviceType", resultList.get(i).get("service"));
				temp.put("serviceMode", resultList.get(i).get("serviceMode"));
				temp.put("deleveryType", resultList.get(i).get("mode"));
				temp.put("countUC", resultList.get(i).get("countUC"));
				temp.put("countBC", resultList.get(i).get("countBC"));
				temp.put("viewers", Integer.parseInt(resultList.get(i).get("countUC").toString()) + Integer.parseInt(resultList.get(i).get("countBC").toString()));
				resultArray.add(temp);
			}
			
			resultObj.put("resultList", resultArray);
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return resultObj.toJSONString();
	}
	
	@RequestMapping(value = "/mood/getMoodStats.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String getMoodStats(@RequestParam HashMap<String, Object> param, ModelMap model) {
		MoodMapper mapper = sqlSession.getMapper(MoodMapper.class);
		try {
			List<HashMap<String, Object>> moodList = mapper.getMoodHistory(param);
			Gson gson = new Gson();
			String moodData = gson.toJson(moodList);
			model.addAttribute("moodData", moodData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "moodStats";
	}
	
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mood/searchMoodList.do", method = {RequestMethod.POST}, produces="application/json;charset=UTF-8;")
	@ResponseBody
	public String searchMoodList(@RequestBody String body, HttpServletRequest request) {
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
//		MoodMapper mapper = sqlSession.getMapper(MoodMapper.class);
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);

			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			String searchServiceType = (String) requestJson.get("searchServiceType");
			String searchSchedule = (String) requestJson.get("searchSchedule");
			String searchDateFrom = (String) requestJson.get("searchDateFrom");
			String searchDateTo = (String) requestJson.get("searchDateTo");
			String searchKeyword = (String) requestJson.get("searchKeyword");
			String searchType = (String) requestJson.get("searchType");
			String choiceTreeStr = (String) requestJson.get("choiceTreeStr"); 
			String circle_id = (String) requestJson.get("circle_id");
			
			Users user = (Users)request.getSession().getAttribute("USER");
			HashMap<String, Object> searchParam = new HashMap<String, Object>();
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset+1);
			searchParam.put("end", offset + limit);
			searchParam.put("searchServiceType", searchServiceType);
			searchParam.put("searchSchedule", searchSchedule);
			searchParam.put("searchDateFrom", searchDateFrom);
			searchParam.put("searchDateTo", searchDateTo); 
			searchParam.put("searchType", searchType);
			searchParam.put("searchKeyword", searchKeyword);
			searchParam.put("circle_id", circle_id);
			
			Gson json = new Gson();
			
			if(!choiceTreeStr.equals("all")) {
				HashMap<String, String> choiceTreeStrList = json.fromJson(choiceTreeStr, HashMap.class);
				
				String circleListStr = choiceTreeStrList.get("circleListStr");
				String cityListStr = choiceTreeStrList.get("cityListStr");
				String hotspotListStr = choiceTreeStrList.get("hotspotListStr");
				
				searchParam.put("circleListStr", circleListStr);
				searchParam.put("cityListStr", cityListStr);
				searchParam.put("hotspotListStr", hotspotListStr);
			}
			
			JSONArray rows = new JSONArray();
			
			List<HashMap<String, Object>> resultList = mapper.selectRegionalSchedule(searchParam);
			for(HashMap<String, Object> map : resultList) {
				String tempJsonStr = json.toJson(map);
				rows.add(json.fromJson(tempJsonStr, JSONObject.class));
			}
			
			jsonResult.put("rows", rows);
			
			int total = mapper.selectRegionalScheduleCount(searchParam);
			jsonResult.put("total", total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult.toString();
	}*/ 
	
}
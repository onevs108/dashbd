package com.catenoid.dashbd;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.BmscMapper;
import com.catenoid.dashbd.dao.ContentsMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.Embms;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.service.BmscService;
import com.catenoid.dashbd.service.OperatorService;

/**
 * BM-SC 관리 Controller
 * 
 * @author iskwon
 */
@Controller
public class BmscController {
	
	private static final Logger logger = LoggerFactory.getLogger(BmscController.class);
	
	@Autowired
	private BmscService bmscServiceImpl;
	@Autowired
	private OperatorService operatorServiceImpl;
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * BMSC 관리 페이지 이동
	 */
	@RequestMapping(value = "/resources/bmsc.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getBmscMgmt(ModelMap modelMap) {
		logger.info("-> []");
		
		List<Operator> operatorList = operatorServiceImpl.getOperatorListAll();
		modelMap.addAttribute("operatorList", operatorList);
		
		logger.info("<- [operatorListSize = {}]", operatorList.size());
		return "bmsc/bmscMgmt";
	}
	
	/**
	 * BMSC 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/bmsc/list.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postBmscList(
			@RequestBody String body) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String operatorIdToStr = (String) requestJson.get("operatorId");
			Integer operatorId = operatorIdToStr == null ? null : Integer.parseInt(operatorIdToStr);
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			JSONArray rows = bmscServiceImpl.getBmscListToJsonArray(operatorId, sort, order, offset, limit);
			jsonResult.put("rows", rows);
			int total = bmscServiceImpl.getBmscListCount(operatorId);
			jsonResult.put("total", total);
			
			logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	/**
	 * Bmsc 정보 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/bmsc/info.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postBmscInfo(
			@RequestParam(value = "bmscId", required = true) Integer bmscId) {
		logger.info("-> [bmscId = {}]", bmscId);
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			Bmsc bmsc = bmscServiceImpl.getBmsc(bmscId);
			jsonResult.put("bmsc", bmsc.toJSONObject());
		} catch (Exception e) {
			logger.error("~~ [An error occurred!]", e);
		}
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Bmsc 등록 및 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/bmsc/insert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postBmscInsert(
			@ModelAttribute Bmsc bmsc) {
		
		logger.info("-> [operator = {}]", bmsc.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", bmscServiceImpl.insertBmsc(bmsc));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Bmsc 삭제
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/bmsc/delete.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postBmscDelete(
			@RequestParam(value = "bmscId", required = true) Integer bmscId) {
		logger.info("-> [bmscId = {}]", bmscId);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", bmscServiceImpl.deleteBmsc(bmscId));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}



	
	
	/**
	 * embms 등록 및 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/bmsc/embmsInsert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postEmbmsInsert(
			@ModelAttribute Embms embms) {
		
		logger.info("-> [operator = {}]", embms.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", bmscServiceImpl.insertEmbms(embms));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Bmsc 삭제
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/bmsc/embmsDel.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String embmsDel(
			@RequestParam(value = "embmsId", required = true) Integer bmscId) {
		logger.info("-> [bmscId = {}]", bmscId);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", bmscServiceImpl.deleteEmbms(bmscId));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	@RequestMapping( value = "api/bmsc/embmsList.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public Map< String, Object > embmsList( @RequestParam Map< String, Object > params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		BmscMapper mapper = sqlSession.getMapper(BmscMapper.class);
		
		List<Map> list = mapper.selectEmbms(params);
		
        Map< String, Object > returnMap = new HashMap< String, Object >();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", "SUCCESS");
        
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
        resultMap.put( "contents", list );
        
		return (Map<String, Object>) resultMap;
	}
}

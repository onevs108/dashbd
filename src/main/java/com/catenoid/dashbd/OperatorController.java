package com.catenoid.dashbd;


import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.service.OperatorService;

/**
 * Operator 관리 Controller
 * 
 * @author iskwon
 */
@Controller
public class OperatorController {
	
	private static final Logger logger = LoggerFactory.getLogger(OperatorController.class);
	
	@Autowired
	private OperatorService operatorServiceImpl;
	
	/**
	 * Operator 관리 페이지 이동
	 */
	@RequestMapping(value = "/resources/operator.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getOperatorMgmt(Model model) {
		logger.info("-> []");
		//circle 리스트 가져오기
		List<Circle> circleList = operatorServiceImpl.getCircleListAll();
		model.addAttribute("circleList", circleList);
		return "operator/operatorMgmt";
	}
	
	/**
	 * Operator 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/list.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOperatorList(
			@RequestBody String body) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);

			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			JSONArray rows = operatorServiceImpl.getOperatorListToJsonArray(sort, order, offset, limit);
			jsonResult.put("rows", rows);
			int total = operatorServiceImpl.getGradeListCount();
			jsonResult.put("total", total);
			
			logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	/**
	 * Operator 정보 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/info.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOeratorInfo(
			@RequestParam(value = "operatorId", required = true) Integer operatorId) {
		logger.info("-> [operatorId = {}]", operatorId);
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			Operator operator = operatorServiceImpl.getOperator(operatorId);
			jsonResult.put("operator", operator.toJSONObject());
		} catch (Exception e) {
			logger.error("~~ [An error occurred!]", e);
		}
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Grade 등록 및 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/grade/insert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postGradeInsert(@ModelAttribute Operator operator) {
		logger.info("-> [operator = {}]", operator.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.insertGrade(operator));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Operator 등록 및 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/insert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOperatorInsert(@ModelAttribute Operator operator) {
		logger.info("-> [operator = {}]", operator.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.insertOperator(operator));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Operator 삭제
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/delete.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOperatorDelete(
			@RequestParam(value = "operatorId", required = true) Integer operatorId) {
		logger.info("-> [operatorId = {}]", operatorId);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.deleteOperator(operatorId));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Grade Name 중복 체크
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/grade/check.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postGradeCheck(@RequestParam(value = "operatorName", required = true) String operatorName) {
		logger.info("-> [operatorName = {}]", operatorName);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.checkGradeName(operatorName));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * Operator Name 중복 체크
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/operator/check.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postOperatorCheck(@RequestParam(value = "operatorName", required = true) String operatorName) {
		logger.info("-> [operatorName = {}]", operatorName);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", operatorServiceImpl.checkOperatorName(operatorName));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
}

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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.service.OperatorService;
import com.catenoid.dashbd.service.PermissionService;

/**
 * 권한 관리 Controller
 * 
 * @author iskwon
 */
@Controller
public class PermissionController {
	
	private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
	
	@Autowired
	private PermissionService permissionServiceImpl;
	@Autowired
	private OperatorService operatorServiceImpl;
	
	/**
	 * Permission 관리 페이지 이동
	 */
	@RequestMapping(value = "/resources/permission.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getPermissionMgmt(ModelMap modelMap) {
		logger.info("-> []");
		
		List<Permission> permissionList = permissionServiceImpl.getPermissionList(null);
		modelMap.addAttribute("permissionList", permissionList);
		
		List<Operator> operatorList = operatorServiceImpl.getOperatorListAll();
		modelMap.addAttribute("operatorList", operatorList);
		
		logger.info("<- [permissionListSize = {}], [operatorListSize = {}]", permissionList.size(), operatorList.size());
		return "permission/permissionMgmt";
	}
	
	/**
	 * 사용자 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/permission/userlist.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserList(
			@RequestBody String body) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String searchOperatorId = (String) requestJson.get("searchOperatorId");
			String searchUserId = (String) requestJson.get("searchUserId");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			JSONArray rows = permissionServiceImpl.getUserListToJsonArray(searchOperatorId, searchUserId, offset, limit);
			jsonResult.put("rows", rows);
			int total = permissionServiceImpl.getUserListCount(searchOperatorId, searchUserId);
			jsonResult.put("total", total);
			
			logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	/**
	 * 사용자의 Permission 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/permission/list.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postPermissionList(
			@RequestParam(value = "userId", required = true) String userId) {
		logger.info("-> [userId = {}]", userId);
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			jsonResult.put("permissionList", permissionServiceImpl.getPermissionListToJsonArray(userId));
		} catch (Exception e) {
			logger.error("~~ [An error occurred!]", e);
		}
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * 사용자의 Permission 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/permission/insert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postPermissionInsert(
			@RequestParam(value = "body", required = true) String body) {
		
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String userId = (String) requestJson.get("userId");
			List<String> permissions = (List<String>) requestJson.get("permissions");
			
			jsonResult.put("result", permissionServiceImpl.insertUserPermission(userId, permissions));
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
			jsonResult.put("result", false);
		}
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
}

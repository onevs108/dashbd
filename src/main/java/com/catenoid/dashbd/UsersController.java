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

import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.OperatorService;
import com.catenoid.dashbd.service.UserService;

/**
 * 사용자 관리 Controller
 * 
 * @author iskwon
 */
@Controller
public class UsersController {
	
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private OperatorService operatorServiceImpl;
	
	/**
	 * 사용자 관리 페이지 이동
	 */
	@RequestMapping(value = "/resources/user.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getUserMgmt(
			@RequestParam(value = "isBack", required = false) Boolean isBack,
			Model model) {
		logger.info("-> [isBack = {}]", isBack);
	
		model.addAttribute("isBack", isBack == null ? false : isBack);
		
		List<Operator> operatorList = operatorServiceImpl.getOperatorListAll();
		model.addAttribute("operatorList", operatorList);
		
		logger.info("<- [operatorListSize = {}]", operatorList.size());
		return "user/userMgmt";
	}
	
	/**
	 * 사용자 관리 Form 페이지 이동
	 */
	@RequestMapping(value = "/resources/userform.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getLogin(
			@RequestParam(value = "flag", required = false) String flag, // 등록, 상세, 수정 구분
			@RequestParam(value = "userId", required = false) String userId,
			Model model) {
		logger.info("-> [flag = {}], [userId = {}]", flag, userId);
		
		model.addAttribute("flag", flag);
		
		List<Operator> operatorList = operatorServiceImpl.getOperatorListAll();
		model.addAttribute("operatorList", operatorList);
		
		if (userId != null)
			model.addAttribute("userId", userId); // 상세, 수정 구분 용
		
		logger.info("<- [flag = {}], [operatorListSize = {}]", flag, operatorList.size());
		return "user/userMgmtForm";
	}
	
	/**
	 * 사용자 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/list.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserList(
			@RequestBody String body) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String searchColumn = (String) requestJson.get("searchColumn");
			String searchKeyword = (String) requestJson.get("searchKeyword");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			JSONArray rows = userServiceImpl.getUserListToJsonArray(searchColumn, searchKeyword, offset, limit);
			jsonResult.put("rows", rows);
			int total = userServiceImpl.getUserListCount(searchColumn, searchKeyword);
			jsonResult.put("total", total);
			
			logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	/**
	 * 사용자 정보 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/info.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserInfo(
			@RequestParam(value = "userId", required = true) String userId) {
		logger.info("-> [userId = {}]", userId);
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			Users reqUser = new Users();
			reqUser.setUserId(userId);
			
			Users user = userServiceImpl.getUser(reqUser);
			jsonResult.put("user", user.toJSONObject());
		} catch (Exception e) {
			logger.error("~~ [An error occurred!]", e);
		}
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * 사용자 등록 및 수정
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/insert.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserInsert(
			@ModelAttribute Users user) {
		logger.info("-> [user = {}]", user.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", userServiceImpl.insertUser(user));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * 사용자 삭제
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/delete.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserDelete(
			@ModelAttribute Users user) {
		logger.info("-> [user = {}]", user.toString());
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", userServiceImpl.deleteUser(user));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * 사용자 ID 중복 확인
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/check.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postCheckUser(
			@RequestParam(value = "userId", required = true) String userId) {
		logger.info("-> [userId = {}]", userId);
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result", userServiceImpl.checkUserId(userId));
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
}

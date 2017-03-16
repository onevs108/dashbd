package com.catenoid.dashbd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.OperatorService;
import com.catenoid.dashbd.service.PermissionService;
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
	private SqlSession sqlSession;
	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private OperatorService operatorServiceImpl;
	@Autowired
	private PermissionService permissionServiceImpl;
	
	/**
	 * 사용자 관리 페이지 이동
	 */
	@RequestMapping(value = "/resources/user.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getUserMgmt(@RequestParam(value = "isBack", required = false) Boolean isBack, HttpSession session, ModelMap modelMap) {
		logger.info("-> [isBack = {}]", isBack);
		
		Users user = (Users) session.getAttribute("USER");
		String circleName = user.getCircleName();
		if(circleName != null) {
			List<Circle> townList = userServiceImpl.selectTownFromCircle(circleName);
			modelMap.addAttribute("townList", townList);
		}
		
		modelMap.addAttribute("isBack", isBack == null ? false : isBack);
		
		List<Operator> gradeList = operatorServiceImpl.getGradeListAll();
		List<Circle> circleList = operatorServiceImpl.getCircleListNameAll();
		
		modelMap.addAttribute("gradeList", gradeList);
		modelMap.addAttribute("circleList", circleList);
		
		return "user/userMgmt";
	}
	
	/**
	 * 사용자 관리 Form 페이지 이동
	 */
	@RequestMapping(value = "/resources/userform.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getLogin(@RequestParam(value = "flag", required = false) String flag, // 등록, 상세, 수정 구분
						   @RequestParam(value = "userId", required = false) String userId,
						   Model model) {
		logger.info("-> [flag = {}], [userId = {}]", flag, userId);
		
		model.addAttribute("flag", flag);
		
		List<Operator> gradeList = operatorServiceImpl.getGradeListAll();
		List<Circle> circleList = operatorServiceImpl.getCircleListAll();
		
		model.addAttribute("gradeList", gradeList);
		model.addAttribute("circleList", circleList);
		
		if (userId != null)
			model.addAttribute("userId", userId); // 상세, 수정 구분 용
		
		return "user/userMgmtForm";
	}
	
	/**
	 * 사용자 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/list.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserList(
			@RequestBody String body,
			HttpServletRequest request) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String searchOperatorId = (String) requestJson.get("searchOperatorId");
			String searchKeyword = (String) requestJson.get("searchKeyword");
			String searchColumn = (String) requestJson.get("searchColumn");
			String searchCircleId = (String) requestJson.get("searchCircleId");
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			HttpSession session = request.getSession(false);
			if (session != null) {
				Users user = (Users) session.getAttribute("USER");
				if (user != null) {
					Integer operatorId = searchOperatorId == null || searchOperatorId.isEmpty() ? null : Integer.parseInt(searchOperatorId);
					JSONArray rows = userServiceImpl.getUserListToJsonArray(searchColumn, searchKeyword, operatorId, sort, order, offset, limit, searchCircleId);
					jsonResult.put("rows", rows);
					int total = userServiceImpl.getUserListCount(searchColumn, searchKeyword, operatorId, searchCircleId);
					jsonResult.put("total", total);
					
					logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
				}
			}
			else {
				logger.info("<- [Session is null!!]");
			}
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
	public String postUserInfo(@RequestParam(value = "userId", required = true) String userId) {
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
			@ModelAttribute Users user,
			HttpServletRequest request) {
		
		Users LogUser = (Users)request.getSession().getAttribute("USER");
		HashMap<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("reqType", "Operaotor");
		
		if(!userServiceImpl.checkUserId(user.getUserId())) {
			logMap.put("reqSubType", "Add Operator");
			logMap.put("reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId() + " - Add Operator (userid:" + user.getUserId() + ")");
		} else {
			logMap.put("reqSubType", "Edit Operator");
			logMap.put("reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId() + " - Edit Operator (userid:" + user.getUserId() + ")");
		}
		
		logMap.put("reqUrl", "/api/user/insert.do");
		logMap.put("reqCode", "SUCCESS");
		logMap.put("targetId", LogUser.getUserId());
		UsersMapper logMapper = sqlSession.getMapper(UsersMapper.class);
		logMapper.insertSystemAjaxLog(logMap);
		
		JSONObject jsonResult = new JSONObject();
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			logger.info("-> [Session is null!]");
			return jsonResult.toString();
		}
		
		Users userOfSession = (Users) session.getAttribute("USER");
		if (userOfSession == null) {
			logger.info("-> [Session is null!]");
			return jsonResult.toString();
		}
		
		logger.info("-> [user = {}], [userOfSession = {}]", user.toString(), userOfSession.toString());

		insertPermission(user);
		jsonResult.put("result", userServiceImpl.insertUser(user));

		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		
		return jsonResult.toString();
	}
	
	public void insertPermission(Users addUser) {
		Operator operator = null;
		if(addUser.getOperatorId() != null){
			operator = operatorServiceImpl.getOperator(addUser.getOperatorId());
		}else{
			operator = operatorServiceImpl.selectByGradeName(addUser.getOperatorName());
		}
		String[] permission = operator.getPermission().split(",");
		List<String> permissions = new ArrayList<String>();
		for (int i = 0; i < permission.length; i++) {
			permissions.add(permission[i]);
		}
		permissionServiceImpl.insertUserPermission(addUser.getUserId(), permissions);
	}
	
	/**
	 * 사용자 삭제
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/delete.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserDelete(
			@ModelAttribute Users user,
			HttpServletRequest request) {
		
		JSONObject jsonResult = new JSONObject();
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			logger.info("-> [Session is null!]");
			return jsonResult.toString();
		}
		
		Users userOfSession = (Users) session.getAttribute("USER");
		if (userOfSession == null) {
			logger.info("-> [Session is null!]");
			return jsonResult.toString();
		}
		
		logger.info("-> [user = {}], [userOfSession = {}]", user.toString(), userOfSession.toString());
		
		// 사용자를 삭제하려는 사용자가 일반 사용자인 경우 삭제 대상의 Operator와 일치해야 한다.
		// 이 확인은 Security가 대신 해줄수 없기 때문에 직접 해준다.
		if (userOfSession.getGrade() == Const.USER_GRADE_ADMIN)
			jsonResult.put("result", userServiceImpl.deleteUser(user));
		else if (userOfSession.getGrade() == Const.USER_GRADE_USER && userOfSession.getOperatorId() == user.getOperatorId())
			jsonResult.put("result", userServiceImpl.deleteUser(user));
		else
			logger.info("~~ [Operator was incorrect!]");
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		
		Users LogUser = (Users)request.getSession().getAttribute("USER");
		HashMap<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("reqType", "Operator");
		logMap.put("reqSubType", "Delete Operator");
		logMap.put("reqUrl", "/api/user/delete.do");
		logMap.put("reqCode", "SUCCESS");
		logMap.put("targetId", LogUser.getUserId());
		logMap.put("reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId() + " - Delete Operator (userid:" + user.getUserId() + ")");
		UsersMapper logMapper = sqlSession.getMapper(UsersMapper.class);
		logMapper.insertSystemAjaxLog(logMap);
		
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
	
	/**
	 * TownList 가져오기
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/getTownFromCircle.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getTownFromCircle(@RequestParam(value = "circleName", required = true) String circleName) {
		logger.info("-> [circleName = {}]", circleName);
		
		JSONObject jsonResult = new JSONObject();
		
		List<Circle> townList = userServiceImpl.selectTownFromCircle(circleName);
		JSONArray array = new JSONArray();
		for(int i = 0; i < townList.size(); i++) {
			Circle data = townList.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", data.getId());
			obj.put("circle_id", data.getCircle_id());
			obj.put("circle_name", data.getCircle_name());
			obj.put("town_name", data.getTown_name());
			obj.put("town_code", data.getTown_code());
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			array.add(obj);
		}
		jsonResult.put("result", array);
		return jsonResult.toJSONString();
	}
	
	private String getFormatDateTime(Date date, String format) {
		if(date == null) return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	/**
	 * 사용자 비밀번호 초기화
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/initPassword.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String initPassword(HttpServletRequest request) {
		
		JSONObject jsonResult = new JSONObject();
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			logger.info("-> [Session is null!]");
			return jsonResult.toString();
		}
		
		Users userOfSession = (Users) session.getAttribute("USER");
		if (userOfSession == null) {
			logger.info("-> [Session is null!]");
			return jsonResult.toString();
		}
		
		try {
			String userId = request.getParameter("userId");
			String initPassword = "9999";
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("password", initPassword);
			param.put("status", "init");
			
			int resultCnt = userServiceImpl.initPassword(param);
			
			if(resultCnt > 0) {
				jsonResult.put("resultCode", "S");
			} else {
				jsonResult.put("resultCode", "F");
				logger.error("<- User Password Init Fail! : [jsonResult = {}]", jsonResult.toString());
			}
			
			Users LogUser = (Users)request.getSession().getAttribute("USER");
			HashMap<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("reqType", "Operator");
			logMap.put("reqSubType", "Password Reset");
			logMap.put("reqUrl", "/api/user/initPassword.do");
			logMap.put("reqCode", "SUCCESS");
			logMap.put("targetId", LogUser.getUserId());
			logMap.put("reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId() + " - Password Reset (userid:" + userId + ")");
			UsersMapper logMapper = sqlSession.getMapper(UsersMapper.class);
			logMapper.insertSystemAjaxLog(logMap);
			
		} catch(Exception e) {
			jsonResult.put("resultCode", "F");
			logger.error("<- User Password Init Fail! : " + e.getMessage());
		}
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	} 
}

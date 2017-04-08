package com.catenoid.dashbd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.catenoid.dashbd.dao.StatusNotifyMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.dao.model.StatusNotifyWithBLOBs;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.UserService;

import catenoid.net.msg.XmlPara;
import catenoid.net.msg.XmlParaSet;
import catenoid.net.msg.XmlTlv;
import catenoid.net.tools.XmlFormer;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private SqlSession sqlSession; 
	
	@Autowired
	private UserService userServiceImpl;
	
	/**
	 * 로그인 페이지 이동
	 * 
	 * @author iskwon
	 */
	@RequestMapping(value = { "/", "/login.do" }, method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String doLogin(ModelMap modelMap) {
		logger.info("-> []");
		
//		OperatorMapper mapper = sqlSession.getMapper(OperatorMapper.class);
//		List<Operator> operatorList = mapper.selectOperatorListAll();
//		modelMap.addAttribute("operatorList", operatorList);
//		
//		logger.info("<- [operatorListSize = {}]", operatorList.size());
		return "login";
	}
	
	/**
	 * 로그아웃
	 * 
	 * @author iskwon
	 */
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public String doLogoutPage(HttpServletRequest request) {
		Users LogUser = (Users) request.getSession().getAttribute("USER");
		
		if(LogUser != null) {
			String ip = request.getHeader("X-FORWARDED-FOR");
	        if (ip == null)
	            ip = request.getRemoteAddr();
			
			HashMap<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("reqType", "Login");
			logMap.put("reqSubType", "Logout");
			logMap.put("reqUrl", "logout.do");
			logMap.put("reqCode", "SUCCESS");
			logMap.put("targetId", LogUser.getUserId());
			logMap.put("reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId() + " - Logout (IP address : " + ip + ")");
			UsersMapper logMapper = sqlSession.getMapper(UsersMapper.class);
			logMapper.insertSystemAjaxLog(logMap);
	        
			sessionRegistry.removeSessionInformation(request.getSession().getId());
			request.getSession().invalidate();
		}
		
		logger.info("-> []");
		
		logger.info("<- []");
		return "redirect:/login.do";
	}
	
	/**
	 * 로그인 실패 시 실패 화면 이동
	 * 
	 * @author iskwon
	 */
	@RequestMapping(value = "/loginfail.do", method = RequestMethod.GET)
	public String doLoginFail(
			@RequestParam(value = "cause", required = true) Integer cause,
			HttpServletRequest request,
			ModelMap modelMap) {
		logger.info("-> [cause = {}]", cause);
		
		modelMap.put("cause", cause);
		modelMap.put("userId", request.getSession().getAttribute("userId"));
		request.getSession().invalidate();
		
		logger.info("<- [cause = {}]", cause);
		return "login_fail";
	}
	
	/**
	 * 패스워드 변경 페이지 이동
	 * 
	 * @author iskwon
	 */
	@RequestMapping(value = { "/", "/change_password.do" }, method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded")
	public String change_password(HttpServletRequest request, ModelMap modelMap) {
		modelMap.put("userId", request.getParameter("userId"));
		return "change_password";
	}
	
	/**
	 * 사용자 비밀번호 변경
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/changePassword.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String changePassword(HttpServletRequest request) {
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			String userId = request.getParameter("userId");
			String password = request.getParameter("password");
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("password", password);
			param.put("status", "normal");
			
			int resultCnt = userServiceImpl.initPassword(param);
			
			if(resultCnt > 0) {
				jsonResult.put("resultCode", "S");
			} else {
				jsonResult.put("resultCode", "F");
				logger.error("<- User Password Init Fail! : [jsonResult = {}]", jsonResult.toString());
			}
		} catch(Exception e) {
			jsonResult.put("resultCode", "F");
			logger.error("<- User Password Init Fail! : " + e.getMessage());
		}
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}
	
	/**
	 * session 만료 시 처리
	 * 
	 * @author iskwon
	 */
	@RequestMapping(value = "/sessiontimeout.do", method = RequestMethod.GET)
	public String doSessionTimeout() {
		logger.info("-> []");
		
		logger.info("<- []");
		return "error/session_expired";
	}
	
	/**
	 * 메뉴 리스트 리턴
	 */
	@RequestMapping(value = "/api/menu.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doManu(HttpServletRequest request) {
		logger.info("-> []");
		
		logger.info("<- []");
		return getMenuList(request);
//		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	@SuppressWarnings("unchecked")
	private ResponseEntity<String> getMenuList(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			StringBuilder menuHtml = new StringBuilder();
			String currentMenu = (String) request.getParameter("currentMenu");
			Users user = (Users) request.getSession(false).getAttribute("USER");
			if (user == null) {
				logger.info("<- [Session is null!!]");
				root.put("result", 1);
				return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
			}
			root.put("userFirstName", user.getFirstName());
			root.put("userLastName", user.getLastName());
			List<Permission> permissions = user.getPermissions();
			
			if (user.getGrade() == Const.USER_GRADE_ADMIN) {
//				menuHtml.append(currentMenu.equals(Const.MENU_USER_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"\"><i class=\"fa fa-user\"></i> <span class=\"nav-label\">User Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_PERMISSION_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/permission.do\"><i class=\"fa fa-lock\"></i> <span class=\"nav-label\">Permission Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_CONTENTS_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/view/content.do\"><i class=\"fa fa-file\"></i> <span class=\"nav-label\">Contents Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_OPERATOR_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/operator.do\"><i class=\"fa fa-envelope\"></i> <span class=\"nav-label\">Group Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_BMSC_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/bmsc.do\"><i class=\"fa fa-flag\"></i> <span class=\"nav-label\">BM-SC Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_CIRCLE_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/circle.do\"><i class=\"fa fa-flag\"></i> <span class=\"nav-label\">Circle & City Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_HOTSPOT_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/hotspot.do\"><i class=\"fa fa-flag\"></i> <span class=\"nav-label\">Hot spot Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_AREA_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/serviceArea.do\"><i class=\"fa fa-globe\"></i> <span class=\"nav-label\">Service Area Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_AREA_GROUP_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/serviceAreaGroup.do\"><i class=\"fa fa-globe\"></i> <span class=\"nav-label\">Service Area Group Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_ENB_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/eNBMgmt.do\"><i class=\"fa fa-puzzle-piece\"></i> <span class=\"nav-label\">eNB Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SCHEDULE_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/view/schdMgmt.do\"><i class=\"fa fa-calendar\"></i> <span class=\"nav-label\">Schedule Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"#\"><i class=\"fa fa-th-large\"></i> <span class=\"nav-label\">System Mgmt</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_CONF_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemConfMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">System Config</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_DB_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemDbMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">DB Backup & Restore</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"#\" onclick=\"javascript:goSystemMgmt();return false;\"><i class=\"fa fa-th-large\"></i> <span class=\"nav-label\">System Mgmt</span><span class=\"fa arrow\"></span></a><ul class=\"nav nav-second-level collapse\"><li class=\"\"><a href=\"#\" onclick=\"javascript:goSystemMgmt();return false;\"><i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">Statistic</span></a></li><li><a href=\"#\"><i class=\"fa fa-gear\"></i> <span class=\"nav-label\">System Configuration</span></a></li><li><a href=\"#\"><i class=\"fa fa-database\"></i> <span class=\"nav-label\">DB Backup/Restore</span></a></li></ul></li>");
				
				menuHtml.append(currentMenu.equals(Const.MENU_OPERATOR_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/user.do'><i class='fa fa-th-large'></i> <span class='nav-label'>Operator Mgmt</span> <span class='fa arrow'></span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_OPERATOR_GROUP_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/operator.do'><i class='fa fa-group'></i> <span class='nav-label'>Operator Group Mgmt</span><span class='fa arrow'></span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_CONTENTS_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/view/content.do\"><i class=\"fa fa-file\"></i> <span class=\"nav-label\">Contents Mgmt</span><span class='fa arrow'></span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_CIRCLE_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/circle.do'><i class='fa fa-dot-circle-o'></i> <span class='nav-label'>Circle &amp; City Mgmt</span><span class='fa arrow'></span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_HOTSPOT_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/hotspot.do'><i class='fa fa-spotify'></i> <span class='nav-label'>Hot spot Mgmt </span><span class='fa arrow'></span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_AREA_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/serviceArea.do'><i class='fa fa-pie-chart'></i> <span class='nav-label'>Service Area Mgmt</span><span class='fa arrow'></span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_AREA_GROUP_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/serviceAreaGroup.do'><i class='fa fa-flask'></i> <span class='nav-label'>Service Area Group Mgmt</span><span class='fa arrow'></span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_SCHEDULE_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/view/schdMgmtDetail.do?bmscId=793'><i class='fa fa-edit'></i> <span class='nav-label'>Schedule Mgmt</span><span class='fa arrow'></span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_CLASS_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/view/schdMgmt.do'><i class='fa fa-desktop'></i> <span class='nav-label'>Service Class Mgmt</span><span class='fa arrow'></span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SESSION_MONITORING) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='#'><i class='fa fa-desktop'></i> <span class='nav-label'>Sessions Monitoring</span><span class='fa arrow'></span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='#'><i class='fa fa-desktop'></i> <span class='nav-label'>System Mgmt</span><span class='fa arrow'></span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_LOG_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/logMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">Log</span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_CONF_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemConfMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">System View</span></a></li>");
				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_DB_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemDbMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">Database Backup</span></a></li>");
//				menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">Statistic</span></a></li>");
			}
			else {
				for (Permission permission : permissions) {
					if (permission.getRole().equals(Const.ROLE_ADMIN)); // 위 에서 처리 함
//					else if (permission.getRole().equals(Const.ROLE_PERMISSION_MGMT))
//						menuHtml.append(currentMenu.equals(Const.MENU_PERMISSION_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/permission.do\"><i class=\"fa fa-lock\"></i> <span class=\"nav-label\">Permission Mgmt</span></a></li>");
					else if (permission.getRole().equals(Const.ROLE_OPERATOR_MGMT))
						menuHtml.append(currentMenu.equals(Const.MENU_OPERATOR_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/user.do'><i class='fa fa-th-large'></i> <span class='nav-label'>Operator Mgmt</span> <span class='fa arrow'></span></a></li>");
					else if (permission.getRole().equals(Const.ROLE_OPERATOR_GROUP_MGMT))
						menuHtml.append(currentMenu.equals(Const.MENU_OPERATOR_GROUP_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='/dashbd/resources/operator.do'><i class='fa fa-group'></i> <span class='nav-label'>Operator Group Mgmt</span><span class='fa arrow'></span></a></li>");
					else if (permission.getRole().equals(Const.ROLE_CONTENTS_MGMT))
						menuHtml.append(currentMenu.equals(Const.MENU_CONTENTS_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/view/content.do\"><i class=\"fa fa-file\"></i> <span class=\"nav-label\">Contents Mgmt</span><span class='fa arrow'></span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_BMSC_MGMT))
//						menuHtml.append(currentMenu.equals(Const.MENU_BMSC_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/bmsc.do\"><i class=\"fa fa-flag\"></i> <span class=\"nav-label\">BM-SC Mgmt</span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_CIRCLE_MGMT))
//						menuHtml.append(currentMenu.equals(Const.ROLE_CIRCLE_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/circle.do\"><i class=\"fa fa-flag\"></i> <span class=\"nav-label\">Circle & City Mgmt</span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_HOTSPOT_MGMT))
//						menuHtml.append(currentMenu.equals(Const.ROLE_HOTSPOT_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/hotspot.do\"><i class=\"fa fa-flag\"></i> <span class=\"nav-label\">Hot spot Mgmt</span></a></li>");
					else if (permission.getRole().equals(Const.ROLE_SERVICE_AREA_MGMT))
						menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_AREA_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/serviceArea.do\"><i class=\"fa fa-globe\"></i> <span class=\"nav-label\">Service Area Mgmt</span><span class='fa arrow'></span></a></li>");
					else if (permission.getRole().equals(Const.ROLE_SERVICE_AREA_GROUP_MGMT))
						menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_AREA_GROUP_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/serviceAreaGroup.do\"><i class=\"fa fa-globe\"></i> <span class=\"nav-label\">Service Area Group Mgmt</span><span class='fa arrow'></span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_ENB_MGMT))
//						menuHtml.append(currentMenu.equals(Const.MENU_ENB_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/resources/eNBMgmt.do\"><i class=\"fa fa-puzzle-piece\"></i> <span class=\"nav-label\">eNB Mgmt</span></a></li>");
					else if (permission.getRole().equals(Const.ROLE_SCHEDULE_MGMT))
						menuHtml.append(currentMenu.equals(Const.MENU_SCHEDULE_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"/dashbd/view/schdMgmtDetail.do\"><i class=\"fa fa-calendar\"></i> <span class=\"nav-label\">Schedule Mgmt</span><span class='fa arrow'></span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_SERVICE_CLASS_MGMT))
//						menuHtml.append(currentMenu.equals(Const.MENU_SERVICE_CLASS_MGMT) ? "<li class=\'landing_link\'>" : "<li>").append("<a href=\"/dashbd/view/schdMgmt.do\"><i class='fa fa-desktop'></i> <span class='nav-label'>Service Class Mgmt</span><span class='fa arrow'></span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_SESSION_MONITORING))
//						menuHtml.append(currentMenu.equals(Const.MENU_SESSION_MONITORING) ? "<li class=\'landing_link\'>" : "<li>").append("<a href='#'><i class='fa fa-desktop'></i> <span class='nav-label'>Sessions Monitoring</span><span class='fa arrow'></span></a></li>");
					else if (permission.getRole().equals(Const.ROLE_SYSTEM_MGMT)) {
						menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li>").append("<a href=\"#\"><i class=\"fa fa-th-large\"></i> <span class=\"nav-label\">System Mgmt</span><span class='fa arrow'></span></a></li>");
						menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/logMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">Log</span></a></li>");
						menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemConfMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">System View</span></a></li>");
						menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemDbMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">Database Backup</span></a></li>");
					}
//					else if (permission.getRole().equals(Const.ROLE_SYSTEM_MGMT))
//						menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">Statistic</span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_SYSTEM_CONF_MGMT))
//						menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_CONF_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemConfMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">System Config</span></a></li>");
//					else if (permission.getRole().equals(Const.ROLE_SYSTEM_DB_MGMT))
//						menuHtml.append(currentMenu.equals(Const.MENU_SYSTEM_DB_MGMT) ? "<li class=\"landing_link\">" : "<li style=\"font-size: 12px !important;\">").append("<a href=\"/dashbd/resources/systemDbMgmt.do\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-bar-chart\"></i> <span class=\"nav-label\">DB Backup & Restore</span></a></li>");
				}
			}
			
			root.put("result", 0);
			root.put("menuHtml", menuHtml.toString());
			logger.info("<- [menuHtml = {}]", menuHtml.toString());
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "B2interface", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> b2Interface(HttpServletRequest request) {
		try {
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(request.getInputStream()));
			String inputLine;
			StringBuffer req = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				req.append(inputLine);
			}
			in.close();			
			logger.info("REQUEST: " + req.toString());
			XmlParaSet reqXml = XmlFormer.toXmlParaSet(req.toString().replaceAll("&", "&amp;"));
			StatusNotifyMapper mapper = sqlSession.getMapper(StatusNotifyMapper.class);
			StatusNotifyWithBLOBs record = new StatusNotifyWithBLOBs();
			
			XmlParaSet trans = (XmlParaSet) reqXml.getPara("transaction");
			record.setTransactionId(trans.getIntAttr("id"));
			record.setAgentKey(trans.getValue("agentKey"));
			
			for(int i = 0; i < reqXml.size(); i++) {
				if(!reqXml.get(i).getTag().equalsIgnoreCase("parameters")) continue;
				XmlParaSet parameters = (XmlParaSet) reqXml.get(i);
				XmlParaSet notify = (XmlParaSet) parameters.getPara("notify");
				record.setCode(notify.getIntValue("code"));
				record.setMessage(notify.getValue("message"));
			
				XmlParaSet params = (XmlParaSet) notify.getPara("params");
				if(params.getPara("serviceId") != null) record.setServiceId(params.getIntValue("serviceId"));
				if(params.getPara("bmsc") != null) record.setBmscId(params.getIntValue("bmsc"));
				if(params.getPara("saillst") != null) record.setSaillst(params.getValue("saillst"));
				
				int res = mapper.insertSelective(record);
				logger.info("INSERT RESULT(" + (i + 1) + "): " + res);
			}
			
			XmlParaSet resXml = new XmlParaSet("message");
			resXml.addAttr(new XmlTlv("name")); resXml.setAttr("name", "STATUS.NOTIFY");
			resXml.addAttr(new XmlTlv("type")); resXml.setAttr("type", "RESPONSE");
			
			trans.delPara("agentKey");
			for(int i = 0; i < reqXml.size(); i++) {
				if(!reqXml.get(i).getTag().equalsIgnoreCase("parameters")) continue;
				XmlParaSet result = new XmlParaSet("result");
				result.addPara(new XmlPara("code")); result.addPara(new XmlPara("message"));
				XmlParaSet parameters = (XmlParaSet) reqXml.get(i);
				XmlParaSet notify = (XmlParaSet) parameters.getPara("notify");
				result.setValue("code", notify.getIntValue("code"));
				result.setValue("message", notify.getValue("message"));
				
				XmlParaSet params = (XmlParaSet) notify.getPara("params");				
				if(params != null) result.addPara(params);
				
				trans.addPara(result);
			}
			resXml.addPara(trans);
			
			return new ResponseEntity<String>(XmlFormer.toXmlString(resXml), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "/image/**", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> getImage(
			HttpServletRequest request) throws IOException {
		
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		
		System.out.println(env.getProperty("image.root"));
		System.out.println(restOfTheUrl);
		String imagePath = "D:/catenoid/kollus/samplevideos/17565_20151013-6whdtz7s_mobile1_kwchoffmpeg-mobile1-normal-20151013042655.mp4.144x108x1200.jpg";
		File file = new File(imagePath);
		FileInputStream in = new FileInputStream(file);
		
		final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_JPEG);
	    
	    return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
	}
}

package com.catenoid.dashbd.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.catenoid.dashbd.Const;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Permission;
import com.catenoid.dashbd.dao.model.Users;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);
	
	// TODO @Autowired 적용이 안된다... 추후 원인파악할 것
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	/**
	 * LoginService 에서 성공을 하더라도 실제 로그인 체크를 해야 한다.
	 * 여기서 로그인 체크 후 실패 시 파라미터로 원인을 보내준다.
	 * 성공 시 /loginsuccess 호출
	 */
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response, 
			Authentication authentication) throws IOException, ServletException {
		
		String userId = request.getParameter("userId");
		
//		Integer operatorId = Integer.parseInt(request.getParameter("operatorId"));
//		logger.info("-> [userId = {}], [operatorId = {}]", userId, operatorId);
		
		UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
		Users user = mapper.selectByUserId(userId);
		List<Permission> permission = mapper.selectPermissionsByUserId(userId);
		user.setPermissions(permission);
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(user.getStatus().equals("init")) {
			map.put("reqType", "Login");
			map.put("reqSubType", "login");
			map.put("reqUrl", "login.do");
			map.put("reqCode", "Fail");
			map.put("reqMsg", Const.LOGIN_FAIL_CREDENTIALS_EXPIRED);
//			mapper.insertSystemAjaxLog(map);
			response.sendRedirect("/dashbd/loginfail.do?cause=" + Const.LOGIN_FAIL_CREDENTIALS_EXPIRED + "&userId="+userId);
		} else if(user.getStatus().equals("lock")) {
			map.put("reqType", "Login");
			map.put("reqSubType", "login");
			map.put("reqUrl", "login.do");
			map.put("reqCode", "Fail");
			map.put("reqMsg", Const.LOGIN_FAIL_LOCKED);
//			mapper.insertSystemAjaxLog(map);
			response.sendRedirect("/dashbd/loginfail.do?cause=" + Const.LOGIN_FAIL_LOCKED + "&userId="+userId);
		}
		
//		if (permission.size() != 0) {
			HttpSession session = request.getSession(false);
			session.setAttribute("USER", user);
			
			logger.info("<- [redirect = {}]", "/dashbd/resources/main.do");
			
			String ip = request.getHeader("X-FORWARDED-FOR");
	        if (ip == null)
	            ip = request.getRemoteAddr();
			
	        Users LogUser = (Users)session.getAttribute("USER");
			HashMap<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("reqType", "Login");
			logMap.put("reqSubType", "Login");
			logMap.put("reqUrl", "login.do");
			logMap.put("reqCode", "SUCCESS");
			logMap.put("targetId", LogUser.getUserId());
			logMap.put("reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId() + " - Login (IP address : " + ip + ")");
			UsersMapper logMapper = sqlSession.getMapper(UsersMapper.class);
			logMapper.insertSystemAjaxLog(logMap);
			
			logMap.put("userId", LogUser.getUserId());
			logMapper.resetUserPasswordCnt(logMap);
			
			response.sendRedirect("/dashbd/resources/main.do");
//		}
//		else { // 부여된 권한이 없는 경우
//			logger.info("<- [redirect = {}] [cause = {}]", "/dashbd/loginfail.do", Const.LOGIN_FAIL_MISMATCH);
//
//			map.put("reqType", "Login");
//			map.put("reqSubType", "login");
//			map.put("reqUrl", "login.do");
//			map.put("reqCode", "Fail");
//			map.put("reqMsg", Const.LOGIN_FAIL_MISMATCH);
//			mapper.insertSystemAjaxLog(map);
//			response.sendRedirect("/dashbd/loginfail.do?cause=" + Const.LOGIN_FAIL_MISMATCH);
//		}
	}

}

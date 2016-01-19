package com.catenoid.dashbd.security;

import java.io.IOException;

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
		Integer operatorId = Integer.parseInt(request.getParameter("operatorId"));
		
		logger.info("-> [userId = {}], [operatorId = {}]", userId, operatorId);
		
		UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
		Users user = mapper.selectByUserId(userId);
		
		// Super Admin은 operatorId가 null 이다
		if (user.getOperatorId() == null || user.getOperatorId() == operatorId) {
			user.setPermissions(mapper.selectPermissionsByUserId(userId));
			
			HttpSession session = request.getSession(false);
			session.setAttribute("USER", user);
			
			logger.info("<- [redirect = {}]", "/dashbd/resources/main.do");
			response.sendRedirect("/dashbd/resources/main.do");
		}
		else { // 선택한 operator와 실제 operator가 다른 경우
			logger.info("<- [redirect = {}] [cause = {}]", "/dashbd/loginfail.do", Const.LOGIN_FAIL_MISMATCH);
			response.sendRedirect("/dashbd/loginfail.do?cause=" + Const.LOGIN_FAIL_MISMATCH);
		}
	}

}

package com.catenoid.dashbd.security;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.catenoid.dashbd.Const;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Users;

public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);
	
	// TODO @Autowired 적용이 안된다... 추후 원인파악할 것
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request,
			HttpServletResponse response, 
			AuthenticationException authnticationException)
			throws IOException, ServletException {
		
		String userId = request.getParameter("userId");
		logger.info("-> [Login Failure!(userId = {})]", userId);
		
		int cause = Const.COMMON_SERVER_ERROR;
		if (authnticationException.getClass().equals(BadCredentialsException.class)) { //비밀번호 틀림
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			Users user = mapper.selectByUserId(userId);
			if(user.getFailCnt() == 4) {
				cause = Const.LOGIN_FAIL_LOCKED;
				param.put("status", "lock");
			}
			else {
				cause = Const.LOGIN_FAIL_MISMATCH_PASSWORD;
				param.put("status", "");
			}
			
			mapper.updateFailCnt(param);
		}
		else if (authnticationException.getClass().equals(AuthenticationServiceException.class)) // 계정 없음
			cause = Const.LOGIN_FAIL_MISMATCH;
		else if (authnticationException.getClass().equals(DisabledException.class)) // 계정 Disable
			cause = Const.LOGIN_FAIL_DISABLED;
		else if (authnticationException.getClass().equals(AccountExpiredException.class)) // 계정 만료
			cause = Const.LOGIN_FAIL_ACCOUNT_EXPIRED;
		else if (authnticationException.getClass().equals(CredentialsExpiredException.class)) // 계정 권한 만료
			cause = Const.LOGIN_FAIL_CREDENTIALS_EXPIRED;
		else if (authnticationException.getClass().equals(LockedException.class)) // 계정 잠김
			cause = Const.LOGIN_FAIL_LOCKED;
		else { // 그 외는 서버에러로 본다.
			logger.error(authnticationException.toString());
			cause = Const.COMMON_SERVER_ERROR;
		}
		
		logger.info("<- [redirect = {}] [cause = {}]", "/dashbd/loginfail.do", cause);
		
		request.getSession(true).setAttribute("userId", userId);
		response.sendRedirect("/dashbd/loginfail.do?cause=" + cause);
	}
	
}

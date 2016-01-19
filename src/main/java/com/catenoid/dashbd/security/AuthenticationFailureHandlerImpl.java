package com.catenoid.dashbd.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.catenoid.dashbd.Const;

public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);
	
	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request,
			HttpServletResponse response, 
			AuthenticationException authnticationException)
			throws IOException, ServletException {
		
		String userId = request.getParameter("userId");
		logger.info("-> [Login Failure!(userId = {})]", userId);
		
		int cause = Const.COMMON_SERVER_ERROR;
		if (authnticationException.getClass().equals(BadCredentialsException.class) ||
			authnticationException.getClass().equals(AuthenticationServiceException.class)) // 계정 없음
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
		response.sendRedirect("/dashbd/loginfail.do?cause=" + cause);
	}
	
}

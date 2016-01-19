package com.catenoid.dashbd.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import com.catenoid.dashbd.dao.model.Users;

public class AjaxSessionTimeoutFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(AjaxSessionTimeoutFilter.class);
	
	private String ajaxHeader;
	
	public String getAjaxHeader() {
		return ajaxHeader;
	}

	public void setAjaxHeader(String ajaxHeader) {
		this.ajaxHeader = ajaxHeader;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
 
		if (isAjaxRequest(request)) {
			logger.info("-> []");
			HttpSession session = request.getSession(false);
			if (session == null) {
				logger.info("~~ [Session expired when call ajax!!]");
				logger.info("<- [return http status = {}]", HttpServletResponse.SC_FORBIDDEN);
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
			else {
				Users user = (Users) session.getAttribute("USER");
				if (user == null) {
					logger.info("~~ [Session expired when call ajax!!]");
					logger.info("<- [return http status = {}]", HttpServletResponse.SC_FORBIDDEN);
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				
				try {
					chain.doFilter(request, response);
				} catch (AccessDeniedException e) {
					logger.info("~~ [Session expired when call ajax!!]");
					logger.info("<- [return http status = {}]", HttpServletResponse.SC_FORBIDDEN);
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				} catch (AuthenticationException e) {
					logger.info("~~ [Session expired when call ajax!!]");
					logger.info("<- [return http status = {}]", HttpServletResponse.SC_UNAUTHORIZED);
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	private boolean isAjaxRequest(HttpServletRequest req) {
		return req.getHeader(ajaxHeader) != null && req.getHeader(ajaxHeader).equals(Boolean.TRUE.toString());
	}

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
}

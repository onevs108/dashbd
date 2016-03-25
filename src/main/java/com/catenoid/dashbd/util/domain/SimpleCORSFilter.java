package com.catenoid.dashbd.util.domain;





import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class SimpleCORSFilter implements Filter {
	   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

	        HttpServletResponse response = (HttpServletResponse) res;


	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	        response.setHeader("Access-Control-Max-Age", "3600");
	        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	        
	        //response.setHeader("Access-Control-Allow-Origin", "*");
	        response.addHeader("Access-Control-Allow-Origin", "http://dash.edgesuite.net");
	        response.addHeader("Access-Control-Allow-Origin", "http://vm2.dashif.org");
	        response.addHeader("Access-Control-Allow-Origin", "http://165.213.100.148");
	        response.addHeader("Access-Control-Allow-Origin", "http://temp.samsung.co.kr");
	        //response.addHeader("Access-Control-Allow-Origin", "http://test.ozrank.co.kr");
	     


	        chain.doFilter(req, res);
	    }

	    public void init(FilterConfig filterConfig) {}

	    public void destroy() {}

}

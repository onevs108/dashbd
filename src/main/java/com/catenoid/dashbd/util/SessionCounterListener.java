package com.catenoid.dashbd.util;

import java.util.Date;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.catenoid.dashbd.ServiceAreaController;


public class SessionCounterListener implements HttpSessionListener {
	  private static final Logger logger = LoggerFactory.getLogger(ServiceAreaController.class);
	  
	  private static int activeSessions = 0;

	  public void sessionCreated(HttpSessionEvent event) {
		  if (logger.isDebugEnabled()) {
	            logger.debug("Session ID".concat(event.getSession().getId()).concat(" created at ").concat(new Date().toString()));
	      }
		  
		  activeSessions++;
	  }

	  public void sessionDestroyed(HttpSessionEvent event) {
		if (logger.isDebugEnabled()) {
	          logger.debug("Session ID".concat(event.getSession().getId()).concat(" destroyed at ").concat(new Date().toString()));
	    }
		  
		if(activeSessions > 0)
			activeSessions--;
	  }

	  public static int getActiveSessions() {
	    return activeSessions;
	  }
	}
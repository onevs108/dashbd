package com.catenoid.dashbd.ctrl;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.catenoid.dashbd.HomeController;

@Controller
public class ScheduleMgmtController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	
	@RequestMapping(value = "/view/schdMgmt.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String schdMgmt(Locale locale, Model model) throws UnsupportedEncodingException {
		//bmcm 와 serviceArea  값으로  스케줄 정보를 가져온다.
		
		logger.info("schdMgmt load");
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		System.out.println(formattedDate);
				
		return "schd/schdMgmt";
	}
	
	@RequestMapping(value = "view/schdMgmtDetail.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String schdMgmtDetail(Locale locale, Model model) throws UnsupportedEncodingException {

			
		return "schd/schdMgmtDetail";
	}
}

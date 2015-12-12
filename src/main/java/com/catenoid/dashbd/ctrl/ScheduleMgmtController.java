package com.catenoid.dashbd.ctrl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.service.XmlManager;
import com.catenoid.dashbd.util.HttpNetAgent;
import com.catenoid.dashbd.util.HttpNetAgentException;


@Controller
public class ScheduleMgmtController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private XmlManager xmlManager;
	
	/**
	 * 스케줄 메인페이지
	 * @param params
	 * @param req
	 * @param locale
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
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
	
	/**
	 * 스케줄 메인페이지 > timetable 스케줄 가져오기(ajax)
	 * @param params
	 * @param req
	 * @param locale
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping( value = "/view/getSchedule.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public Map< String, Object > getSchedule( @RequestParam Map< String, Object > params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		//params.put("serviceId", "3048");
		List<Map> list = mapper.selectSchdule(params);
		
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 2;
        if (hour < 0)
        	hour = 0;
        
        Map< String, Object > returnMap = new HashMap< String, Object >();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", "SUCCESS");
        
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
        resultMap.put( "contents", list );
        resultMap.put( "viewStartHour", hour);
        
		return (Map<String, Object>) resultMap;
	}
	
	/**
	 * 스케줄 상세페이지 > 팝업 > 스케줄 추가(ajax)
	 * @param params
	 * @param req
	 * @param locale
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "view/addScheduleWithInitContent.do", produces="text/plain;charset=UTF-8")
	@ResponseBody
	public Map< String, Object > addScheduleWithInitContent( @RequestParam Map< String, Object > params,
            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		int ret = mapper.addScheduleWithInitContent(params);
		logger.info("addScheduleWithInitContent [ret={}]", ret);
		Map< String, Object > returnMap = new HashMap< String, Object >();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", "SUCCESS");
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
                
		return (Map<String, Object>) resultMap;
	}
	
	/**
	 * 스케줄 메인페이지 > 스케줄 상세페이지
	 * @param locale
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "view/schdMgmtDetail.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String schdMgmtDetail(Locale locale, Model model) throws UnsupportedEncodingException {

			
		return "schd/schdMgmtDetail";
	}
	
	/**
	 * 스케줄 메인페이지 > 스케줄 상세페이지 > broadcast  상세페이지
	 * @param locale
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "view/schedule.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String schedule(Locale locale, Model model) throws UnsupportedEncodingException {
		logger.info("schedule ");
		return "schd/schedule";
	}
	
	/**
	 * 스케줄 메인페이지 > 스케줄 상세페이지 > broadcast  상세페이지 > 저장
	 * @param locale
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "view/scheduleReg.do", method = RequestMethod.POST)
	@ResponseBody
	public Map< String, Object > scheduleReg( @RequestParam Map< String, Object > params,
            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		logger.info("sending param{}", params);
		String resStr = xmlManager.createBroadcast(params);

		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		//@ insert broadcast_info  with flag which createBroadcast is successed or not
		//@ update schedule broadcast id
		//int ret = mapper.addScheduleWithInitContent(params);

		Map< String, Object > returnMap = new HashMap< String, Object >();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", resStr);
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
                
		return (Map<String, Object>) resultMap;
	}
	
}

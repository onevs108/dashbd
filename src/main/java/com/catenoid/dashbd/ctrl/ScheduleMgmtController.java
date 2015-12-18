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
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.service.XmlManager;
import com.catenoid.dashbd.util.HttpNetAgent;
import com.catenoid.dashbd.util.HttpNetAgentException;
import com.catenoid.dashbd.util.Utils;


@Controller
public class ScheduleMgmtController {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	long transID = 0;
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
	@RequestMapping(value = "/view/schdMgmt.do")
	public ModelAndView schdMgmt(@RequestParam Map< String, Object > params, HttpServletRequest req) throws UnsupportedEncodingException {
		//bmcm 와 serviceArea  값으로  스케줄 정보를 가져온다.
		ModelAndView mv = new ModelAndView( "schd/schdMgmt" );
		try
		{
			String transId = makeTransId();
			params.put("transactionId", transId);
				
			//@ xmlMake & Send, recv
			//String resStr = xmlManager.sendBroadcast(params, xmlManager.BMSC_XML_RETRIEVE);
			//@ check return XML success
//			if (!xmlManager.isSuccess(resStr))
//				throw new Exception(resStr);
			//@ parsing
//			Map<String, String> xmlParam = xmlManager.paringRetrieve(resStr);
			
			//@ db insert
			String searchDate = Utils.getFileDate("YYYY-MM-dd");
			mv.addObject("searchDate", searchDate); 
			
		}catch(Exception e){
			logger.error("", e);
			mv.addObject( "error", e.getMessage() );
		}
				
		return mv;
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
	@RequestMapping( value = "/view/getSchedule.do")
	@ResponseBody
	public Map< String, Object > getSchedule( @RequestParam Map< String, Object > params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		//params.put("serviceId", "3048");
		List<Map> list = mapper.selectSchdule(params);
		
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 4)
        	hour = 0;
        else if (hour < 12)
        	hour = hour -2;
        else if (hour < 18)
        	hour = 12;
        else
        	hour = 14;
        
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
	@RequestMapping(value = "view/addScheduleWithInitContent.do")
	@ResponseBody
	public Map< String, Object > addScheduleWithInitContent( @RequestParam Map< String, Object > params,
            HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		params.put("startTime", convertMysqlDateFormat((String)params.get("startTime")));
		params.put("endTime", convertMysqlDateFormat((String)params.get("endTime")));
		int ret = mapper.addScheduleWithInitContent(params);
		logger.info("addScheduleWithInitContent [ret={}]", ret);

		return makeRetMsg("1000", "SUCCESS");
	}
	
	@RequestMapping(value = "view/modifyScheduleTime.do")
	@ResponseBody
	public Map< String, Object > modifyScheduleTime( @RequestParam Map< String, Object > params,
            HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		params.put("startTime", convertMysqlDateFormat((String)params.get("startTime")));
		params.put("endTime", convertMysqlDateFormat((String)params.get("endTime")));
		//int ret = mapper.modifyScheduleTime(params);
		//logger.info("addScheduleWithInitContent [ret={}]", ret);

		return makeRetMsg("1000", "SUCCESS");
	}
	
	
	
	/**
	 * 스케줄 메인페이지 > 스케줄 상세페이지
	 * @param locale
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "view/schdMgmtDetail.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public ModelAndView schdMgmtDetail( @RequestParam Map< String, Object > params,  HttpServletRequest req) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView( "schd/schdMgmtDetail" );
		mv.addObject( "serviceAreaId", params.get("serviceAreaId")); 
		mv.addObject("searchDate", params.get("searchDate"));
		return mv;
	}
	
	/**
	 * 스케줄 메인페이지 > 스케줄 상세페이지 > broadcast  상세페이지
	 */
	@RequestMapping(value = "view/schedule.do")
	public ModelAndView schedule( @RequestParam Map< String, Object > params) throws UnsupportedEncodingException {
		
		ModelAndView mv = new ModelAndView( "schd/schedule" );
		logger.info("schedule ");
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		Map mapSchedule = mapper.selectSchduleTime(params);
		mv.addObject( "mapSchedule", mapSchedule );
		return mv;
	}
	
	/**
	 * 스케줄 메인페이지 > 스케줄 상세페이지 > broadcast  상세페이지 > 등록, 수정
	 * @param locale
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "view/scheduleReg.do")
	@ResponseBody
	public Map< String, Object > scheduleReg( @RequestParam Map< String, Object > params,
            HttpServletRequest req, Locale locale ) {
		
		logger.info("sending param{}", params);
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		try{
			String transId = makeTransId();
			params.put("transactionId", transId);
			params.put("serviceId","urn:3gpp:" + params.get("serviceType") + ":" + transId);
			
			//@ xmlMake & Send, recv
			String resStr = xmlManager.sendBroadcast(params, xmlManager.BMSC_XML_CREATE);
			
			//@ check return XML success
			if (!xmlManager.isSuccess(resStr))
				return makeRetMsg("9000", resStr);
			
			//@ insert broadcast_info  with flag which createBroadcast is successed or not
			int ret = mapper.insertBroadcastInfo(params);
			logger.info("insertBroadcastInfo ret{}", ret);

			//@ update schedule broadcast id
			ret = mapper.updateSchedule(params);
			logger.info("updateSchedule ret{}", ret);
                
	        return makeRetMsg("1000", "SUCCESS");
		}catch(Exception e){
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
	
	}
	
	/**
	 * 스케줄 메인페이지 > 스케줄 상세페이지 > broadcast  상세페이지 > 삭제
	 * bcid가 있으면  BMSC삭제 연동. 없으면 db만 삭제처리.
	 * @param params
	 * @param req
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "view/delSchedule.do")
	@ResponseBody
	public Map< String, Object > delSchedule( @RequestParam Map< String, Object > params,
            HttpServletRequest req, Locale locale ) {
		
		logger.info("sending param{}", params);
		try{
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			
			if (params.get("BCID") != null && !"".equals(params.get("BCID"))){
				Map mapBroadcast = mapper.selectBroadcast(params);
				params.put("transactionId", makeTransId());
				params.put("serviceId", mapBroadcast.get("serviceId"));
				//@ xmlMake & Send, recv
				String resStr = xmlManager.sendBroadcast(params, xmlManager.BMSC_XML_DELETE);

				
				//@ check return XML success
				if (!xmlManager.isSuccess(resStr))
					return makeRetMsg("1000", resStr);
			}

			/*
			//@ update delete flag 
			int ret = mapper.updateBroadcastInfo4Del(params);
			logger.info("insertBroadcastInfo ret{}", ret);

			 */
			//@ update delete flag 
			int ret = mapper.updateSchedule4Del(params);
			logger.info("updateSchedule ret{}", ret);

			Map< String, Object > returnMap = new HashMap< String, Object >();
	        returnMap.put( "resultCode", "1000" );
	        returnMap.put( "resultMsg", "SUCCESS");
	        Map< String, Object > resultMap = new HashMap< String, Object >();
	        resultMap.put( "resultInfo", returnMap );
	                
			return (Map<String, Object>) resultMap;
			
		}catch(Exception e){
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
	
	}
	private String convertMysqlDateFormat(String dateTime){
		dateTime = dateTime.trim();
		dateTime = dateTime.replaceAll("-", "");
		dateTime = dateTime.replaceAll(":", "");
		dateTime = dateTime.replaceAll("T", "");
		return dateTime;
	}

	private String makeTransId() {
		return System.currentTimeMillis() + "" + transID++;
	}

	private Map<String, Object> makeRetMsg(String code, String resStr) {
		
		Map< String, Object > returnMap = new HashMap< String, Object >();
	    returnMap.put( "resultCode", code );
	    returnMap.put( "resultMsg", resStr);
	      
	    Map< String, Object > resultMap = new HashMap< String, Object >();
	    resultMap.put( "resultInfo", returnMap );
	              
		return (Map<String, Object>) resultMap;
	}
	
}

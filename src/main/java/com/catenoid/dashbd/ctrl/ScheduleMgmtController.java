package com.catenoid.dashbd.ctrl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.BmscMapper;
import com.catenoid.dashbd.dao.OperatorMapper;
import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.service.XmlManager;
import com.catenoid.dashbd.util.Utils;
import com.google.gson.Gson;


@Controller
public class ScheduleMgmtController {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	long transID = 101;	//101 ~ 65535
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private XmlManager xmlManager;
	
	/**
	 * �뒪耳�以� 硫붿씤�럹�씠吏�
	 * @param params
	 * @param req
	 * @param locale
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/view/schdMgmt.do")
	public ModelAndView schdMgmt(@RequestParam Map< String, Object > params, HttpServletRequest request) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView( "schd/schdMgmt" );
		try
		{
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
			
			Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
			Integer perPage = 50;
			
			OperatorSearchParam searchParam = new OperatorSearchParam();
			searchParam.setPage((page-1) * perPage);
			searchParam.setPerPage(perPage);
			List<HashMap<String,String>> result = mapper.getServiceAreaOperator1(searchParam);
			
			HashMap<String,String> initOperator = result.get(0);
			
			searchParam = new OperatorSearchParam();
			searchParam.setPage((page-1) * perPage);
			searchParam.setPerPage(perPage);
			searchParam.setOperatorId(Integer.parseInt(String.valueOf(initOperator.get("id"))));
			
			List<Bmsc> bmscs = mapper.getSeviceAreaBmSc(searchParam);
			
			List<Circle> circleList = operatorMapper.selectCircleListAll();
			mv.addObject("circleList", circleList);
			mv.addObject("OperatorList", result);
			mv.addObject("BmscList", bmscs);
			
			//logger.info("GBRSum=", exampleGBRSum());
//			String transId = makeTransId();
//			params.put("transactionId", transId);
				
			//@ xmlMake & Send, recv
			//String resStr = xmlManager.sendBroadcast(params, xmlManager.BMSC_XML_RETRIEVE);
			//@ check return XML success
//			if (!xmlManager.isSuccess(resStr))
//				throw new Exception(resStr);
			//@ parsing
//			Map<String, String> xmlParam = xmlManager.paringRetrieve(resStr);
			
			//@ db insert
			String searchDate = Utils.getFileDate("yyyy-MM-dd");
			mv.addObject("searchDate", searchDate); 
			
		}catch(Exception e){
			logger.error("", e);
			mv.addObject( "error", e.getMessage() );
		}
				
		return mv;
	}
	
	/**
	 * �뒪耳�以� 硫붿씤�럹�씠吏� > timetable �뒪耳�以� 媛��졇�삤湲�(ajax)
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
		int maxPosition = mapper.selectSchduleMaxPosition(params);
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
        resultMap.put( "maxPosition", maxPosition );
        resultMap.put( "contents", list );
        resultMap.put( "viewStartHour", hour);
        
		return (Map<String, Object>) resultMap;
	}
	
	@RequestMapping(value = "view/checkBandwidth.do")
	@ResponseBody
	public Map<String, Object> checkBandwidth(@RequestParam Map<String, String> params, HttpServletRequest req, Locale locale) {
		Map< String, Object > resultMap = new HashMap< String, Object >();
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		if(params.get("saidList").equals("")){
			resultMap.put("result", "SUCCESS");
			resultMap.put("resultObj", "SUCCESS");
			return resultMap;
		}
		String[] listArray = params.get("saidList").split(",");
		List<Map<String, String>> bwList = mapper.checkBandwidth(params);
		
		for (int i = 0; i < listArray.length; i++) {
			for (int j = 0; j < bwList.size(); j++) {
				if(String.valueOf(bwList.get(j).get("said")).equals(listArray[i])){	//bandwidth가 초과할 때 
				    if(Integer.parseInt(String.valueOf(bwList.get(j).get("bandwidth"))) < Integer.parseInt(params.get("bandwidth"))) {
						resultMap.put("result", "bwExceed");
					    resultMap.put("resultObj", bwList.get(j));
					    return resultMap;
					}
				    break;
				}
				if(j == bwList.size()-1){	//Said가 존재 하지 않을 때 
					resultMap.put("result", "noSaid");
				    resultMap.put("resultObj", listArray[i]);
				    return resultMap;
				}
			}
		}
		
		resultMap.put("result", "SUCCESS");
		resultMap.put("resultObj", "SUCCESS");
		
		return resultMap;
	}
	
	
	@RequestMapping(value = "view/checkExistSaid.do")
	@ResponseBody
	public Map<String, Object> checkExistSaid(@RequestParam Map<String, String> params, HttpServletRequest req, Locale locale) {
		Map< String, Object > resultMap = new HashMap< String, Object >();
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		int result = mapper.checkExistSaid(params);
		if(result < 1) {
			resultMap.put("result", "FAIL");
		}else{
			resultMap.put("result", "SUCCESS");
		}
		
		return resultMap;
	}
	
	@RequestMapping( value = "/view/getScheduleForMain.do")
	@ResponseBody
	public Map< String, Object > getScheduleForMain( @RequestParam Map< String, Object > params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		String searchDate = Utils.getFileDate("yyyy-MM-dd");
		params.put("searchDate", searchDate);
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		List<Map> list = mapper.selectSchdule(params);
        
        Map< String, Object > returnMap = new HashMap< String, Object >();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", "SUCCESS");
        
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
        resultMap.put( "contents", list );
        
		return (Map<String, Object>) resultMap;
	}
	
	/**
	 * �뒪耳�以� �긽�꽭�럹�씠吏� > �뙘�뾽 > �뒪耳�以� 異붽�(ajax)
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
		params.put("startTime", convertMysqlDateFormat((String)params.get("startTime"), false));
		params.put("endTime", convertMysqlDateFormat((String)params.get("endTime"), true));
		int ret = mapper.addScheduleWithInitContent(params);
		logger.info("addScheduleWithInitContent [ret={}]", ret);

		return makeRetMsg("1000", "SUCCESS");
	}
	
	@RequestMapping(value = "view/modifyScheduleTime.do")
	@ResponseBody
	public Map< String, Object > modifyScheduleTime( @RequestParam Map< String, String > params,
            HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		
		logger.info("modifyScheduleTime param{}", params);

		try{
			BmscMapper mapperBmsc = sqlSession.getMapper(BmscMapper.class);
			Bmsc bmsc = mapperBmsc.selectBmsc(Integer.parseInt(params.get("bmscId")));
			
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			String startTime = convertMysqlDateFormat(params.get("startTime"), true);
			String endTime = convertMysqlDateFormat(params.get("endTime"), false);
			params.put("startTime", startTime);
			params.put("endTime", 	endTime);
			
			int ret = mapper.modifyScheduleTime(params);
			logger.info("modifyScheduleTime ret{}", ret);
			
			if (params.get("BCID") != null && !"".equals(params.get("BCID"))){
				//@ update broadcast_info
				params.put("transactionId", makeTransId());
				params.put("schedule_start", startTime);
				params.put("schedule_stop", endTime);
				params.put("deliveryInfo_start", startTime);
				params.put("deliveryInfo_end", endTime);
				ret = mapper.updateBroadcastInfo(params);
				logger.info("updateSchedule ret{}", ret);

				//@ xml update �뿰�룞
				Map<String, String> mapBroadcast = mapper.selectBroadcast(params);
				mapBroadcast.put("bmscIp", bmsc.getIpaddress());
				
				String[] resStr = xmlManager.sendBroadcast(mapBroadcast, xmlManager.BMSC_XML_UPDATE);
	
				//@ check return XML success
				if (!xmlManager.isSuccess(resStr[0]))
					return makeRetMsg("9000", resStr[0]);
		}
		}catch(Exception e){
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
		
		return makeRetMsg("1000", "SUCCESS");
	}
	

	/**
	 * �뒪耳�以� 硫붿씤�럹�씠吏� > �뒪耳�以� �긽�꽭�럹�씠吏�
	 * @param locale
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "view/schdMgmtDetail.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public ModelAndView schdMgmtDetail( @RequestParam Map< String, Object > params,  HttpServletRequest req) throws UnsupportedEncodingException {
		logger.info("schdMgmtDetail {}", params);
		ModelAndView mv = new ModelAndView( "schd/schdMgmtDetail" );
		mv.addObject( "serviceAreaId", params.get("serviceAreaId")); 
		mv.addObject( "bmscId", params.get("bmscId"));
		mv.addObject("searchDate", params.get("searchDate"));
		mv.addObject("title", params.get("title"));
		mv.addObject("category", params.get("category"));
		
		return mv;
	}
	
	/**
	 * �뒪耳�以� 硫붿씤�럹�씠吏� > �뒪耳�以� �긽�꽭�럹�씠吏� > broadcast  �긽�꽭�럹�씠吏�
	 */
	@RequestMapping(value = "view/schedule.do")
	public ModelAndView schedule( @RequestParam Map< String, String > params) throws UnsupportedEncodingException {
		
		ModelAndView mv = new ModelAndView( "schd/schedule" );
		logger.info("schedule ");
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		Map<String, String> mapContentUrl = mapper.selectSchduleContentURL(params);
		Map<String, String> mapSchedule = mapper.selectSchduleTime(params);
//		List<Map<String, String>> mapContents = mapper.selectSchduleTime(params);
		
		if (mapSchedule.get("BCID") == null || "".equals(mapSchedule.get("BCID"))){
			mapSchedule.put("service_name", "testService");
			mapSchedule.put("language", "en");
			mapSchedule.put("GBR", "1000");
			mapSchedule.put("QCI", "1");
			mapSchedule.put("FileRepair", "off");
			mapSchedule.put("receptionReport", "off");
			mapSchedule.put("schedule_start", mapSchedule.get("temp_start"));
			mapSchedule.put("schedule_stop", mapSchedule.get("temp_end"));
		}
		
		mv.addObject( "mapContentUrl", mapContentUrl );
		mv.addObject( "mapSchedule", mapSchedule );
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view/getGroupListFromCircleId.do")
	@ResponseBody
	public String getGroupListFromCircleId(@RequestParam HashMap<String,String> param) {
		
		JSONObject jsonResult = new JSONObject();
		
		ScheduleMapper scheduleMapper = sqlSession.getMapper(ScheduleMapper.class);
		List<HashMap<String,String>> serviceGroupList = scheduleMapper.getGroupListFromCircleId(param);
		
		Gson gson = new Gson();
		String str = gson.toJson(serviceGroupList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("result", json);
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view/getGroupSaidList.do")
	@ResponseBody
	public String getGroupSaidList(@RequestParam HashMap<String,String> param) {
		
		JSONObject jsonResult = new JSONObject();
		
		ScheduleMapper scheduleMapper = sqlSession.getMapper(ScheduleMapper.class);
		List<HashMap<String,String>> groupSaidList = scheduleMapper.getGroupSaidList(param);
		
		Gson gson = new Gson();
		String str = gson.toJson(groupSaidList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("result", json);
		
		return jsonResult.toString();
	}
	
	/**
	 * �뒪耳�以� 硫붿씤�럹�씠吏� > �뒪耳�以� �긽�꽭�럹�씠吏� > broadcast  �긽�꽭�럹�씠吏� > �벑濡�, �닔�젙
	 * @param locale
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "view/scheduleReg.do")
	@ResponseBody
	public Map< String, Object > scheduleReg( @RequestParam Map< String, String > params,
			@RequestParam(value="saidData", required=false) List<String> saidData,
			@RequestParam(value="schedule_start", required=false) List<String> schedule_start,
			@RequestParam(value="schedule_stop", required=false) List<String> schedule_stop,
			@RequestParam(value="fileURI", required=false) List<String> fileURI,
			@RequestParam(value="deliveryInfo_start", required=false) List<String> deliveryInfo_start,
			@RequestParam(value="deliveryInfo_end", required=false) List<String> deliveryInfo_end,
			@RequestParam(value="contentLength", required=false) List<String> contentLength,
			@RequestParam(value="saidList", required=false) List<String> saidList,
			@RequestParam(value="mpdURI", required=false) List<String> mpdURI,
            HttpServletRequest req, Locale locale ) {
		
		int ret;
		logger.info("sending param{}", params);
		List<List<String>> paramList = new ArrayList<List<String>>();
		paramList.add(schedule_start);
		paramList.add(schedule_stop);
		paramList.add(fileURI);
		paramList.add(deliveryInfo_start);
		paramList.add(deliveryInfo_end);
		paramList.add(contentLength);
		paramList.add(saidList);
		paramList.add(mpdURI);
		
		String tmp = params.get("preEmptionCapabiity");
		
		if (tmp == null){
			tmp="off";
			params.put("preEmptionCapabiity", tmp);
		}
		
		tmp = params.get("preEmptionVulnerability");
		
		if (tmp == null){
			tmp="off";
			params.put("preEmptionVulnerability", tmp);
		}
		
		try{
			String transId = makeTransId();
			String bcid = params.get("BCID");
			BmscMapper mapperBmsc = sqlSession.getMapper(BmscMapper.class);
			Bmsc bmsc = mapperBmsc.selectBmsc(Integer.parseInt(params.get("bmscId")));
			
			//@ xmlMake & Send, recv
			int xmlMode = xmlManager.BMSC_XML_UPDATE;
			
			if (bcid == null || "".equals(bcid)){
				xmlMode = xmlManager.BMSC_XML_CREATE;
			}
			
			params.put("transactionId", transId);
			params.put("schedule_start", convertMysqlDateFormat(params.get("schedule_start"), false));
			params.put("schedule_stop", convertMysqlDateFormat(params.get("schedule_stop"),false));
			params.put("deliveryInfo_start", convertMysqlDateFormat(params.get("deliveryInfo_start"), false));
			params.put("deliveryInfo_end", convertMysqlDateFormat(params.get("deliveryInfo_end"),false));
			params.put("bmscIp", bmsc.getIpaddress());
			String[] resStr = xmlManager.sendBroadcast(params, xmlMode, saidData, paramList);
			
			//@ check return XML success
			if (!xmlManager.isSuccess(resStr[0]))
				return makeRetMsg("9000", resStr[0]);
			
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			
			if (bcid == null || "".equals(bcid)){
				//@ insert broadcast_info  with flag which createBroadcast is successed or not
				ret = mapper.insertBroadcastInfo(params);
				logger.info("insertBroadcastInfo ret{}", ret);
				
				if(params.get("serviceType").equals("streaming")){
					
				}
				else
				{
					params.put("serviceAreaId", params.get("serviceAreaId")+","+saidList.get(0));
				}
				
				//전송 후 본래의 스케쥴 업데이트
				ret = mapper.updateSchedule(params);
				
				//schedule start 갯수만큼
				for (int i = 0; i < paramList.get(0).size(); i++) {	
					
				}
				//@ insert schedule append said
				/*if (saidList.size() > 0) {
					for(int i = 0; i < saidList.size(); i++){
						if(!saidList.get(i).equals("")){
							String[] saidArray = saidList.get(i).split(",");
							for (int j = 0; j < saidArray.length; j++) {
								params.put("serviceAreaId", saidArray[j]);
								ret = mapper.insertAddSchedule(params);
								logger.info("insertAddSchedule ret{}", ret);
							}
						}
					}
				}*/
				
				insertContentInfo(resStr[1], params.get("id"));
				
				logger.info("updateSchedule ret{}", ret);
			}else{
				ret = mapper.updateBroadcastInfo(params);
				logger.info("updateSchedule ret{}", ret);
			}
                
	        return makeRetMsg("1000", "SUCCESS");
		}catch(Exception e){
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public int insertContentInfo(String xmlString, String scheduleId) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		int result = 0;
		
		try {
			InputStream stream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
			SAXBuilder builder = new SAXBuilder(); 
			Document jdomdoc = builder.build(stream);
			Element root = jdomdoc.getRootElement();
			Element parameters = (Element) root.getChildren().get(1);
			Element service = parameters.getChild("services").getChild("service");
			String serviceType = parameters.getChild("services").getChild("service").getAttribute("serviceType").getValue();
			Element customType = service.getChild(serviceType);
			
			List<Element> schedule = customType.getChildren("schedule");
			List<Element> serviceArea = customType.getChildren("serviceArea");

			for (int i = 0; i < serviceArea.size(); i++) {
				HashMap<String,String> param = new HashMap<String, String>();
				param.put("scheduleId", String.valueOf(Integer.parseInt(scheduleId)+i));
				List<Element> content = schedule.get(i).getChildren();
				for (int j = 0; j < content.size(); j++) {
					param.put("contentId", content.get(j).getAttributeValue("contentId"));
					if(content.get(j).getAttributeValue("start") != null){
						param.put("startTime", content.get(j).getAttributeValue("start"));
						param.put("endTime", content.get(j).getAttributeValue("stop"));
					}
					param.put("fileURI", content.get(j).getAttributeValue("fileURI"));
					mapper.insertScheduleContent(param);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * �뒪耳�以� 硫붿씤�럹�씠吏� > �뒪耳�以� �긽�꽭�럹�씠吏� > broadcast  �긽�꽭�럹�씠吏� > �궘�젣
	 * bcid媛� �엳�쑝硫�  BMSC�궘�젣 �뿰�룞. �뾾�쑝硫� db留� �궘�젣泥섎━.
	 * @param params
	 * @param req
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "view/delSchedule.do")
	@ResponseBody
	public Map< String, Object > delSchedule( @RequestParam Map< String, String > params,
            HttpServletRequest req, Locale locale ) {
		
		logger.info("delSchedule params{}", params);
		try{
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			
			if (params.get("BCID") != null && !"".equals(params.get("BCID"))){
				Map<String, String> mapBroadcast = mapper.selectBroadcast(params);
				params.put("transactionId", makeTransId());
				
				BmscMapper mapperBmsc = sqlSession.getMapper(BmscMapper.class);
				Bmsc bmsc = mapperBmsc.selectBmsc(Integer.parseInt(params.get("bmscId")));
				mapBroadcast.put("bmscIp", bmsc.getIpaddress());
				mapBroadcast.put("bmscId", params.get("bmscId"));
				mapBroadcast.put("serviceType", params.get("serviceType"));
				
				//@ xmlMake & Send, recv
				String[] resStr = xmlManager.sendBroadcast(mapBroadcast, xmlManager.BMSC_XML_DELETE);
				
				//@ check return XML success
				if (!xmlManager.isSuccess(resStr[0]))
					return makeRetMsg("9000", resStr[0]);
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
	
	private String convertMysqlDateFormat(String dateTime, boolean add30Secons){
		if (dateTime == null)
			return null;
		
		dateTime = dateTime.replaceAll("-", "");
		dateTime = dateTime.replaceAll(":", "");
		dateTime = dateTime.replaceAll("T", "");
		dateTime = dateTime.replaceAll(" ", "");
		
		if (add30Secons)
			dateTime = dateTime.substring(0,12) + "30";
		
		return dateTime;
	}

	private String makeTransId() {
		if (transID >=  65535 )
			transID = 101;
		
		return "" + ++transID;
	}

	private Map<String, Object> makeRetMsg(String code, String resStr) {
		
		Map< String, Object > returnMap = new HashMap< String, Object >();
	    returnMap.put( "resultCode", code );
	    returnMap.put( "resultMsg", resStr);
	      
	    Map< String, Object > resultMap = new HashMap< String, Object >();
	    resultMap.put( "resultInfo", returnMap );
	              
		return (Map<String, Object>) resultMap;
	}
	
	private String exampleGBRSum(){
		Map< String, String > params = new HashMap<String, String>();
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		params.put("serviceAreaId", "3048");
		Map <String, String> retmap = mapper.selectGBRSum(params);
		return String.valueOf(retmap.get("GBRSum"));
	}
}

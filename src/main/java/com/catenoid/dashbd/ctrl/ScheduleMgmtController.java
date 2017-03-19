package com.catenoid.dashbd.ctrl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.catenoid.dashbd.Const;
import com.catenoid.dashbd.dao.BmscMapper;
import com.catenoid.dashbd.dao.OperatorMapper;
import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.Circle;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.Users;
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
	public ModelAndView schdMgmt(@RequestParam Map< String, Object > params, HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView( "schd/schdMgmt" );
		Users user = (Users) session.getAttribute("USER");
		try
		{
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			ScheduleMapper schedulemapper = sqlSession.getMapper(ScheduleMapper.class);
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
			
			List<Circle> circleList = operatorMapper.selectCircleListNameAll();
			String searchDate = Utils.getFileDate("yyyy-MM-dd");
			List<Map<String, String>> scList = schedulemapper.selectServiceClassAll();
			if(user.getGrade() == 9999){
				for (int i = circleList.size()-1; i > -1; i--) {
					if(Integer.parseInt(user.getCircleId()) != circleList.get(i).getCircle_id()){
						circleList.remove(i);
					}
				}
			}
			mv.addObject("circleList", circleList);
			mv.addObject("OperatorList", result);
			mv.addObject("BmscList", bmscs);
			mv.addObject("searchDate", searchDate); 
			mv.addObject("scList", scList);
			mv.addObject("userGrade", user.getGrade());
			
//			logger.info("GBRSum=", exampleGBRSum());
//			String transId = makeTransId();
//			params.put("transactionId", transId);
				
			//@ xmlMake & Send, recv
			//String resStr = xmlManager.sendBroadcast(params, xmlManager.BMSC_XML_RETRIEVE);
			//@ check return XML success
//			if (!xmlManager.isSuccess(resStr))
//				throw new Exception(resStr);
			//@ parsing
//			Map<String, String> xmlParam = xmlManager.paringRetrieve(resStr);
			
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
		mv.addObject("serviceAreaId", params.get("serviceAreaId")); 
		mv.addObject("bmscId", params.get("bmscId"));
		mv.addObject("searchDate", params.get("searchDate"));
		mv.addObject("title", params.get("title"));
		mv.addObject("category", params.get("category"));
		mv.addObject("type", params.get("type"));
		
		return mv;
	}
	
	/**
	 * 스케쥴 상세
	 */
	@RequestMapping(value = "view/schedule.do")
	public ModelAndView schedule(@RequestParam Map< String, String > params, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView("schd/schedule");
		Users user = (Users) session.getAttribute("USER");
		String mode = "update";
		String type = "area";
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		
		Map<String, String> mapContentUrl = mapper.selectSchduleContentURL(params);
		List<Map<String, String>> contentList = mapper.selectSchduleContentList(params);
		Map<String, String> mapSchedule = mapper.selectSchduleTime(params);
		List<Circle> circleList = operatorMapper.selectCircleListAll();
		List<Map<String, String>> serviceClassList = mapper.selectServiceClassAll();
		List<Map<String, String>> serviceIdList = mapper.selectServiceIdAll();
		int serviceIdIdx = mapper.selectServiceIdIdx();
		
		if (mapSchedule.get("BCID") == null || "".equals(mapSchedule.get("BCID"))){
			mode = "new";
			mapSchedule.put("service_name", mapSchedule.get("content_name"));
			mapSchedule.put("language", "en");
			mapSchedule.put("GBR", "1000");
			mapSchedule.put("QCI", "1");
			mapSchedule.put("FileRepair", "off");
			mapSchedule.put("receptionReport", "off");
			mapSchedule.put("schedule_start", mapSchedule.get("temp_start"));
			mapSchedule.put("schedule_stop", mapSchedule.get("temp_end"));
		}
		
		Gson gson = new Gson();
		String str = gson.toJson(contentList);
		
		if(mapSchedule.get("nationalYN").equals("Y")){
			type = "national";
		}else if(mapSchedule.get("serviceGroupId") == null){
			type = "area";
		}else{
			type = "group";
		}
		
		mv.addObject("type", type);
		mv.addObject("mode", mode);
		mv.addObject("circleList", circleList);
		mv.addObject("contentList", str);
		mv.addObject("serviceClassList", serviceClassList);
		mv.addObject("serviceIdList", serviceIdList);
		mv.addObject("serviceIdIdx", serviceIdIdx);
		mv.addObject("mapContentUrl", mapContentUrl);
		mv.addObject("mapSchedule", mapSchedule);
		mv.addObject("userGrade", user.getGrade());
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/getServiceIdTable.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String getServiceIdTable(@RequestBody HashMap<String, Object> params, HttpServletRequest req) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
        
		int offset = Integer.parseInt(String.valueOf(params.get("offset")));
		int limit = Integer.parseInt(String.valueOf(params.get("limit")));
		params.put("page", offset);
		params.put("perPage", limit);
		
        JSONObject jsonResult = new JSONObject();
		
        List<Map<String, String>> serviceIdList = mapper.selectServiceIdList(params);
		
		Gson gson = new Gson();
		String str = gson.toJson(serviceIdList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		jsonResult.put("total", mapper.selectServiceIdCount(params));
		
		return jsonResult.toString();
	}
	
	@RequestMapping( value = "/view/checkServiceId.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String checkServiceId(@RequestParam HashMap<String, Object> params, HttpServletRequest req) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		if(mapper.checkServiceId(params) > 0){
			return "EXIST";
		}
		
		return "SUCCESS";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/insertServiceId.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String insertServiceId(@RequestParam HashMap<String, Object> params, HttpServletRequest req) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		params.put("page", 0);
		params.put("perPage", 5);
		
		JSONObject jsonResult = new JSONObject();
		
		int check = mapper.selectServiceId(params);
		if(check == 0){
			int result = mapper.insertServiceId(params);
			if(result == 1) {
				List<Map<String, String>> serviceClassList = mapper.selectServiceIdList(params);
				Gson gson = new Gson();
				String str = gson.toJson(serviceClassList);
				org.json.JSONArray json = new org.json.JSONArray(str);
				jsonResult.put("rows", json);
				jsonResult.put("total", mapper.selectServiceClassCount(params));
			}
		} else {
			jsonResult.put("result", "EXIST");
		}
		
		return jsonResult.toString();
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/getServiceClassTable.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String getServiceClassTable(@RequestBody HashMap<String, Object> params, HttpServletRequest req) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
        
		int offset = Integer.parseInt(String.valueOf(params.get("offset")));
		int limit = Integer.parseInt(String.valueOf(params.get("limit")));
		params.put("page", offset);
		params.put("perPage", limit);
		
        JSONObject jsonResult = new JSONObject();
		
        List<Map<String, String>> serviceClassList = mapper.selectServiceClassList(params);
		
		Gson gson = new Gson();
		String str = gson.toJson(serviceClassList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		jsonResult.put("total", mapper.selectServiceClassCount(params));
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/selectServiceClassAll.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String selectServiceClassAll(HttpServletRequest req) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		JSONObject jsonResult = new JSONObject();
		
		List<Map<String, String>> serviceClassList = mapper.selectServiceClassAll();
		
		Gson gson = new Gson();
		String str = gson.toJson(serviceClassList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/selectServiceIdAll.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String selectServiceIdAll(HttpServletRequest req) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		JSONObject jsonResult = new JSONObject();
		
		List<Map<String, String>> serviceClassList = mapper.selectServiceIdAll();
		
		Gson gson = new Gson();
		String str = gson.toJson(serviceClassList);
		org.json.JSONArray json = new org.json.JSONArray(str);
		
		jsonResult.put("rows", json);
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/actionServiceClass.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String actionServiceClass(@RequestParam HashMap<String, Object> params, HttpServletRequest req) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		JSONObject jsonResult = new JSONObject();
		
		if(params.get("type").equals("edit")){
			mapper.editServiceClass(params);
		}else{
			mapper.deleteServiceClass(params);
		}
		
		jsonResult.put("result", "SUCCESS");
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/actionServiceId.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String actionServiceId(@RequestParam HashMap<String, Object> params, HttpServletRequest req) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		JSONObject jsonResult = new JSONObject();
		
		if(params.get("type").equals("edit")){
			mapper.editServiceId(params);
		}else{
			mapper.deleteServiceId(params);
		}
		
		jsonResult.put("result", "SUCCESS");
		
		return jsonResult.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/view/insertServiceClass.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String insertServiceClass(@RequestParam HashMap<String, Object> params, HttpServletRequest req) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		params.put("page", 0);
		params.put("perPage", 5);
		
		JSONObject jsonResult = new JSONObject();
		
		int check = mapper.selectServiceClass(params);
		if(check == 0){
			int result = mapper.insertServiceClass(params);
			if(result == 1) {
				List<Map<String, String>> serviceClassList = mapper.selectServiceClassList(params);
				Gson gson = new Gson();
				String str = gson.toJson(serviceClassList);
				org.json.JSONArray json = new org.json.JSONArray(str);
				jsonResult.put("rows", json);
				jsonResult.put("total", mapper.selectServiceClassCount(params));
			}
		} else {
			jsonResult.put("result", "EXIST");
		}
		
		return jsonResult.toString();
		
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
	
	@RequestMapping(value = "view/ .do")
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
			@RequestParam(value="contentId", required=false) List<String> contentId,
			@RequestParam(value="bcBasePattern", required=false) List<String> bcBasePattern,
            HttpServletRequest request, Locale locale ) {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		int ret;
		logger.info("sending param{}", params);
		List<List<String>> paramList = new ArrayList<List<String>>();
		paramList.add(schedule_start);		//0
		paramList.add(schedule_stop);		//1
		paramList.add(fileURI);				//2
		paramList.add(deliveryInfo_start);	//3
		paramList.add(deliveryInfo_end);	//4
		paramList.add(contentLength);		//5
		paramList.add(saidList);			//6
		paramList.add(mpdURI);				//7
		paramList.add(contentId);			//8
		paramList.add(bcBasePattern);		//9
		
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
		
		tmp = params.get("reportClientId");
		if (tmp == null){
			tmp="off";
			params.put("reportClientId", tmp);
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
			
			//Group으로 생성할 경우
			if(params.get("serviceAreaId").equals("")){
				saidList.clear();
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("cityId", params.get("serviceGroupId"));
				List<HashMap<String, String>> groupSaid = mapper.getGroupSaidList(param);
				String said = "";
				for (int i = 0; i < groupSaid.size(); i++) {
					if(i == groupSaid.size()-1){
						said += String.valueOf(groupSaid.get(i).get("sub_said"));
					}else{
						said += String.valueOf(groupSaid.get(i).get("sub_said"))+",";
					}
				}
				param.put("serviceAreaId", params.get("serviceGroupId"));
				saidList.add(said);
				paramList.add(6, saidList);
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
			
			if (bcid == null || "".equals(bcid)) {
				//@ insert broadcast_info  with flag which createBroadcast is successed or not
				
				if(params.get("serviceType").equals("streaming")){
					
				}
				else
				{
					/*if(!saidList.get(0).equals("")){
						params.put("serviceAreaId", params.get("serviceAreaId")+","+saidList.get(0));
					}*/
				}
				
				//서비스ID 숫자 증가
				ret = mapper.updateServiceIdIdx();
				//방송 정보 삽입
				ret = mapper.insertBroadcastInfo(params);
				//전송 후 본래의 스케쥴 업데이트
				ret = mapper.updateSchedule(params);
				
				//schedule start 갯수만큼
				for (int i = 0; i < paramList.get(0).size(); i++) {	
					
				}
				
				//@ insert schedule append said
				/*if (saidList.size() > 0) {
					for(int i = 0; i < saidList.size(); i++) {
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
				ret = mapper.updateSchedule(params);
				ret = mapper.updateBroadcastInfo(params);
				logger.info("updateSchedule ret{}", ret);
			}
            
			String scheduleType = "Schedule";
			String type = " Create";
			if(params.get("type").equals("national")){
				scheduleType = "National Schedule";
			}
			if(xmlMode == xmlManager.BMSC_XML_UPDATE){
				type = " Modified";
			}
			
			Users LogUser = (Users)request.getSession().getAttribute("USER");
			HashMap<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("reqType", "Schedule");
			logMap.put("reqSubType", "Add/Edit Schedule");
			logMap.put("reqUrl", "scheduleReg.do");
			logMap.put("reqCode", "SUCCESS");
			logMap.put("targetId", LogUser.getUserId());
			logMap.put(
					"reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId()
					+ " - "+scheduleType+type+"(Schedule ID : "+ params.get("id") + ", Service Type : "+ params.get("serviceType")
					+ ", Service ID : " + params.get("serviceId") + ", Service Class : " + params.get("serviceClass")
					+ ", Service Name : " + params.get("name") + ", Start Date : [" + convertDateFormat2(params.get("schedule_start")) +"]"
					+ ", End Date : [" + convertDateFormat2(params.get("schedule_stop")) +"]" );
			logMap.put("serviceType", params.get("serviceType"));
			logMap.put("serviceClass", params.get("serviceClass"));
			logMap.put("serviceId", params.get("serviceId"));
			logMap.put("said", params.get("serviceAreaId"));
			UsersMapper logMapper = sqlSession.getMapper(UsersMapper.class);
			logMapper.insertSystemAjaxLog(logMap);
			
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
			HashMap<String,String> param = new HashMap<String, String>();
			param.put("scheduleId", scheduleId);
			if(serviceType.equals("streaming")) 
			{
				Element contentSet = customType.getChild("contentSet");
				
				Element mpd = contentSet.getChild("mpd");
				param.put("contentId", contentSet.getAttributeValue("contentSetId"));
				param.put("mpdURI", mpd.getChild("mpdURI").getText());
			}
			else 
			{
				for (int i = 0; i < serviceArea.size(); i++) {
					List<Element> content = schedule.get(i).getChildren();
					for (int j = 0; j < content.size(); j++) {
						List<Element> child = content.get(j).getChildren();
						param.put("contentId", content.get(j).getAttributeValue("contentId"));
						if(serviceType.equals("fileDownload")){
							if(child.get(1).getAttributeValue("start") != null){
								param.put("startTime", convertDateFormat(child.get(1).getAttributeValue("start")));
								param.put("endTime", convertDateFormat(child.get(1).getAttributeValue("end")));
							}
						}
						param.put("fileURI", child.get(0).getText());
					}
				}
			}
			
			mapper.insertScheduleContent(param);
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
            HttpServletRequest request, Locale locale ) {
		
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
				
				String scheduleType = "Schedule";
				if(params.get("type").equals("national")){
					scheduleType = "National Schedule";
				}
				
				Users LogUser = (Users)request.getSession().getAttribute("USER");
				HashMap<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("reqType", "Schedule");
				logMap.put("reqSubType", "Delete Schedule");
				logMap.put("reqUrl", "delSchedule.do");
				logMap.put("reqCode", "SUCCESS");
				logMap.put("targetId", LogUser.getUserId());
				logMap.put(
						"reqMsg", "[" + Const.getLogTime() + "] User ID : " + LogUser.getUserId()
						+ " - "+scheduleType+" Deleted(Schedule ID : "+ params.get("id")+")");
				UsersMapper logMapper = sqlSession.getMapper(UsersMapper.class);
				logMapper.insertSystemAjaxLog(logMap);
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
	
	//2017-02-27T16:00:00.000+09:00 --> 2017-02-27 16:00:00
	private String convertDateFormat(String dateTime){
		String retStr = "";
		
		try {
			retStr = dateTime.substring(0,19).replace("T", " ");
		} catch (Exception e) {
			logger.error("", e);
		}
				
		return retStr;
		
	}

	//20170317174445 --> 2017-02-27 16:00:00
	private String convertDateFormat2(String dateTime){
		String retStr = "";
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			java.util.Date dateFrom = sdfFrom.parse(dateTime);
			retStr = sdfTo.format(dateFrom);
		} catch (Exception e) {
			logger.error("", e);
		}
		return retStr;
	}
	
}

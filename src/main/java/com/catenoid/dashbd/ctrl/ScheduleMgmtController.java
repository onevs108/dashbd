package com.catenoid.dashbd.ctrl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
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
import com.catenoid.dashbd.util.Base64Coder;
import com.catenoid.dashbd.util.HttpNetAgent;
import com.catenoid.dashbd.util.HttpNetAgentException;
import com.catenoid.dashbd.util.Utils;
import com.google.gson.Gson;

@SuppressWarnings("unchecked")
@Controller
public class ScheduleMgmtController {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	long transID = 101;	//101 ~ 65535
	
	@Autowired
	private SqlSession sqlSession;
	
//	private SqlSession sqlSession;
//	
//	public void setSqlSession(SqlSession sqlSession) {
//		this.sqlSession = sqlSession;
//	}
	
	@Autowired
	private XmlManager xmlManager;
	
	@Value("#{config['bmscId']}")
	private String bmscId;
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
	public ModelAndView schdMgmt(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView( "schd/schdMgmt" );
		Users user = (Users) session.getAttribute("USER");
		if(user == null){
			mv.setViewName("redirect:/login.do");
			return mv;
		}
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
	public Map<String, Object> getSchedule( @RequestParam Map<String, Object> params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);

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
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("resultCode", "1000");
        returnMap.put("resultMsg", "SUCCESS");
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("resultInfo", returnMap);
        resultMap.put("maxPosition", maxPosition);
        resultMap.put("contents", list);
        resultMap.put("viewStartHour", hour);
        
		return (Map<String, Object>) resultMap;
	}
	
	@RequestMapping(value = "view/checkBandwidth.do")
	@ResponseBody
	public Map<String, Object> checkBandwidth(@RequestParam Map<String, String> params, HttpServletRequest req, Locale locale) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		int inputBandwidth = Integer.parseInt(params.get("bandwidth"));
		if(params.get("saidList").equals("")){
			resultMap.put("result", "SUCCESS");
			resultMap.put("resultObj", "SUCCESS");
			return resultMap;
		}
		String searchString = "";
		List<Map<String, String>> bwList = mapper.checkBandwidth(params);
		for (int j = 0; j < bwList.size(); j++) {
			List<String> saidList = mapper.selectSaidRange(bwList.get(j));
			searchString = "";
			for (int i = 0; i < saidList.size(); i++) {
				if(i == saidList.size()-1){
					searchString += saidList.get(i);
				}else{
					searchString += saidList.get(i)+",|,";
				}
			}
			bwList.get(j).put("searchString", searchString);
			HashMap<String, String> bwMap = mapper.getEnableBandwidth(bwList.get(j));
			
			Double d1 = new Double(String.valueOf(bwMap.get("usableBW")));
			Double d2 = new Double(String.valueOf(bwMap.get("usedBW")));
			int usableBWint = d1.intValue();
			int usedBWint = d2.intValue();
			
			if(usableBWint - inputBandwidth < 0){
				resultMap.put("result", "bwExceed");
			    resultMap.put("resultObj", bwList.get(j));
			    return resultMap;
			}
			resultMap.put("usedBandwidth", usedBWint);
			resultMap.put("enableBandwidth", usableBWint);
		}

		if("bandwidth".equals(params.get("type"))) {
			List<HashMap<String, String>> bandWidthInfoList = mapper.getBandWidthInfoList(searchString);
			resultMap.put("bandWidthInfoList", bandWidthInfoList);
		}
		
		resultMap.put("result", "SUCCESS");
		resultMap.put("resultObj", "SUCCESS");
		
		return resultMap;
	}
	
	
	@RequestMapping(value = "view/checkExistSaid.do")
	@ResponseBody
	public Map<String, Object> checkExistSaid(@RequestParam Map<String, String> params, HttpServletRequest req, Locale locale) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		int result = mapper.checkExistSaid(params);
		if(result < 1) {
			resultMap.put("result", "FAIL");
		}else{
			resultMap.put("result", "SUCCESS");
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "view/saidUpload.do")
	@ResponseBody
	public Map<String, Object> saidUpload(MultipartHttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;
		String temp = "";
		String content = "";
		Iterator<String> itr = request.getFileNames();
		if (itr.hasNext()) {
			MultipartFile mpf = request.getFile(itr.next());
			try {
				inputStream = mpf.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

				br = new BufferedReader(inputStreamReader);

				while( (temp = br.readLine()) != null) {
	                content += temp;
	                break;
	            }
	             
	            System.out.println("================== 파일 내용 출력 ==================");
	            System.out.println(content);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
					inputStreamReader.close();
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		resultMap.put("result", content);
		
		return resultMap;
	}
	
	@RequestMapping( value = "/view/getScheduleForMain.do")
	@ResponseBody
	public Map<String, Object> getScheduleForMain( @RequestParam Map<String, Object> params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		String searchDate = Utils.getFileDate("yyyy-MM-dd");
		params.put("searchDate", searchDate);
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		List<Map> list = mapper.selectSchdule(params);
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", "SUCCESS");
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "resultInfo", returnMap );
        resultMap.put( "contents", list );
        
		return (Map<String, Object>) resultMap;
	}
	
	//스케쥴 최초 등록
	@RequestMapping(value = "view/addScheduleWithInitContent.do")
	@ResponseBody
	public Map<String, Object> addScheduleWithInitContent( @RequestParam Map<String, Object> params,
            HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		params.put("startTime", convertMysqlDateFormat((String)params.get("startTime"), false));
		params.put("endTime", convertMysqlDateFormat((String)params.get("endTime"), true));
		int ret = mapper.addScheduleWithInitContent(params);
		logger.info("addScheduleWithInitContent [ret={}]", ret);

		Map<String, Object> result = makeRetMsg("1000", "OK");
		result.put("scheduleId", params.get("id"));
		return result;
	}
	
	@RequestMapping(value = "view/modifyScheduleTime.do")
	@ResponseBody
	public Map<String, Object> modifyScheduleTime( @RequestParam Map< String, String > params,
            HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {

		HashMap<String, String> retValue = new HashMap<String, String>();
		List<List<String>> paramList = new ArrayList<List<String>>();
		List<String> saidData = new ArrayList<String>();
		
		try {
			BmscMapper mapperBmsc = sqlSession.getMapper(BmscMapper.class);
			Bmsc bmsc = mapperBmsc.selectBmsc(Integer.parseInt(params.get("bmscId")));
			
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			String startTime = convertMysqlDateFormat(params.get("startTime"), true);
			String endTime = convertMysqlDateFormat(params.get("endTime"), false);
			params.put("startTime", startTime);
			params.put("endTime", 	endTime);
			
			int ret = mapper.modifyScheduleTime(params);
			logger.info("modifyScheduleTime ret{}", ret);
			
			if (params.get("BCID") != null && !"".equals(params.get("BCID"))) {
				//@ update broadcast_info
				params.put("transactionId", makeTransId());
				params.put("schedule_start", startTime);
				params.put("schedule_stop", endTime);
				params.put("deliveryInfo_start", startTime);
				params.put("deliveryInfo_end", endTime);
				ret = mapper.updateBroadcastInfo(params);
				logger.info("updateSchedule ret{}", ret);

				Map<String, String> mapBroadcast = mapper.selectBroadcast(params);
				List<Map<String, String>> contentList = mapper.selectSchduleContentList(params);
				List<String> schedule_start = new ArrayList<String>();
				List<String> schedule_stop = new ArrayList<String>();
				List<String> fileURI = new ArrayList<String>();
				List<String> saidList = new ArrayList<String>();
				List<String> bcSaidList = new ArrayList<String>();
				List<String> contentLength = new ArrayList<String>();
				List<String> contentId = new ArrayList<String>();
				List<String> deliveryInfo_start = new ArrayList<String>();
				List<String> deliveryInfo_end = new ArrayList<String>();
				List<String> mpdURI = new ArrayList<String>();
				List<String> bcBasePattern = new ArrayList<String>();
				schedule_start.add(convertDateFormat3(mapBroadcast.get("schedule_start")));
				schedule_stop.add(convertDateFormat3(mapBroadcast.get("schedule_stop")));
				String[] saidArray = mapBroadcast.get("said").split(",");
				for (int i = 0; i < saidArray.length; i++) {
					saidList.add(saidArray[i]);
				}
				bcSaidList.add(mapBroadcast.get("bcSaidList"));
				contentLength.add(String.valueOf(contentList.size()));			
				for (int i = 0; i < contentList.size(); i++) {
					contentId.add(String.valueOf(contentList.get(i).get("content_id")));
					deliveryInfo_start.add(contentList.get(i).get("start_time"));
					deliveryInfo_end.add(contentList.get(i).get("end_time"));
					fileURI.add(contentList.get(i).get("url"));
					mpdURI.add(contentList.get(i).get("mpd"));
					bcBasePattern.add(contentList.get(i).get("bcBasePattern"));
				}
				paramList.add(schedule_start);		//0
				paramList.add(schedule_stop);		//1
				paramList.add(fileURI);				//2
				paramList.add(deliveryInfo_start);	//3
				paramList.add(deliveryInfo_end);	//4
				paramList.add(contentLength);		//5
				paramList.add(saidList);			//6
				paramList.add(mpdURI);				//7
				paramList.add(contentId);			//8
				paramList.add(bcSaidList);			//9
				paramList.add(bcBasePattern);		//10
				mapBroadcast.put("serviceType", mapBroadcast.get("service"));
				mapBroadcast.put("contentSetId", String.valueOf(contentList.get(0).get("content_id")));
				mapBroadcast.put("r12mpdURI", contentList.get(0).get("r12mpdURI"));
				mapBroadcast.put("bmscIp", bmsc.getIpaddress());
				
				String[] resStr = xmlManager.sendBroadcast(mapBroadcast, xmlManager.BMSC_XML_UPDATE, saidData, paramList);
				retValue = parseRes(resStr[0]);
				
				if (!xmlManager.isSuccess(resStr[0]))
					return makeRetMsg(retValue.get("code"), retValue.get("message"));
			}
		} catch(Exception e) {
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
		return makeRetMsg("1000", "OK");
	}
	

	@RequestMapping(value = "view/schdMgmtDetail.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public ModelAndView schdMgmtDetail( @RequestParam Map<String, Object> params,  HttpServletRequest req, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView( "schd/schdMgmtDetail" );
		Users user = (Users) session.getAttribute("USER");
		if(user == null) {
			mv.setViewName("redirect:/login.do");
			return mv;
		}
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		OperatorMapper operatorMapper = sqlSession.getMapper(OperatorMapper.class);
		List<Circle> circleList = operatorMapper.selectCircleListNameAll();
		if(user.getGrade() == 9999) {
			for (int i = circleList.size()-1; i > -1; i--) {
				if(Integer.parseInt(user.getCircleId()) != circleList.get(i).getCircle_id()) {
					circleList.remove(i);
				}
			}
		}
		List<Map<String, String>> scList = mapper.selectServiceClassAll();
		List<Map<String, String>> serviceIdList = mapper.selectServiceIdAll();
		mv.addObject("serviceAreaId", params.get("serviceAreaId")); 
		mv.addObject("bmscId", bmscId);
		mv.addObject("searchDate", Utils.getFileDate("yyyy-MM-dd"));
		mv.addObject("title", params.get("title"));
		mv.addObject("category", params.get("category"));
		mv.addObject("type", params.get("type"));
		mv.addObject("userGrade", user.getGrade());
		mv.addObject("circleList", circleList);
		mv.addObject("scList", scList);
		mv.addObject("serviceIdList", serviceIdList);
		mv.addObject("userGrade", user.getGrade());
		return mv;
	}
	
	/**
	 * 스케쥴 상세
	 */
	@RequestMapping(value = "view/schedule.do")
	public ModelAndView schedule(@RequestParam Map< String, String > params, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView("schd/schedule");
		Users user = (Users) session.getAttribute("USER");
		if(user == null){
			mv.setViewName("redirect:/login.do");
			return mv;
		}
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
		int moodInterval = mapper.getMoodInterval();
		
		if (mapSchedule.get("BCID") == null || "".equals(mapSchedule.get("BCID"))){
			mode = "new";
			mapSchedule.put("service_name", mapSchedule.get("content_name"));
			mapSchedule.put("language", "en");
			mapSchedule.put("GBR", "150000");
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
		mv.addObject("contentListJson", str);
		mv.addObject("serviceClassList", serviceClassList);
		mv.addObject("serviceIdList", serviceIdList);
		mv.addObject("contentsType", params.get("contentsType"));
		mv.addObject("mapContentUrl", mapContentUrl);
		mv.addObject("mapSchedule", mapSchedule);
		mv.addObject("userGrade", user.getGrade());
		mv.addObject("moodInterval", moodInterval);
		return mv;
	}
	
	
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
			result += mapper.insertServiceIdIdx(params);
			if(result == 2) {
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
	
	@RequestMapping( value = "/view/getServiceIdIdx.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String getServiceIdIdx(@RequestParam HashMap<String, Object> params, HttpServletRequest req) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		JSONObject jsonResult = new JSONObject();
		int result = mapper.getServiceIdIdx(params);
		jsonResult.put("idx", result);
		
		return jsonResult.toString();
	}
	
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
	
	
	@RequestMapping( value = "/view/actionServiceId.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String actionServiceId(@RequestParam HashMap<String, Object> params, HttpServletRequest req) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		
		JSONObject jsonResult = new JSONObject();
		
		if(params.get("type").equals("edit")){
			String serviceId = mapper.selectServiceIdName(params);
			params.put("beforeServiceId", serviceId);
			mapper.editServiceId(params);
			mapper.editServiceIdIdx(params);
		}else{
			mapper.deleteServiceId(params);
		}
		
		jsonResult.put("result", "SUCCESS");
		
		return jsonResult.toString();
	}
	
	
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
	
	
	@RequestMapping(value = "view/receiveMoodRequestAction.do")
	@ResponseBody
	public void receiveMoodRequestAction(@RequestParam HashMap<String,String> param, HttpServletRequest req) throws UnsupportedEncodingException, HttpNetAgentException {
		String retStr =
		 //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<message name=\"MOOD.REPORT\" type=\"REQUEST\">\n" +
			"   <transaction id=\"363\">\n" +
			"      <agentKey>dGVzdA</agentKey>\n" +
			"   </transaction>\n" +
			"   <request>\n" +
			"      <moodData>\n" +
			"         <crsId>1</crsId>\n" +
			"         <serviceId>mood:202</serviceId>\n" +
			"         <timestamp>2017-02-27T16:00:00 09:00</timestamp>\n" +
			"         <serviceArea>\n" +
			"            <saId>100</saId>\n" +
			"            <countUC>0</countUC>\n" +
			"            <countBC>0</countBC>\n" +
			"            <countConsumptionType />\n" +
			"	  	  </serviceArea>\n" +
			"      </moodData>\n" +
			"   </request>\n" +
			"</message>";
			String result = new HttpNetAgent().execute("http://127.0.0.1:8080/dashbd/view/receiveMoodRequest.do", "", retStr, false);
			System.out.println(result); 
	}
	
	@RequestMapping(value = "view/receiveMoodRequest.do")
	@ResponseBody
	public String receiveMoodRequest(@RequestParam HashMap<String,String> param, HttpServletRequest req, @RequestBody String postData) {
		ScheduleMapper scheduleMapper = sqlSession.getMapper(ScheduleMapper.class);
		SAXBuilder builder = new SAXBuilder();
		String returnStr = "";
		try {
			InputStream stream = new ByteArrayInputStream(java.net.URLDecoder.decode(postData, "utf-8").getBytes("utf-8"));
			Document jdomdoc = builder.build(stream);	//test XML 파일
			System.out.println(outString(jdomdoc));
			Element message = jdomdoc.getRootElement();
			Element transaction = (Element) message.getChildren().get(0);
			Element request = (Element) message.getChildren().get(1);
			String name = message.getAttributeValue("name");
			String reqType = name.substring(name.indexOf(".")+1).toUpperCase();
			if(reqType.equals("TIMESTAMP"))
			{
				Element service = request.getChild("service");
				Element timestamp = service.getChild("timestamp");
				String crsId = timestamp.getAttributeValue("crsId");
				List<HashMap<String, String>> currnetService = scheduleMapper.getTimeStampMoodService(crsId);
				
				System.out.println("================== Mood Receive Timestamp Start ==================");
				Element messageNew = new Element("message");
				messageNew.setAttribute(new Attribute("name", "SERVICE.TIMESTAMP"));	
				messageNew.setAttribute(new Attribute("type", "RESPONSE"));
				Document docNew = new Document(messageNew);
				docNew.setRootElement(messageNew);
				Element timestampNew = new Element("timestamp");
				timestampNew.setAttribute(new Attribute("crsId", crsId));
				
				for (int i = 0; i < currnetService.size(); i++) {
					Element timeset = new Element("timeset");
					Element serviceId = new Element("serviceId").setText(currnetService.get(i).get("serviceId"));
					Element timestampIn = new Element("timestamp").setText(convertDateFormatNew(String.valueOf(currnetService.get(i).get("timestamp"))));
					timeset.addContent(serviceId);
					timeset.addContent(timestampIn);
					timestampNew.addContent(timeset);
				}
				
				Element transactionNew = new Element("transaction");
				transactionNew.setAttribute(new Attribute("id", transaction.getAttributeValue("id")));
				transactionNew.addContent(new Element("agentKey").setText(transaction.getChild("agentKey").getText()));
				
				if(currnetService.size() == 0){
					Element result = new Element("result");
					Element code = new Element("code").setText("404");
					Element message0 = new Element("message").setText("Service Not Found");
					result.addContent(code);
					result.addContent(message0);
					transactionNew.addContent(result);
				}
				
				Element reply = new Element("reply");
				Element serviceNew = new Element("service");
				
				serviceNew.addContent(timestampNew);
				reply.addContent(serviceNew);
				
				docNew.getRootElement().addContent(transactionNew);
				if(currnetService.size() != 0){
					docNew.getRootElement().addContent(reply);
				}
				System.out.println(outString(docNew));
				System.out.println("================== Mood Receive Timestamp End ==================");
				returnStr = outString(docNew);
			}
			else if(reqType.equals("RETRIEVE")) {
				HashMap<String, String> reParam = new HashMap<String, String>();
				Element service = request.getChild("service");
				Element retrieve = service.getChild("retrieve");
				String crsId = retrieve.getChild("crsId").getText();
				String serviceId = retrieve.getChild("serviceId").getText();
				reParam.put("crsId", crsId);
				reParam.put("serviceId", serviceId);
				HashMap<String, String> retrieveInfo = scheduleMapper.getMoodRetrieve(reParam);
				
				System.out.println("================== Mood Receive Retrieve Start ==================");
				Element messageNew = new Element("message");
				messageNew.setAttribute(new Attribute("name", "SERVICE.RETRIEVE"));	
				messageNew.setAttribute(new Attribute("type", "RESPONSE"));
				Document docNew = new Document(messageNew);
				docNew.setRootElement(messageNew);
				Element retrieveNew = new Element("retrieve");
				Element crsIdNew = new Element("crsId").setText(crsId);
				Element serviceIdNew = new Element("serviceId").setText(serviceId);
				
				Element schedule = new Element("schedule");
				Element index = new Element("index").setText("1");
				Element start = new Element("start").setText(convertDateFormatNew(String.valueOf(retrieveInfo.get("schedule_start"))));
				Element stop = new Element("stop").setText(convertDateFormatNew(String.valueOf(retrieveInfo.get("schedule_stop"))));
				schedule.addContent(index);
				schedule.addContent(start);
				schedule.addContent(stop);
				
				Element contentSet = new Element("contentSet");
				Element associatedDelivery = new Element("associatedDelivery");
				Element consumptionReport = new Element("consumptionReport");
				Element reportInterval = new Element("reportInterval").setText(retrieveInfo.get("moodReportInterval"));
				Element moodUsageDataReportInterval = new Element("moodUsageDataReportInterval").setText(String.valueOf(retrieveInfo.get("moodUsageDataReportInterval")));
				
				consumptionReport.addContent(reportInterval);
				consumptionReport.addContent(moodUsageDataReportInterval);
				associatedDelivery.addContent(consumptionReport);
				
				Element transactionNew = new Element("transaction");
				transactionNew.setAttribute(new Attribute("id", transaction.getAttributeValue("id")));
				transactionNew.addContent(new Element("agentKey").setText(transaction.getChild("agentKey").getText()));		
				
				Element reply = new Element("reply");
				Element serviceNew = new Element("service");
				
				retrieveNew.addContent(crsIdNew);
				retrieveNew.addContent(serviceIdNew);
				retrieveNew.addContent(schedule);
				retrieveNew.addContent(contentSet);
				retrieveNew.addContent(associatedDelivery);
				serviceNew.addContent(retrieveNew);
				reply.addContent(serviceNew);
				
				docNew.getRootElement().addContent(transactionNew);
				docNew.getRootElement().addContent(reply);
				System.out.println(outString(docNew));
				System.out.println("================== Mood Receive Retrieve End ==================");
				returnStr = outString(docNew);
			}
			else
			{
				Element moodData = request.getChild("moodData");
				List<Element> serviceArea = moodData.getChildren("serviceArea");
				System.out.println("================== Mood Report Insert Start ==================");
				
				param.put("transaction", transaction.getAttributeValue("id"));
				param.put("agentKey", transaction.getChild("agentKey").getText());
				param.put("crsId", moodData.getChild("crsId").getText());
				param.put("serviceId", moodData.getChild("serviceId").getText());
				param.put("timestamp", convertDateFormat(moodData.getChild("timestamp").getText()));
				
				int result = scheduleMapper.checkMoodServiceId(param);
				if(result == 0){
					param.put("code", "500");
					param.put("message", "The service identified by ServiceID is not on-air");
					returnStr = makeMoodReportResponse(param);
					System.out.println(returnStr);
					return returnStr;
				}
				
				for (int i = 0; i < serviceArea.size(); i++) {
					param.put("saId", serviceArea.get(i).getChild("saId").getText());
					System.out.println("saId ======================== "+param.get("saId"));
					int count = scheduleMapper.checkMoodSaId(param);
					System.out.println("count ======================== "+count);
					if(count == 0){
						param.put("code", "501");
						param.put("message", "Service Area ID ("+param.get("saId")+") is not configured to ServiceID ("+param.get("serviceId")+")");
						returnStr = makeMoodReportResponse(param);
						System.out.println(returnStr);
						return returnStr;
					}
				}
				
				for (int i = 0; i < serviceArea.size(); i++) {
					param.put("saId", serviceArea.get(i).getChild("saId").getText());
					param.put("countUC", serviceArea.get(i).getChild("countUC").getText());
					param.put("countBC", serviceArea.get(i).getChild("countBC").getText());
					scheduleMapper.insertMoodRequest(param);
					param.put("code", "200");
					param.put("message", "Mood report Data is saved successfully");
					returnStr = makeMoodReportResponse(param);
					System.out.println(returnStr);
				}
				
				System.out.println("================== Mood Report Insert Complete ==================");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnStr;
	}
	
	@RequestMapping(value = "view/receiveHeartbeatBMSC.do", method=RequestMethod.POST)
	@ResponseBody
	public String receiveHeartbeatBMSC(@RequestParam HashMap<String,String> param, HttpServletRequest req, @RequestBody String postData) {
		SAXBuilder builder = new SAXBuilder();
		String returnStr = "";
		try {
			InputStream stream = new ByteArrayInputStream(java.net.URLDecoder.decode(postData, "utf-8").getBytes("utf-8"));
			Document doc = builder.build(stream);	//test XML 파일
			System.out.println(outString(doc));
			Element message = doc.getRootElement();
			Element transaction = (Element) message.getChildren().get(0);
			
			System.out.println("================== Heartbeat Start ==================");
			Element messageNew = new Element("message");
			messageNew.setAttribute(new Attribute("name", "HEARTBEAT.CHECK"));	
			messageNew.setAttribute(new Attribute("type", "RESPONSE"));
			Document docNew = new Document(messageNew);
			docNew.setRootElement(messageNew);
			Element resultNew = new Element("result");
			Element codeNew = new Element("code");
			Element message2New = new Element("message");
			resultNew.addContent(codeNew.setText("100"));
			resultNew.addContent(message2New.setText("OK"));
			
			Element transactionNew = new Element("transaction");
			transactionNew.setAttribute(new Attribute("id", transaction.getAttributeValue("id")));
			transactionNew.addContent(resultNew);		
			
			docNew.getRootElement().addContent(transactionNew);
			System.out.println(outString(docNew));
			System.out.println("================== Heartbeat End ==================");
			returnStr = outString(docNew);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnStr;
	}
	
	@RequestMapping(value = "view/receiveHeartbeatCRS.do", method=RequestMethod.OPTIONS)
	public void receiveHeartbeatCRS(HttpServletResponse response, HttpServletRequest req) {
		String returnStr = "";
		try {
			Document doc = new Document();
			Element root = new Element("message");
			Element transaction = new Element("transaction");
			transaction.setAttribute("id", makeTransId());
			Element result = new Element("result");
			Element code = new Element("code").setText("100");
			Element message = new Element("message").setText("OK");
			result.addContent(code);
			result.addContent(message);
			transaction.addContent(result);
			root.addContent(transaction);
			doc.setRootElement(root);
			returnStr = outString(doc);
			System.out.println(returnStr);
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
			response.getWriter().print(returnStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "view/receiveRetrieveRequest.do")
	@ResponseBody
	public String receiveRetrieveRequest(@RequestParam HashMap<String,String> param, HttpServletRequest req, @RequestBody String postData) {
		ScheduleMapper scheduleMapper = sqlSession.getMapper(ScheduleMapper.class);
		SAXBuilder builder = new SAXBuilder();
		HashMap<String, String> reParam = new HashMap<String, String>();
		String returnStr = "";
		try {
			InputStream stream = new ByteArrayInputStream(java.net.URLDecoder.decode(postData, "utf-8").getBytes("utf-8"));
			Document doc = builder.build(stream);	//test XML 파일
			System.out.println(outString(doc));
			Element message = doc.getRootElement();
			Element transaction = (Element) message.getChildren().get(0);
			Element request = (Element) message.getChildren().get(1);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	private String outString(Document doc){
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString(doc);
	}
	
	@RequestMapping(value = "view/scheduleReg.do")
	@ResponseBody
	public Map<String, Object> scheduleReg( @RequestParam Map< String, String > params,
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
			@RequestParam(value="bcSaidList", required=false) List<String> bcSaidList,
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
		paramList.add(bcSaidList);			//9
		paramList.add(bcBasePattern);		//10
		
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
		tmp = params.get("fileRepair");
		if (tmp == null){
			tmp="off";
			params.put("fileRepair", tmp);
		}
		tmp = params.get("receptionReport");
		if (tmp == null){
			tmp="off";
			params.put("receptionReport", tmp);
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
			String said = "";
			if(params.get("serviceAreaId").equals("")){
				saidList.clear();
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("cityId", params.get("serviceGroupId"));
				List<HashMap<String, String>> groupSaid = mapper.getGroupSaidList(param);
				for (int i = 0; i < groupSaid.size(); i++) {
					if(i == groupSaid.size()-1){
						said += String.valueOf(groupSaid.get(i).get("sub_said"));
					}else{
						said += String.valueOf(groupSaid.get(i).get("sub_said"))+",";
					}
				}
				params.put("serviceAreaId", said);
				saidList.add(said);
//				paramList.add(6, saidList);
			} else {
				if(params.get("serviceType").equals("streaming"))
				{
					for (int i = 0; i < saidList.size(); i++) {
						if(i == saidList.size()-1){
							said += String.valueOf(saidList.get(i));
						}else{
							said += String.valueOf(saidList.get(i))+",";
						}
					}
				}
				else
				{
					String[] saidArray = saidList.get(0).split(",");
					for (int i = 0; i < saidArray.length; i++) {
						if(i == saidArray.length-1){
							said += saidArray[i];
						}else{
							said += saidArray[i]+",";
						}
					}
				}
				params.put("serviceAreaId", said);
			}
			
			params.put("transactionId", transId);
			params.put("schedule_start", convertMysqlDateFormat(params.get("schedule_start"), false));
			params.put("schedule_stop", convertMysqlDateFormat(params.get("schedule_stop"),false));
			params.put("deliveryInfo_start", convertMysqlDateFormat(params.get("deliveryInfo_start"), false));
			params.put("deliveryInfo_end", convertMysqlDateFormat(params.get("deliveryInfo_end"),false));
			params.put("bmscIp", bmsc.getIpaddress());
			
			String[] resStr = xmlManager.sendBroadcast(params, xmlMode, saidData, paramList);
			HashMap<String, String> retValue = parseRes(resStr[0]);
			//@ check return XML success
			if (!xmlManager.isSuccess(resStr[0])){
				return makeRetMsg(retValue.get("code"), retValue.get("message"));
			}
			
			if (bcid == null || "".equals(bcid)) {
				if(params.get("serviceMode").equals("MooD")){
					String bcSaidListString = "";
					for (int i = 0; i < bcSaidList.size(); i++) {
						if(i == bcSaidList.size()-1){
							bcSaidListString += bcSaidList.get(i);
						}else{
							bcSaidListString += bcSaidList.get(i)+",";
						}
					}
					params.put("bcServiceArea", bcSaidListString);
				}
				
//				//서비스ID 숫자 증가
//				String svId = params.get("serviceId").toString();
//				params.put("serviceIdIdx", svId.substring(svId.lastIndexOf(":")+1));
//				params.put("serviceIdFormat", svId.substring(0, svId.lastIndexOf(":")));
//				ret = mapper.updateServiceIdIdx(params);
				//방송 정보 삽입
				ret = mapper.insertBroadcastInfo(params);
				//전송 후 본래의 스케쥴 업데이트
				ret = mapper.updateSchedule(params);
				if(params.get("serviceMode").equals("MooD")){
					HashMap<String, String> moodParam = new HashMap<String, String>();
					moodParam.put("serviceId", params.get("serviceId"));
					for (int i = 0; i < bcSaidList.size(); i++) {
						moodParam.put("said", bcSaidList.get(i));
						moodParam.put("mode", "Broadcast");
						mapper.insertMoodService(moodParam);
						saidList.remove(bcSaidList.get(i));
					}
					for (int i = 0; i < saidList.size(); i++) {
						moodParam.put("said", saidList.get(i));
						moodParam.put("mode", "Unicast");
						mapper.insertMoodService(moodParam);
					}
				}
				
				System.out.println("insertContentInfo ===== "+resStr[1]);
				insertContentInfo(resStr[1], params.get("id"));
			} else {
				ret = mapper.updateSchedule(params);
				ret = mapper.updateBroadcastInfo(params);
				int result = mapper.deleteScheduleContent(params);
				if(result > 0) {
					insertContentInfo(resStr[1], params.get("id"));
				}
			}
            
			String scheduleType = "Schedule";
			String type = " Create";
			if(params.get("type").equals("national")){
				scheduleType = "National Schedule";
			}else if(params.get("type").equals("emergency")){
				scheduleType = "Emergency Schedule";
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
			
	        return makeRetMsg(retValue.get("code"), retValue.get("message"));
		}catch(Exception e){
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
	}
	
	public HashMap<String, String> parseRes(String retStr) throws JDOMException, IOException{
		HashMap<String, String> retValue = new HashMap<String, String>(); 
		Document doc = new SAXBuilder().build(new StringReader(retStr));
		Element message = doc.getRootElement();
		Element result = message.getChild("transaction").getChild("result");
		retValue.put("code", result.getChild("code").getValue());
		retValue.put("message", result.getChild("message").getValue());
		
		return retValue;
	}
	
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
			String serviceMode = "noMooD";
			Element customType = service.getChild(serviceType);
			
			List<Element> schedule = customType.getChildren("schedule");
			List<Element> serviceArea = customType.getChildren("serviceArea");
			HashMap<String,String> param = new HashMap<String, String>();
			param.put("scheduleId", scheduleId);
			if(serviceType.equals("streaming")) 
			{
				serviceMode = customType.getAttribute("serviceMode").getValue();
				Element contentSet = customType.getChild("contentSet");
				
				Element mpd = contentSet.getChild("mpd");
				param.put("contentId", contentSet.getAttributeValue("contentSetId"));
				param.put("mpdURI", mpd.getChild("mpdURI").getText());
				if("MooD".equals(serviceMode)){
					Element mood = contentSet.getChild("mood");
					param.put("r12MpdURI", mood.getChild("r12MpdURI").getText());
					List<Element> bcBasePattern = mood.getChildren("bcBasePattern");
					String bcBasePatternStr = "";
					for (int i = 0; i < bcBasePattern.size(); i++) {
						if(i == bcBasePattern.size()-1){
							bcBasePatternStr += bcBasePattern.get(i).getText();
						}else{
							bcBasePatternStr += bcBasePattern.get(i).getText()+",";
						}
					}
					param.put("bcBasePattern", bcBasePatternStr);
				}
				System.out.println(serviceType);
				System.out.println(serviceMode);
				System.out.println("r12MpdURI ================ "+param.get("r12MpdURI"));
				System.out.println("bcBasePattern ================ "+param.get("bcBasePattern"));
				System.out.println(param);
				param.put("serviceType", serviceType);
				param.put("serviceMode", serviceMode);
				mapper.insertScheduleContent(param);
			}
			else 
			{
				for (int i = 0; i < serviceArea.size(); i++) {
					List<Element> content = schedule.get(i).getChildren();
					for (int j = 0; j < content.size(); j++) {
						List<Element> child = content.get(j).getChildren();
						if(serviceType.equals("fileDownload")){
							param.put("contentId", content.get(j).getAttributeValue("contentId"));
							if(child.get(1).getAttributeValue("start") != null){
								param.put("startTime", convertDateFormat(child.get(1).getAttributeValue("start")));
								param.put("endTime", convertDateFormat(child.get(1).getAttributeValue("end")));
							}
						}else if(serviceType.equals("carousel-SingleFile")){
							param.put("contentId", String.valueOf(j));
						}else if(serviceType.equals("carousel-MultipleFiles")){
							param.put("contentId", content.get(j).getAttributeValue("contentId"));
						}
						param.put("fileURI", child.get(0).getText());
						param.put("serviceType", serviceType);
						param.put("serviceMode", serviceMode);
						mapper.insertScheduleContent(param);
					}
					
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
	
	@RequestMapping(value = "view/delSchedule.do")
	@ResponseBody
	public Map<String, Object> delSchedule( @RequestParam Map< String, String > params,
            HttpServletRequest request, Locale locale ) {
		HashMap<String, String> retValue = new HashMap<String, String>();
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
				int deleteType = 4;
				if(params.get("deleteType").equals("btnAbort")){
					deleteType = xmlManager.BMSC_XML_ABORT;
				}
				String[] resStr = xmlManager.sendBroadcast(mapBroadcast, deleteType);
				retValue = parseRes(resStr[0]);
				//@ check return XML success
				if (!xmlManager.isSuccess(resStr[0]))
					return makeRetMsg(retValue.get("code"), retValue.get("message"));
				
				String scheduleType = "Schedule";
				if(params.get("type").equals("national")){
					scheduleType = "National Schedule";
				}else if(params.get("type").equals("emergency")){
					scheduleType = "Emergency Schedule";
				}
				int ret = mapper.updateSchedule4Del(params);
				logger.info("updateSchedule ret{}", ret);
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
				return makeRetMsg(retValue.get("code"), retValue.get("message"));
			}

			//@ update delete flag 
			int ret = mapper.updateSchedule4Del(params);
			logger.info("updateSchedule ret{}", ret);
			
			return makeRetMsg("1000", "OK");
			
		}catch(Exception e){
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
	}
	
	@RequestMapping(value = "view/delScheduleSeSM.do")
	@ResponseBody
	public Map<String, Object> delScheduleSeSM(@RequestParam Map< String, String > params, HttpServletRequest request, Locale locale ) {
		try{
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			
			String scheduleType = "Schedule";
			if(params.get("type").equals("national")){
				scheduleType = "National Schedule";
			}else if(params.get("type").equals("emergency")){
				scheduleType = "Emergency Schedule";
			}
			int ret = mapper.updateSchedule4Del(params);
			logger.info("updateSchedule ret{}", ret);
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
			
			return makeRetMsg("1000", "OK");
			
		}catch(Exception e){
			logger.error("", e);
			return makeRetMsg("9999", e.getMessage());
		}
	}
	
	@RequestMapping(value = "view/changeServiceMode.do")
	@ResponseBody
	public Map<String, Object> changeServiceMode(HttpServletRequest request, Locale locale ) {
		try{
			BmscMapper mapperBmsc = sqlSession.getMapper(BmscMapper.class);
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			String[] rtvs = new String[2];
			String reqBody = "";
			String respBody = "SUCCESS";
			String reqBodyCrs = "";
			String respBodyCrs = "";
			String serviceId = request.getParameter("serviceId");
			String said = request.getParameter("said");
			String mode = request.getParameter("mode");
			HashMap<String, String> saidParam = new HashMap<String, String>(); 
			saidParam.put("said", said);
			saidParam.put("ntype", mode);
			HashMap<String, List<HashMap<String, String>>> svId = new HashMap<String, List<HashMap<String,String>>>();
			List<HashMap<String, String>> saidList = new ArrayList<HashMap<String,String>>();
			saidList.add(saidParam);
			svId.put(serviceId, saidList);
			try {
				Bmsc bmsc = mapperBmsc.selectBmsc(793);
				System.out.println(" ================== BMSC Mood Update Start ================== ");
				String agentKey = Base64Coder.encode(bmsc.getIpaddress()); 
				reqBody = makeModityXml(svId, agentKey);
				respBody = new HttpNetAgent().execute("http://" + bmsc.getIpaddress() + bmsc.getCircle(), "", reqBody, false);
				System.out.println(" ================== BMSC Mood Update End ================== ");
				
				System.out.println(" ================== (said = "+saidParam.get("said")+") CRS Mood Update Start ================== ");
				HashMap<String, String> crsParam = new HashMap<String, String>();
				crsParam.put("serviceId", serviceId);
				crsParam.put("said", said);
				crsParam.put("ntype", mode);
				HashMap<String, String> crsInfo = mapper.getCrsInfo(crsParam);
				crsParam.put("crsId", String.valueOf(crsInfo.get("id")));
				String agentKeyCRS = Base64Coder.encode(crsInfo.get("ip"));
				reqBodyCrs = makeModityXmlCRS(serviceId, crsInfo, agentKeyCRS, saidParam.get("said"));
				respBodyCrs = new HttpNetAgent().execute("http://" + crsInfo.get("ip") + crsInfo.get("updateUrl"), "", reqBodyCrs, false);
				mapper.updateSaidMode(crsParam);
				System.out.println(" ================== ("+saidParam.get("said")+") CRS Mood Update End ================== ");
			} catch (Exception e) {
				e.printStackTrace();
				return makeRetMsg("N", "FAIL");
			}
			rtvs[0] = respBody;
			rtvs[1] = reqBody;
		} catch(Exception e) { 
			logger.error("", e);
			return makeRetMsg("N", "FAIL");
		}
		return makeRetMsg("S", "OK");
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
		Map<String, Object> returnMap = new HashMap<String, Object>();
	    returnMap.put( "resultCode", code );
	    returnMap.put( "resultMsg", resStr);
	      
	    Map<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put( "resultInfo", returnMap );
	              
		return (Map<String, Object>) resultMap;
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
	
	//2017-02-27T16:00:00.000+09:00
	private String convertDateFormatNew(String dateTime){
		String retStr = "";
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");
		try {
//				sdfFrom.setTimeZone(TimeZone.getTimeZone("IST"));
//				sdfTo.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date dateFrom = sdfFrom.parse(dateTime);
		    retStr = sdfTo.format(dateFrom);
		} catch (Exception e) {
			logger.error("", e);
		}
		return retStr;
	}
	
	//2017-02-27T16:00:00.000+09:00
	private String convertDateFormatNew2(String dateTime){
		String retStr = "";
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");
		try {
			Date dateFrom = sdfFrom.parse(dateTime);
		    retStr = sdfTo.format(dateFrom);
		} catch (Exception e) {
		}
		return retStr;
	}
	
	
	//Wed Mar 15 17:17:00 2017 --> 2017-02-27 16:00:00
	private String convertDateFormat3(String dateTime){
		String retStr = "";
		
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateFrom = sdfFrom.parse(dateTime);
			retStr = sdfTo.format(dateFrom);
		} catch (Exception e) {
			logger.error("", e);
		}
		return retStr;
	}
	
	//현재 시간 출력
	private String convertDateFormat4(String dateTime){
		Date dt = new Date();
		
		String retStr = "";
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");
		retStr = sdfTo.format(dt);
		return retStr;
	}
	
	private String makeMoodReportResponse(HashMap<String, String> param) {
		String respStr = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<message name=\"MOOD.REPORT\" type=\"RESPONSE\">\n" +
			"  <transaction id=\""+param.get("transaction")+"\">\n" +
			"    <agentKey>"+param.get("agentKey")+"</agentKey>\n" +
			"  </transaction>\n" +
			"  <reply>\n" +
			"    <moodData>\n" +
			"      <code>"+param.get("code")+"</code>\n" +
			"      <message>"+param.get("message")+"</message>\n" +
			"    </moodData>\n" +
			"  </reply>\n" +
			"</message>";
		return respStr;
	}
	
	private String makeModityXmlCRS(String tempSvId, HashMap<String, String> crsInfo, String agentKeyCRS, String said) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		Map<String, String> bcParam = new HashMap<String, String>();
		bcParam.put("serviceId", tempSvId);
		bcParam.put("BCID", mapper.getBcIdFromServiceId(bcParam));
		Map<String, String> params = mapper.selectBroadcast(bcParam);
		bcParam.put("id", mapper.getScheduleIdFromBCID(bcParam));
		
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));	
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(agentKeyCRS));
		
		doc.getRootElement().addContent(transaction);

		Element request = new Element("request");
		Element service = new Element("service");
		
		String serviceId = params.get("serviceId");
		if (serviceId == null) {
			serviceId = "";
		}
		
		Element update = new Element("update");
		
		update.addContent(new Element("crsid").setText(String.valueOf(crsInfo.get("id"))));
		update.addContent(new Element("timestamp").setText(convertDateFormat4(new Date().toString())));
		update.addContent(new Element("serviceId").setText(serviceId));
		
		Element associatedDelivery = new Element("associatedDelivery");
		Element consumptionReport = null;
		consumptionReport = new Element("consumptionReport");
		consumptionReport.addContent(new Element("reportInterval").setText(params.get("moodReportInterval")));
		consumptionReport.addContent(new Element("moodUsageDataReportInterval").setText(String.valueOf(crsInfo.get("moodUsageDataReportInterval"))));
		associatedDelivery.addContent(consumptionReport);
		
		Element schedule = null;
		schedule = new Element("schedule");
		schedule.addContent(new Element("index").setText(String.valueOf(1)));
		//time format ex) 2015-04-10T17:24:09.000+09:00 
		schedule.addContent(new Element("start").setText(convertDateFormatNew2(params.get("schedule_start"))));
		schedule.addContent(new Element("stop").setText(convertDateFormatNew2(params.get("schedule_stop"))));
		Element contentSet = new Element("contentSet");
		Element serviceArea = new Element("serviceArea");
		
		serviceArea.addContent(new Element("said").setText(said));
		contentSet.addContent(serviceArea);
		update.addContent(schedule);
		update.addContent(contentSet);
		update.addContent(associatedDelivery);
		
		service.addContent(update);
		request.addContent(service);
		
		doc.getRootElement().addContent(request);
		System.out.println("============================ Mood Update Start ============================");
		System.out.println(outString(doc));
		System.out.println("============================ Mood Update End ============================");
		return outString(doc);
	}
	
	private String makeModityXml(HashMap<String, List<HashMap<String, String>>> svId, String agentKey) {
		ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
		Map<String, String> bcParam = new HashMap<String, String>();
		List<Map<String, String>> contentList = null;
		Iterator<String> it = svId.keySet().iterator();
		String tempSvId = it.next();
		bcParam.put("serviceId", tempSvId);
		bcParam.put("BCID", mapper.getBcIdFromServiceId(bcParam));
		Map<String, String> params = mapper.selectBroadcast(bcParam);
		bcParam.put("id", mapper.getScheduleIdFromBCID(bcParam));
		if(bcParam.get("BCID") != null){
			contentList = mapper.selectSchduleContentList(bcParam);
		}
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		//common- depth one
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(agentKey));					//key ??
		
		doc.getRootElement().addContent(transaction);

		Element parameters = new Element("parameters");
		Element services = new Element("services");
		Element service = new Element("service");
		
		Element name = new Element("name");
		name.setAttribute(new Attribute("lang", params.get("serviceNameLanguage")));										//??
		name.setText(params.get("name"));
		Element serviceLanguage = new Element("serviceLanguage");
		serviceLanguage.setText(params.get("serviceLanguage"));
		
		Element transferConfig = new Element("transferConfig");
		Element QoS = new Element("QoS");
		QoS.addContent( new Element("GBR").setText(params.get("GBR")));
		QoS.addContent( new Element("QCI").setText(params.get("QCI")));
		
		Element ARP = new Element("ARP");
		ARP.addContent( new Element("level").setText(params.get("level")));
		String flag = "on".equals(params.get("preEmptionCapabiity"))? "0" : "1";
		ARP.addContent( new Element("preEmptionCapability").setText(flag));
		flag = "on".equals(params.get("preEmptionVulnerability"))? "0" : "1";
		ARP.addContent( new Element("preEmptionVulnerability").setText(flag));
		QoS.addContent(ARP);
		
		Element FEC = new Element("FEC");
		FEC.addContent( new Element("fecType").setText(params.get("fecType")));
		String ratio = "0";
		
		if (null != params.get("fecRatio") && !"".equals(params.get("fecRatio"))){
			ratio = params.get("fecRatio");
		}
		
		FEC.addContent( new Element("fecRatio").setText(ratio));

		transferConfig.addContent(QoS);
		transferConfig.addContent(FEC);
		//schedule 은 배열이 될수도 있음. 일딴 한개만 처리
		Element receptionReport = null;
		Element associatedDelivery = new Element("associatedDelivery");
		
		if ("on".equals(params.get("receptionReport"))){
			receptionReport = new Element("receptionReport");
			receptionReport.setAttribute(new Attribute("reportType", params.get("reportType")));
			receptionReport.setAttribute(new Attribute("samplePercentage", params.get("samplePercentage")));
			receptionReport.setAttribute(new Attribute("cancelled", "false"));
			receptionReport.setAttribute(new Attribute("offsetTime", params.get("offsetTime")));
			receptionReport.setAttribute(new Attribute("randomTime", params.get("randomTime")));			
		}
		
		String serviceId = params.get("serviceId");
		if (serviceId == null) {
			serviceId = "";
		}
		
		Element serviceType = null;
		service.setAttribute(new Attribute("serviceType", "streaming"));
		
		Element streaming = new Element("streaming");
		streaming.setAttribute(new Attribute("serviceId", serviceId));
		if ( null != params.get("serviceClass") && !"".equals(params.get("serviceClass"))){
			streaming.setAttribute(new Attribute("serviceClass", params.get("serviceClass")));
		}
		streaming.setAttribute(new Attribute("serviceMode", params.get("serviceMode")));
		
		transferConfig.addContent(new Element("SegmentAvailableOffset").setText(params.get("segmentAvailableOffset")));
		
		if ("on".equals(params.get("reportType"))){
			associatedDelivery.addContent(receptionReport);
		}
		
		if ( null != params.get("name") && !"".equals(params.get("name")))
			streaming.addContent(name);
		
		if ( null != params.get("serviceLanguage") && !"".equals(params.get("serviceLanguage")))
			streaming.addContent(serviceLanguage);
	 	
		streaming.addContent(transferConfig);
		Element consumptionReport = null;
		if ("MooD".equals(params.get("serviceMode"))){
			consumptionReport = new Element("consumptionReport");
			consumptionReport.setAttribute(new Attribute("location", params.get("moodLocation")));
			consumptionReport.setAttribute(new Attribute("reportClientId", "true"));
			consumptionReport.setAttribute(new Attribute("reportInterval", params.get("moodReportInterval")));
			consumptionReport.setAttribute(new Attribute("offsetTime", params.get("moodOffsetTime")));
			consumptionReport.setAttribute(new Attribute("randomTime", params.get("moodRandomTimePeriod")));			
			consumptionReport.setAttribute(new Attribute("samplePercentage", params.get("moodSamplePercentage")));
			associatedDelivery.addContent(consumptionReport);
		}
		serviceType = streaming;
		
		Element schedule = null;
		schedule = new Element("schedule");
		schedule.setAttribute(new Attribute("index", "1"));
		schedule.setAttribute(new Attribute("cancelled", "false"));
		//time format ex) 2015-04-10T17:24:09.000+09:00 
		schedule.setAttribute(new Attribute("start", convertDateFormatNew2(params.get("schedule_start"))));
		schedule.setAttribute(new Attribute("stop", convertDateFormatNew2(params.get("schedule_stop"))));
		
		Element contentSet = new Element("contentSet");
		contentSet.setAttribute(new Attribute("contentSetId", String.valueOf(contentList.get(0).get("content_id")))); 					
		contentSet.setAttribute(new Attribute("cancelled", "false"));				
							
		Element serviceArea = new Element("serviceArea");
		String[] saidList = params.get("said").split(",");
		for (int i = 0; i < saidList.length; i++) {
			serviceArea.addContent( new Element("said").setText(saidList[i]));
		}
		
		Element mpd = new Element("mpd");
		mpd.setAttribute(new Attribute("changed", "false"));									
		mpd.addContent(new Element("mpdURI").setText(contentList.get(0).get("mpd")));

		Element mood = new Element("mood");
		Element r12MpdURI = new Element("r12MpdURI");
		System.out.println(contentList.get(0).get("r12mpdURI"));
		r12MpdURI.setText(contentList.get(0).get("r12mpdURI"));
		r12MpdURI.setAttribute(new Attribute("changed", "false"));				
		mood.addContent(r12MpdURI);
		
		Element bcBasePattern = new Element("bcBasePattern");
		if(contentList.size() != 0){
			if(contentList.get(0).get("bcBasePattern") != null){
				String[] pattern = contentList.get(0).get("bcBasePattern").split(",");
				for (int k = 0; k < pattern.length; k++) {
					bcBasePattern.setText(pattern[k]);
					if(bcBasePattern.getChildren().size() > 0){
						mood.addContent(bcBasePattern);
					}
				}
			}
		}
		
		//핵심부분
		Element bcServiceArea = new Element("bcServiceArea");
		for (int j = 0; j < svId.get(tempSvId).size(); j++) {
			System.out.println(svId.get(tempSvId).get(j).get("ntype"));
			System.out.println(svId.get(tempSvId).get(j).get("said"));
			if(svId.get(tempSvId).get(j).get("ntype").equals("Unicast")){
				bcServiceArea.addContent(new Element("said").setText(svId.get(tempSvId).get(j).get("said")));
			}
		}	
		
		System.out.println(bcServiceArea.getChildren().size());
		if(bcServiceArea.getChildren().size() > 0){
			mood.addContent(bcServiceArea);
		}
		
		contentSet.addContent(serviceArea);
		contentSet.addContent(mpd);
		contentSet.addContent(mood);
		serviceType.addContent(schedule);
		serviceType.addContent(contentSet);
		if ("MooD".equals(params.get("serviceMode")) || "on".equals(params.get("receptionReport"))){
			serviceType.addContent(associatedDelivery);
		} 
		
		service.addContent(serviceType);
		services.addContent(service);
		parameters.addContent(services);
		
		doc.getRootElement().addContent(parameters);
//		System.out.println(outString(doc));
		return outString(doc);
	}
	
}




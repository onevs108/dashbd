package com.catenoid.dashbd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.quartz.CronTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.Log;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.ServiceAreaPermissionAp;
import com.catenoid.dashbd.dao.model.SystemBroadCastContents;
import com.catenoid.dashbd.dao.model.SystemDatabaseBackup;
import com.catenoid.dashbd.dao.model.SystemIncomingLog;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.BmscService;
import com.google.gson.Gson;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class SystemController{

	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	@Value("#{config['main.contents.max']}")
	private Integer contentMax;
	
	@Value("#{config['b2.server.hostName']}")
	private String serverHostName;
	
	@Value("#{config['b2.server.status']}")
	private String serverStatus;
	
	@Value("#{config['b2.server.start']}")
	private String serverStart;
	
	@Value("#{config['b2.server.stop']}")
	private String serverStop;
	
	@Value("#{config['b2.server.move']}")
	private String serverMove;
	
	@Value("#{config['b2.database.status']}")
	private String databaseStatus;
	
	@Value("#{config['b2.database.start']}")
	private String databaseStart;
	
	@Value("#{config['b2.database.stop']}")
	private String databaseStop;
	
	@Value("#{config['b2.database.backup']}")
	private String databaseBackup;
	
	@Value("#{config['b2.database.backupprog']}")
	private String databaseBackupprog;
	
	@Value("#{config['b2.database.restore']}")
	private String databaseRestore;
	
	@Value("#{config['b2.database.restoreprog']}")
	private String databaseRestoreprog;

	@Autowired
	private BmscService bmscServiceImpl;
	
	@RequestMapping(value = "/resources/systemMgmt.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView systemMgmtView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("systemMgmt");
		
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		Integer page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		Integer perPage = 50;
		
		OperatorSearchParam searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		
		List<Operator> result = mapper.getServiceAreaOperator(searchParam);
		
		Operator initOperator = result.get(0);
		
		searchParam = new OperatorSearchParam();
		searchParam.setPage((page-1) * perPage);
		searchParam.setPerPage(perPage);
		searchParam.setOperatorId(initOperator.getId());
		
		List<Bmsc> bmscs = mapper.getSeviceAreaBmSc(searchParam);
		
		mv.addObject("OperatorList", result);
		mv.addObject("BmscList", bmscs);
		
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/getPermissionList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getPermissionList(@RequestBody String body) {
		logger.info("-> [body = {}]", body);

		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String operatorIdToStr = (String) requestJson.get("operatorId");
			Integer operatorId = operatorIdToStr == null ? 0 : Integer.parseInt(operatorIdToStr);
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			if (sort == null || sort.isEmpty()) sort = null;
			if (order == null || order.isEmpty()) order = null;
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			HashMap<String, Object> searchParam = new HashMap();
			searchParam.put("operatorId", operatorId);
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset+1);
			searchParam.put("end", offset + limit);
			
			List<ServiceAreaPermissionAp> datas = mapper.getPermissionList(searchParam);
			
			JSONArray rows = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				ServiceAreaPermissionAp data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("rownum", data.getRownum());
				obj.put("permissionId", data.getPermissionId());
				obj.put("permissionName", data.getPermissionName());
				obj.put("permissionCount", data.getPermissionCount());
				obj.put("totalCount", data.getTotalCount());
				rows.add(obj);
				
				if( i == 0 ) {
					jsonResult.put("total", data.getTotalCount());
				}
			}
			
			jsonResult.put("rows", rows);
			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getPermissionList");
			syslogMap.put("reqUrl", "api/getPermissionList.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);

		} catch (Exception e) {
			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getPermissionList");
			syslogMap.put("reqUrl", "api/getPermissionList.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/getIncomingTrafficList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getIncomingTrafficList(@RequestBody String body) {
		logger.info("-> [body = {}]", body);

		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			String searchYear = (String) requestJson.get("searchYear");
			String searchMonth = (String) requestJson.get("searchMonth");
			String searchDay = (String) requestJson.get("searchDay");
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			if (sort == null || sort.isEmpty()) sort = null;
			if (order == null || order.isEmpty()) order = null;
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			HashMap<String, Object> searchParam = new HashMap();
			searchParam.put("searchYear", searchYear);
			searchParam.put("searchMonth", searchMonth);
			searchParam.put("searchDay", searchDay);
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset+1);
			searchParam.put("end", offset + limit);
			
			List<SystemIncomingLog> datas = mapper.getIncomingTrafficList(searchParam);

			JSONArray rows = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				SystemIncomingLog data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("rownum", data.getRownum());
				obj.put("reqType", data.getReqType());
				obj.put("successCount", data.getSuccessCount());
				obj.put("failCount", data.getFailCount());
				obj.put("totPercentage", Math.round((float)(((double)data.getSuccessCount()/(double)(data.getSuccessCount()+data.getFailCount()))*100)));
				obj.put("totalCount", data.getTotalCount());
				rows.add(obj);
				
				if( i == 0 ) {
					jsonResult.put("total", data.getTotalCount());
				}
			}
			
			jsonResult.put("rows", rows);

			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getIncomingTrafficList");
			syslogMap.put("reqUrl", "api/getIncomingTrafficList.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (Exception e) {
			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getIncomingTrafficList");
			syslogMap.put("reqUrl", "api/getIncomingTrafficList.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/getInterTrafficList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getInterTrafficList(@RequestBody String body) {
		logger.info("-> [body = {}]", body);

		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			String searchOperator = (String) requestJson.get("searchOperator");
			String searchBmsc = (String) requestJson.get("searchBmsc");
			String searchYear = (String) requestJson.get("searchYear");
			String searchMonth = (String) requestJson.get("searchMonth");
			String searchDay = (String) requestJson.get("searchDay");
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			if (sort == null || sort.isEmpty()) sort = null;
			if (order == null || order.isEmpty()) order = null;
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			HashMap<String, Object> searchParam = new HashMap();
			searchParam.put("searchOperator", searchOperator);
			searchParam.put("searchBmsc", searchBmsc);
			searchParam.put("searchYear", searchYear);
			searchParam.put("searchMonth", searchMonth);
			searchParam.put("searchDay", searchDay);
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset+1);
			searchParam.put("end", offset + limit);
			
			List<SystemIncomingLog> datas = mapper.getInterTrafficList(searchParam);

			JSONArray rows = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				SystemIncomingLog data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("rownum", data.getRownum());
				obj.put("reqType", data.getReqType());
				obj.put("successCount", data.getSuccessCount());
				obj.put("failCount", data.getFailCount());
				obj.put("totPercentage", Math.round((float)(((double)data.getSuccessCount()/(double)(data.getSuccessCount()+data.getFailCount()))*100)));
				rows.add(obj);
			}
			
			jsonResult.put("total", datas.size());
			jsonResult.put("rows", rows);

			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getInterTrafficList");
			syslogMap.put("reqUrl", "api/getInterTrafficList.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (Exception e) {
			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getInterTrafficList");
			syslogMap.put("reqUrl", "api/getInterTrafficList.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	@RequestMapping(value = "/resources/systemConfMgmt.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ModelAndView systemConfMgmt(HttpServletRequest request){
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		ModelAndView mv = new ModelAndView("systemConfMgmt");
		String RETURN_SHELL = "Session_Cnt:";

		String exeHostName = String.format("%s", serverHostName);
		String exeServerStatus = String.format("%s", serverStatus);
		String exeDatabaseStatus = String.format("%s", databaseStatus);
		try{
			Runtime runtimeHostName = Runtime.getRuntime();
			Process processHostName = runtimeHostName.exec(exeHostName);
			InputStream isHostName = processHostName.getInputStream();
			InputStreamReader isrHostName = new InputStreamReader(isHostName);
			BufferedReader brHostName = new BufferedReader(isrHostName);
			String lineHostName;
			StringBuffer sbHostName = new StringBuffer();
			while((lineHostName = brHostName.readLine()) != null) {
				sbHostName.append(lineHostName);
			}
			System.out.println("serverHostName   >>  " + sbHostName);
			//String sessionHostNameCnt = sbHostName.substring(RETURN_SHELL.length());
			String sessionHostNameCnt = sbHostName.toString();
			mv.addObject("sessionCntsessionHostNameCnt", sessionHostNameCnt);

			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "sessionCntsessionHostNameCnt");
			syslogMap.put("reqUrl", "resources/systemConfMgmt.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
	    }catch(Exception e){
	    	logger.error("",e);

			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "sessionCntsessionHostNameCnt");
			syslogMap.put("reqUrl", "resources/systemConfMgmt.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
	    	mv.addObject("sessionCntsessionHostNameCnt", "-1");
	    }
		try{
			Runtime runtimeServerStatus = Runtime.getRuntime();
            Process processServerStatus = runtimeServerStatus.exec(exeServerStatus);
            InputStream isServerStatus = processServerStatus.getInputStream();
            InputStreamReader isrServerStatus = new InputStreamReader(isServerStatus);
            BufferedReader brServerStatus = new BufferedReader(isrServerStatus);
            String lineServerStatus;
            StringBuffer sbServerStatus = new StringBuffer();
            while((lineServerStatus = brServerStatus.readLine()) != null) {
          	  sbServerStatus.append(lineServerStatus);
            }

			System.out.println("exeServerStatus   >>  " + sbServerStatus);
			//String sessionServerStatuseCnt = sbServerStatus.substring(RETURN_SHELL.length());
			String sessionServerStatuseCnt = sbServerStatus.toString();
			mv.addObject("sessionServerStatuseCnt", sessionServerStatuseCnt);

			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "sessionServerStatuseCnt");
			syslogMap.put("reqUrl", "resources/systemConfMgmt.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
	    }catch(Exception e){
	    	logger.error("",e);
			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "sessionServerStatuseCnt");
			syslogMap.put("reqUrl", "resources/systemConfMgmt.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
	    	mv.addObject("sessionServerStatuseCnt", "-1");
	    }
		try{
			Runtime runtimeDatabaseStatus = Runtime.getRuntime();
            Process processDatabaseStatus = runtimeDatabaseStatus.exec(exeDatabaseStatus);
            InputStream isDatabaseStatus = processDatabaseStatus.getInputStream();
            InputStreamReader isrDatabaseStatus = new InputStreamReader(isDatabaseStatus);
            BufferedReader brDatabaseStatus = new BufferedReader(isrDatabaseStatus);
            String lineDatabaseStatus;
            StringBuffer sbDatabaseStatus = new StringBuffer();
            while((lineDatabaseStatus = brDatabaseStatus.readLine()) != null) {
          	  sbDatabaseStatus.append(lineDatabaseStatus);
            }

			System.out.println("exeDatabaseStatus   >>  " + sbDatabaseStatus);
			
			//String sessionDatabaseStatuseCnt = sbDatabaseStatus.substring(RETURN_SHELL.length());
			String sessionDatabaseStatuseCnt = sbDatabaseStatus.toString();
			mv.addObject("sessionDatabaseStatuseCnt", sessionDatabaseStatuseCnt);

			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "sessionDatabaseStatuseCnt");
			syslogMap.put("reqUrl", "resources/systemConfMgmt");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
	    }catch(Exception e){
	    	logger.error("",e);
			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "sessionServerStatuseCnt");
			syslogMap.put("reqUrl", "resources/systemConfMgmt.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
	    	mv.addObject("sessionDatabaseStatuseCnt", "-1");
	    }
		return mv;
	}
	
	@RequestMapping(value = "/resources/systemConfControll.do")
	@ResponseBody
	public Map< String, Object > systemConfControll( @RequestParam Map< String, String > params, HttpServletRequest req, Locale locale ) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		String exeTargetURI = "";
		String RETURN_SHELL = "Session_Cnt:";
		try{
			
			if(params.get("serverTarget").equals("DataBaseStop")){
				exeTargetURI = String.format("%s", databaseStop);
			}else if(params.get("serverTarget").equals("DataBaseStart")){
				exeTargetURI = String.format("%s", databaseStart);
			}else if(params.get("serverTarget").equals("TomcatStart")){
				exeTargetURI = String.format("%s", serverStart);
			}else if(params.get("serverTarget").equals("TomcatStop")){
				exeTargetURI = String.format("%s", serverStop);
			}else if(params.get("serverTarget").equals("moveServer")){
				exeTargetURI = String.format("%s", serverMove);
			}
			
			Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(exeTargetURI);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer sb = new StringBuffer();
            while((line = br.readLine()) != null) {
          	  sb.append(line);
            }
            
            System.out.println("exeTargetURI   >>  " + exeTargetURI);
            System.out.println("exeTargetURI SB   >>  " + sb);
          
			//String sessionCnt = sb.substring(RETURN_SHELL.length());
			String sessionCnt = sb.toString();
			
			Map< String, Object > returnMap = new HashMap< String, Object >();
			returnMap.put("sessionCnt", sessionCnt);
	        returnMap.put( "resultCode", "1000" );
	        returnMap.put( "resultMsg", "SUCCESS");
	        Map< String, Object > resultMap = new HashMap< String, Object >();
	        resultMap.put( "resultInfo", returnMap );

			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "systemConfControll");
			syslogMap.put("reqUrl", "resources/systemConfControll");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return (Map<String, Object>) resultMap;
			
		}catch(Exception e){
			logger.error("", e);

			syslogMap.put("reqType", "system Conf Mgmt");
			syslogMap.put("reqSubType", "systemConfControll");
			syslogMap.put("reqUrl", "resources/systemConfControll");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			return makeRetMsg("9999", e.getMessage());
		}
	}

	private Map<String, Object> makeRetMsg(String code, String resStr) {
		
		Map< String, Object > returnMap = new HashMap< String, Object >();
		returnMap.put("sessionCnt", "-1");
	    returnMap.put( "resultCode", code );
	    returnMap.put( "resultMsg", resStr);
	      
	    Map< String, Object > resultMap = new HashMap< String, Object >();
	    resultMap.put( "resultInfo", returnMap );
	              
		return (Map<String, Object>) resultMap;
	}
	
	@RequestMapping(value = "/resources/systemDbMgmt.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String systemDbMgmt(ModelMap modelMap) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			logger.info("-> []");
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "getBmscMgmt");
			syslogMap.put("reqUrl", "resources/systemDbMgmt.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (Exception e) {
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDbMgmt");
			syslogMap.put("reqUrl", "resources/systemDbMgmt.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
		}
		Map<String, Object> backupStatus = usersMapper.selectAutoBackupStatus();
		modelMap.addAttribute("autoYN", backupStatus.get("auto_yn"));
		modelMap.addAttribute("backupTime", backupStatus.get("backup_time"));
		return "system/databaseMgmt";
	}
	
	@RequestMapping(value = "/resources/updateAutoBackupYN.do", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String updateAutoBackupYN(@RequestParam String autoYN) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			logger.info("-> []");
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "getBmscMgmt");
			syslogMap.put("reqUrl", "resources/systemDbMgmt.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (Exception e) {
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDbMgmt");
			syslogMap.put("reqUrl", "resources/systemDbMgmt.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
		}
		if(usersMapper.updateAutoBackupYN(autoYN) != 1){
			return "FAIL";
		}
		return "SUCCESS";
	}
	
	@RequestMapping(value = "/resources/updateBackupTime.do", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String updateBackupTime(@RequestParam String backupTime) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {
			logger.info("-> []");
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "getBmscMgmt");
			syslogMap.put("reqUrl", "resources/systemDbMgmt.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (Exception e) {
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDbMgmt");
			syslogMap.put("reqUrl", "resources/systemDbMgmt.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
		}
		if(usersMapper.updateBackupTime(backupTime) != 1){
			return "FAIL";
		}
		return "SUCCESS";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/resources/systemDblist.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String systemDblist(@RequestBody String body) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			String searchYear = (String) requestJson.get("searchYear");
			String searchMonth = (String) requestJson.get("searchMonth");
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			if (sort == null || sort.isEmpty()) sort = null;
			if (order == null || order.isEmpty()) order = null;
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			HashMap<String, Object> searchParam = new HashMap();
			searchParam.put("backupId", "");
			searchParam.put("searchYear", searchYear);
			searchParam.put("searchMonth", searchMonth);
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset+1);
			searchParam.put("end", offset + limit);
			
			List<SystemDatabaseBackup> datas = mapper.getSystemDblist(searchParam);
			SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		   
		    
			JSONArray rows = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				SystemDatabaseBackup data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("rownum", data.getRownum());
				obj.put("backupId", data.getBackupId());
				obj.put("backupFileName", data.getBackupFileName());
				obj.put("backupFilePath", data.getBackupFilePath());
				obj.put("backupCreatedId", data.getBackupCreatedId());
				obj.put("backupCreatedAt", sdfTo.format(data.getBackupCreatedAt()));
				obj.put("backupType", data.getBackupType());
				obj.put("totalCount", data.getTotalCount());
				rows.add(obj);
				
				if( i == 0 ) {
					jsonResult.put("total", data.getTotalCount());
				}
			}
			
			jsonResult.put("rows", rows);
    
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDblist");
			syslogMap.put("reqUrl", "resources/systemDblist.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
		} catch (ParseException e) {
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDblist");
			syslogMap.put("reqUrl", "resources/systemDblist.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
		
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/resources/systemDbRestore.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String systemDbRestore(@RequestParam(value = "backupId", required = true) Integer backupId) {
		logger.info("-> [backupId = {}]", backupId);
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		
		HashMap<String, Object> searchParam = new HashMap();
		searchParam.put("backupId", backupId);
		searchParam.put("searchYear", "");
		searchParam.put("searchMonth", "");
		searchParam.put("sort", null);
		searchParam.put("order", null);
		searchParam.put("start", 1);
		searchParam.put("end", 10);

		String backupFileNam = "";
		List<SystemDatabaseBackup> datas = mapper.getSystemDblist(searchParam);
	    
		JSONArray rows = new JSONArray();
		for(int i = 0; i < datas.size(); i++) {
			SystemDatabaseBackup data = datas.get(i);
			backupFileNam = data.getBackupFileName();
		}
		
		String RETURN_SHELL = "Session_Cnt:";
		JSONObject jsonResult = new JSONObject();

		String exeDatabaseRestore = String.format("%s %s", databaseRestore, backupFileNam);
		try{
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(exeDatabaseRestore);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null) {
				sb.append(line);
			}

            System.out.println("exeDatabaseRestore   >>  " + sb);
            
			//String sessionHostNameCnt = sb.substring(RETURN_SHELL.length());
            String sessionHostNameCnt = sb.toString();
  			jsonResult.put("result", sessionHostNameCnt);
  			
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDbRestore");
			syslogMap.put("reqUrl", "resources/systemDbRestore.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
	    }catch(Exception e){
	    	logger.error("",e);
			jsonResult.put("result", "-1");
			jsonResult.put("reqMsg", e.toString());
			
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDbRestore");
			syslogMap.put("reqUrl", "resources/systemDbRestore.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
	    }
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/resources/systemDbBackup.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String systemDbBackup(@ModelAttribute Users user, HttpServletRequest request, @RequestParam(value = "fileName", required = true) String fileName) {
		logger.info("-> [fileName = {}]", fileName);
		HttpSession session = request.getSession(false);
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
		String newYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)); 
		String newMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)); 
		String newDay = String.valueOf(Calendar.getInstance().get(Calendar.DATE)); 
		String filePath = "";
		if(newMonth.length() > 1){
			filePath = "/dashbd/backup/"+newYear+"/"+newMonth;
		}else{
			filePath = "/dashbd/backup/"+newYear+"/0"+newMonth;
		}
		if(newDay.length() > 1){
			filePath += "/"+newDay;
		}else{
			filePath += "/0"+newDay;
		}
			
		String RETURN_SHELL = "Session_Cnt:";
		JSONObject jsonResult = new JSONObject();

		String exeDatabaseBackup = String.format("%s %s", databaseBackup, fileName);
		try{
  			
  			Users userOfSession = (Users) session.getAttribute("USER");
  			HashMap<String, Object> searchParam = new HashMap();
  			searchParam.put("fileName", fileName);
  			searchParam.put("filePath", filePath);
  			searchParam.put("createdId", userOfSession.getUserId());

  			int rst = mapper.insertSystemDbBackup(searchParam);
  			
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(exeDatabaseBackup);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null) {
				sb.append(line);
			}

            System.out.println("exeDatabaseBackup   >>  " + sb);
            
			//String sessionHostNameCnt = sb.substring(RETURN_SHELL.length());
            String sessionHostNameCnt = sb.toString();
  			jsonResult.put("result", sessionHostNameCnt);
  			
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDbBackup");
			syslogMap.put("reqUrl", "resources/systemDbBackup.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
	    }catch(Exception e){
	    	logger.error("",e);
			jsonResult.put("result", "-1");
			jsonResult.put("reqMsg", e.toString());
			
			syslogMap.put("reqType", "Database Config");
			syslogMap.put("reqSubType", "systemDbBackup");
			syslogMap.put("reqUrl", "resources/systemDbBackup.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
	    }
		
		logger.info("<- [jsonResult = {}]", jsonResult.toString());
		return jsonResult.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/getBroadCastList.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String getBroadCastList(@RequestBody String body) {
		logger.info("-> [body = {}]", body);

		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			

			String searchOperator = (String) requestJson.get("searchOperator");
			String searchBmsc = (String) requestJson.get("searchBmsc");
			String searchSYear = (String) requestJson.get("searchSYear");
			String searchSMonth = (String) requestJson.get("searchSMonth");
			String searchSDay = (String) requestJson.get("searchSDay");
			String searchEYear = (String) requestJson.get("searchEYear");
			String searchEMonth = (String) requestJson.get("searchEMonth");
			String searchEDay = (String) requestJson.get("searchEDay");
			
			String searchSDate = "";
			String searchEDate = "";
			
			if(searchSDay.equals("")){
				if(searchSMonth.equals("")){
					if(searchSYear.equals("")){
						searchSDate = "";
					}else{
						searchSDate = searchSYear+"-01-01";
					}
				}else{
					searchSDate = searchSYear+"-"+searchSMonth+"-01";
				}
			}else{
				searchSDate = searchSYear+"-"+searchSMonth+"-"+searchSDay;
			}
			if(searchEDay.equals("")){
				if(searchEMonth.equals("")){
					if(searchEYear.equals("")){
						searchEDate = "";
					}else{
						searchEDate = searchEYear+"-12-31";
					}
				}else{
					searchEDate = searchEYear+"-"+searchEMonth+"-31";
				}
			}else{
				searchEDate = searchEYear+"-"+searchEMonth+"-"+searchEDay;
			}
			
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			if (sort == null || sort.isEmpty()) sort = null;
			if (order == null || order.isEmpty()) order = null;
			
			ServiceAreaMapper mapper = sqlSession.getMapper(ServiceAreaMapper.class);
			
			HashMap<String, Object> searchParam = new HashMap();
			searchParam.put("searchOperator", searchOperator);
			searchParam.put("searchBmsc", searchBmsc);
			searchParam.put("searchSDate", searchSDate);
			searchParam.put("searchEDate", searchEDate);
			searchParam.put("sort", sort);
			searchParam.put("order", order);
			searchParam.put("start", offset+1);
			searchParam.put("end", offset + limit);
			
			List<SystemBroadCastContents> datas = mapper.getSystemBCContentsList(searchParam);
			SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			JSONArray rows = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				SystemBroadCastContents data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("rownum", data.getRownum());
				obj.put("schId", data.getSchId());
				obj.put("bmscid", data.getBmscid());
				obj.put("bmscName", data.getBmscName());
				obj.put("serviceAreaId", data.getServiceAreaId());
				obj.put("serviceAreaName", data.getServiceAreaName());
				obj.put("contentId", data.getContentId());
				obj.put("contentName", data.getContentName());
				obj.put("operatorId", data.getOperatorId());
				obj.put("operatorName", data.getOperatorName());
				obj.put("serviceCategory", data.getServiceCategory());
				obj.put("fileType", data.getFileType());
				obj.put("fileFormat", data.getFileFormat());
				obj.put("serviceType", data.getServiceType());
				obj.put("startDate", sdfTo.format(data.getStartDate()));
				obj.put("endDate", sdfTo.format(data.getEndDate()));
				obj.put("totalCount", data.getTotalCount());
				rows.add(obj);
				
				if( i == 0 ) {
					jsonResult.put("total", data.getTotalCount());
				}
			}
			
			jsonResult.put("rows", rows);
			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getBroadCastList");
			syslogMap.put("reqUrl", "api/getBroadCastList.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);

		} catch (Exception e) {
			syslogMap.put("reqType", "System Mgmt");
			syslogMap.put("reqSubType", "getBroadCastList");
			syslogMap.put("reqUrl", "api/getBroadCastList.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	/**
	 * CPU, Memory 차트 데이터 조회 메소드
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/getSystemLogData.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public void serviceAreaProccess(@RequestParam HashMap<String, Object> param, HttpServletResponse response) {
		try {
			JSONObject resultMap = new JSONObject();
			JSONArray jsonArray1 = new JSONArray();
			JSONArray jsonArray2 = new JSONArray();
			JSONArray jsonArray3 = new JSONArray();
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			String year = String.valueOf(cal.get(Calendar.YEAR));
			String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			String date = String.valueOf(cal.get(Calendar.DATE));
			
			month = (month.length() == 1)? "0" + month : month; 
			date = (date.length() == 1)? "0" + date : date;
			
			//cpu로그 데이터와 memory 로그 데이터를 가지고 옴
			File file1 = new File("C:/dashbd/system/resources/cpu/" + year + month + date + ".log");
			File file2 = new File("C:/dashbd/system/resources/memory/" + year + month + date + ".log");
			File file3 = new File("C:/dashbd/system/resources/hdd/" + year + month + date + ".log");
			
			List<String> cpuStrList = new ArrayList<String>();
			List<String> memoryStrList = new ArrayList<String>();
			List<String> hddStrList = new ArrayList<String>();
			
			FileReader fileReader = new FileReader(file1);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				cpuStrList.add(line);
			}
			
			
			fileReader = new FileReader(file2);
			bufferedReader = new BufferedReader(fileReader);
			line = "";
			while ((line = bufferedReader.readLine()) != null) {
				memoryStrList.add(line);
			}
			
			
			fileReader = new FileReader(file3);
			bufferedReader = new BufferedReader(fileReader);
			line = "";
			while ((line = bufferedReader.readLine()) != null) {
				hddStrList.add(line);
			}
			
			fileReader.close();
			
			//마지막 cpu사용량을 담을 변수
			String finalCpu="";
			for(int i=((cpuStrList.size()-20 < 0)? 0 : cpuStrList.size()-20); i < cpuStrList.size(); i++) {
				JSONObject cpuObject = new JSONObject();
				
				String tempCpuInfo = cpuStrList.get(i);
				int tempIndex = tempCpuInfo.indexOf("]");
				cpuObject.put(0, tempCpuInfo.substring(tempIndex-8, tempIndex)); 
				cpuObject.put(1, Integer.parseInt(tempCpuInfo.substring(tempIndex+2, tempCpuInfo.indexOf("%"))));
				jsonArray1.add(cpuObject);
				
				if(i == cpuStrList.size() -1) {
					finalCpu = tempCpuInfo.substring(tempIndex+2, tempCpuInfo.indexOf("%"));
				}
			}
			
			//마지막 memory사용량을 담을 변수
			String finalMemory="";
			for(int i=((memoryStrList.size()-20 < 0)? 0 : memoryStrList.size()-20); i < memoryStrList.size(); i++) {
				JSONObject memoryObject = new JSONObject();
				
				String tempMemoryInfo = memoryStrList.get(i);
				int tempIndex = tempMemoryInfo.indexOf("]");
				memoryObject.put(0, tempMemoryInfo.substring(tempIndex-8, tempIndex)); 
				memoryObject.put(1, Integer.parseInt(tempMemoryInfo.substring(tempIndex+2, tempMemoryInfo.indexOf("%"))));
				jsonArray2.add(memoryObject);
				
				if(i == memoryStrList.size() -1) {
					finalMemory = tempMemoryInfo.substring(tempIndex+2, tempMemoryInfo.indexOf("%"));
				}
			}
			
			//Hdd 로그에서 맨 아래 4(Const 전역 변수 값에 따라 달라짐) 줄을 담아줌
			JSONObject hddObject = new JSONObject();
			hddObject.put("1", "Filesystem");
			hddObject.put("2", "1K-blocks");
			hddObject.put("3", "Used");
			hddObject.put("4", "Available");
			hddObject.put("5", "Use%");
			hddObject.put("6", "Mounted on");
			jsonArray3.add(hddObject);
			
			for(int i=((hddStrList.size()-(Const.hddLogLine) < 0)? 0 : hddStrList.size()-(Const.hddLogLine)); i < hddStrList.size()-1; i++) {
				hddObject = new JSONObject();
				String totalStr = hddStrList.get(i);
				
				List<String> tempArr = new ArrayList<String>();
				for(int j=0; j < totalStr.length(); j++) {
					if(j == totalStr.length()-1) break;
					
					char tempStr = totalStr.charAt(j);
					char tempNextStr = totalStr.charAt(j+1);
					
					if(!String.valueOf(tempStr).equals(" ") && String.valueOf(tempNextStr).equals(" ")) {
						tempArr.add(String.valueOf(j+1));
					} else if(String.valueOf(tempStr).equals(" ") && !String.valueOf(tempNextStr).equals(" ")) {
						tempArr.add(String.valueOf(j+1));
					}
				}
				
				hddObject.put("1", totalStr.substring(0, Integer.parseInt(tempArr.get(0))));
				hddObject.put("2", totalStr.substring(Integer.parseInt(tempArr.get(1)), Integer.parseInt(tempArr.get(2))));
				hddObject.put("3", totalStr.substring(Integer.parseInt(tempArr.get(3)), Integer.parseInt(tempArr.get(4))));
				hddObject.put("4", totalStr.substring(Integer.parseInt(tempArr.get(5)), Integer.parseInt(tempArr.get(6))));
				hddObject.put("5", totalStr.substring(Integer.parseInt(tempArr.get(7)), Integer.parseInt(tempArr.get(8))));
				hddObject.put("6", totalStr.substring(Integer.parseInt(tempArr.get(9))));
				
				jsonArray3.add(hddObject);
			}
			
			resultMap.put("cupResultList", jsonArray1);
			resultMap.put("memoryResultList", jsonArray2);
			resultMap.put("hddResultList", jsonArray3);
			resultMap.put("finalCpu", finalCpu);
			resultMap.put("finalMemory", finalMemory); 
			
			System.out.println("test:" + resultMap.toJSONString());
			response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
	        response.getWriter().print(resultMap.toJSONString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
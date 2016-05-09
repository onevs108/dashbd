package com.catenoid.dashbd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.ServiceAreaPermissionAp;
import com.catenoid.dashbd.dao.model.SystemDatabaseBackup;
import com.catenoid.dashbd.dao.model.SystemIncomingLog;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.BmscService;

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
            
			String sessionHostNameCnt = sbHostName.substring(RETURN_SHELL.length());
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
          
			String sessionServerStatuseCnt = sbServerStatus.substring(RETURN_SHELL.length());
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
          
			String sessionDatabaseStatuseCnt = sbDatabaseStatus.substring(RETURN_SHELL.length());
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
          
			String sessionCnt = sb.substring(RETURN_SHELL.length());
			
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
		return "system/databaseMgmt";
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
			String sessionHostNameCnt = sb.substring(RETURN_SHELL.length());
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
			String sessionHostNameCnt = sb.substring(RETURN_SHELL.length());
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
}
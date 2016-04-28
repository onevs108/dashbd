package com.catenoid.dashbd;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.util.ErrorCode;
import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.dao.ContentsImagesMapper;
import com.catenoid.dashbd.dao.ContentsMapper;
import com.catenoid.dashbd.dao.ScheduleContentsMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.ContentsExample;
import com.catenoid.dashbd.dao.model.ContentsExample.Criteria;
import com.catenoid.dashbd.dao.model.ScheduleContents;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class ContentsController {
	
	private static final Logger logger = LoggerFactory.getLogger(ContentsController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	@RequestMapping(value = "api/content.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doContent(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readContents(request);
		}
		else if(request_type.equalsIgnoreCase("get_tot_count")) {
			return getTotalCount(request);
		}
		else if(request_type.equalsIgnoreCase("create")) {
			return creatContents(request);
		}
		else if(request_type.equalsIgnoreCase("update")) {
			return updateContentsByID(request);
		}
		else if(request_type.equalsIgnoreCase("delete")) {
			return deleteContentsByID(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readContents(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		try {			
			ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
			ContentsExample exm = new ContentsExample();
			
			if(valueCheck(request.getParameter("id"))) exm.and().andIdEqualTo(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("title"))) exm.and().andTitleLike("%" + request.getParameter("title") + "%");
			if(valueCheck(request.getParameter("category"))) exm.and().andCategoryEqualTo(request.getParameter("category"));
			if(valueCheck(request.getParameter("director"))) exm.and().andDirectorLike("%" + request.getParameter("director") + "%");
			if(valueCheck(request.getParameter("actor"))) exm.and().andActorsLike("%" + request.getParameter("actor") + "%");
			if(valueCheck(request.getParameter("content_provider"))) exm.and().andContentProviderLike("%" + request.getParameter("content_provider") + "%");
			if(valueCheck(request.getParameter("age_restriction"))) exm.and().andAgeRestrictionEqualTo(Integer.parseInt(request.getParameter("age_restriction")));			
			if(valueCheck(request.getParameter("order_by"))) exm.setOrderByClause(request.getParameter("order_by"));			
			if(valueCheck(request.getParameter("page"))) {
				if(!valueCheck(request.getParameter("limit"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
				exm.setLimitByClause(((Integer.parseInt(request.getParameter("page")) - 1) * Integer.parseInt(request.getParameter("limit"))) + ", " + request.getParameter("limit"));
			}
			else if(valueCheck(request.getParameter("limit"))) {
				if(Integer.parseInt(request.getParameter("limit")) == 0) {
					exm.setLimitByClause(null);
				}
				else {
					exm.setLimitByClause(request.getParameter("limit"));			
				}
			}
			
			List<Criteria> list = exm.getOredCriteria();
			if(!valueCheck(request.getParameter("page")) && list.size() == 0) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			List<Contents> datas = mapper.selectByExample(exm);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			JSONObject root = new JSONObject();
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					Contents data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getId());
					obj.put("type", data.getType());
					obj.put("title", data.getTitle());
					obj.put("category", data.getCategory());
					obj.put("director", data.getDirector());
					obj.put("actors", data.getActors());
					obj.put("content_provider", data.getContentProvider());
					obj.put("file_format", data.getFileFormat());
					obj.put("age_restriction", data.getAgeRestriction());
					obj.put("description", data.getDescription());
					obj.put("url", data.getUrl());
					obj.put("duration", data.getDuration());
					obj.put("bitrate", data.getBitrate());
					obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
					array.add(obj);
				}
				root.put("result", array);
			}
			else {
				Contents data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getId());
				obj.put("type", data.getType());
				obj.put("title", data.getTitle());
				obj.put("category", data.getCategory());
				obj.put("director", data.getDirector());
				obj.put("actors", data.getActors());
				obj.put("content_provider", data.getContentProvider());
				obj.put("file_format", data.getFileFormat());
				obj.put("age_restriction", data.getAgeRestriction());
				obj.put("description", data.getDescription());
				obj.put("url", data.getUrl());
				obj.put("duration", data.getDuration());
				obj.put("bitrate", data.getBitrate());
				obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
				root.put("result", obj);
			}
			
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "readContents");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "readContents");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> getTotalCount(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject root = new JSONObject();
		try {
			ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
			ContentsExample exm = new ContentsExample();
			if(valueCheck(request.getParameter("title"))) exm.and().andTitleLike("%" + request.getParameter("title") + "%");
			if(valueCheck(request.getParameter("category"))) exm.and().andCategoryEqualTo(request.getParameter("category"));
			if(valueCheck(request.getParameter("director"))) exm.and().andDirectorLike("%" + request.getParameter("director") + "%");
			if(valueCheck(request.getParameter("actor"))) exm.and().andActorsLike("%" + request.getParameter("actor") + "%");
			if(valueCheck(request.getParameter("content_provider"))) exm.and().andContentProviderLike("%" + request.getParameter("content_provider") + "%");
			if(valueCheck(request.getParameter("age_restriction"))) exm.and().andAgeRestrictionEqualTo(Integer.parseInt(request.getParameter("age_restriction")));	
			int count = mapper.countByExample(exm);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("count", count);
			root.put("result", obj);

			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "getTotalCount");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK); 
		}
		catch (Exception e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "getTotalCount");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> creatContents(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject root = new JSONObject();
		try {	
			if(!valueCheck(request.getParameter("type"))) {
				logger.info("Content type is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			else if(!valueCheck(request.getParameter("title"))) {
				logger.info("Titls is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			else if(!valueCheck(request.getParameter("age_restriction"))) {
				logger.info("Age Restriction is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			else if(!valueCheck(request.getParameter("duration"))) {
				logger.info("Duration is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			else if(!valueCheck(request.getParameter("bitrate"))) {
				logger.info("Bitrate is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
			Contents record = new Contents();
			record.setType(request.getParameter("type"));
			record.setTitle(request.getParameter("title"));
			record.setAgeRestriction(Integer.parseInt(request.getParameter("age_restriction")));
			record.setDuration(Integer.parseInt(request.getParameter("duration")));
			record.setBitrate(Integer.parseInt(request.getParameter("bitrate")));	
			if(valueCheck(request.getParameter("category"))) record.setCategory(request.getParameter("category"));
			if(valueCheck(request.getParameter("director"))) record.setDirector(request.getParameter("director"));
			if(valueCheck(request.getParameter("actors"))) record.setActors(request.getParameter("actors"));
			if(valueCheck(request.getParameter("content_provider"))) record.setContentProvider(request.getParameter("content_provider"));
			if(valueCheck(request.getParameter("file_format"))) record.setFileFormat(request.getParameter("file_format"));
			if(valueCheck(request.getParameter("description"))) record.setDescription(request.getParameter("description"));
			if(valueCheck(request.getParameter("url"))) record.setUrl(request.getParameter("url"));		
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("id", record.getId());
			root.put("result", obj);

			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "creatContents");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "creatContents");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "creatContents");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			root.put("code", ErrorCodes.DATA_DUPLICATION.getCode());
			root.put("message", ErrorCodes.DATA_DUPLICATION.getMsg());
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> updateContentsByID(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject root = new JSONObject();
		try {
			if(request.getParameter("id") == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
			Contents record = new Contents();
			record.setId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("type"))) record.setTitle(request.getParameter("type"));
			if(valueCheck(request.getParameter("title"))) record.setTitle(request.getParameter("title"));			
			if(valueCheck(request.getParameter("category"))) record.setCategory(request.getParameter("category"));
			if(valueCheck(request.getParameter("director"))) record.setDirector(request.getParameter("director"));
			if(valueCheck(request.getParameter("actors"))) record.setActors(request.getParameter("actors"));
			if(valueCheck(request.getParameter("content_provider"))) record.setContentProvider(request.getParameter("content_provider"));
			if(valueCheck(request.getParameter("file_format"))) record.setFileFormat(request.getParameter("file_format"));
			if(valueCheck(request.getParameter("age_restriction"))) record.setAgeRestriction(Integer.parseInt(request.getParameter("age_restriction")));
			if(valueCheck(request.getParameter("description"))) record.setDescription(request.getParameter("description"));
			if(valueCheck(request.getParameter("url"))) record.setUrl(request.getParameter("url"));
			if(valueCheck(request.getParameter("duration"))) record.setDuration(Integer.parseInt(request.getParameter("duration")));
			if(valueCheck(request.getParameter("bitrate"))) record.setBitrate(Integer.parseInt(request.getParameter("bitrate")));
			
			int res = mapper.updateByPrimaryKeySelective(record);
			logger.info("UPDATE RESULT: " + res);
			if(res == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "updateContentsByID");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "updateContentsByID");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "updateContentsByID");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			root.put("code", ErrorCodes.DATA_DUPLICATION.getCode());
			root.put("message", ErrorCodes.DATA_DUPLICATION.getMsg());
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "updateContentsByID");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> deleteContentsByID(HttpServletRequest request) {
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		Map<String, Object> syslogMap = new HashMap<String, Object>();
		JSONObject root = new JSONObject();
		try {
			if(request.getParameter("id") == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ScheduleContentsMapper scMapper = sqlSession.getMapper(ScheduleContentsMapper.class);
			List<ScheduleContents> scontents = scMapper.selectScheduleContentsByContentId(Integer.parseInt(request.getParameter("id")));
			if(scontents != null && scontents.size() > 0) {
				root.put("code", ErrorCodes.DEL_DATA_CONSTRAINT.getCode());
				root.put("message", ErrorCodes.DEL_DATA_CONSTRAINT.getMsg());
				if(scontents.size() == 1) {
					ScheduleContents scontent = scontents.get(0);
					JSONObject obj = new JSONObject();
					obj.put("schedule_id", scontent.getScheduleId());
					obj.put("content_id", scontent.getContentId());
					obj.put("start_time", getFormatDateTime(scontent.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					root.put("result", obj);
				}
				else {
					JSONArray array = new JSONArray();
					for(int i = 0; i < scontents.size(); i++) {
						ScheduleContents scontent = scontents.get(i);
						JSONObject obj = new JSONObject();
						obj.put("schedule_id", scontent.getScheduleId());
						obj.put("content_id", scontent.getContentId());
						obj.put("start_time", getFormatDateTime(scontent.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
						array.add(obj);
					}
					root.put("result", array);
				}
				return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
			}
						
			ContentsImagesMapper ciMapper = sqlSession.getMapper(ContentsImagesMapper.class);
			int res = ciMapper.deleteByForeignKey(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE RESULT: " + res);
			
			/// 파일 삭제
			String storePath = null, rootPath = null;
			logger.info("OS: " + System.getProperty("os.name"));
			if(System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) {
				rootPath = "E:\\jee\\workspace2\\dashbd\\src\\main\\webapp\\";
            	storePath = String.format("%s/%d/", fileUploadPath, Integer.parseInt(request.getParameter("id")));
			}
			else {
				HttpSession session  = request.getSession();
            	rootPath = session.getServletContext().getRealPath("/");	
            	storePath = String.format("%s/%d/", fileUploadPath, Integer.parseInt(request.getParameter("id")));
			}
			boolean delRes = deleteDirectory(rootPath + storePath);
			logger.info("FILE DELETE RESULT: " + (delRes == true ? "success" : "failed"));
			
			ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
			res = mapper.deleteByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);

			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "deleteContentsByID");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "SUCCESS");
			syslogMap.put("reqMsg", "");
			usersMapper.insertSystemAjaxLog(syslogMap);
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "deleteContentsByID");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			syslogMap.put("reqType", "Contents Mgmt");
			syslogMap.put("reqSubType", "deleteContentsByID");
			syslogMap.put("reqUrl", "api/content.do");
			syslogMap.put("reqCode", "Fail");
			syslogMap.put("reqMsg", e.toString());
			usersMapper.insertSystemAjaxLog(syslogMap);
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public static Map<String, Object> parameterToMap(HttpServletRequest request) {
		Map<String, Object> prmtMap = new HashMap<String, Object>();
		Enumeration<?> e = request.getParameterNames();
		String prmtNm = null;
		String[] prmtValues = null;
		while(e.hasMoreElements()) {
			prmtNm = (String) e.nextElement();
			prmtValues = request.getParameterValues(prmtNm);
			try {
				if(prmtValues.length > 1) {
					for(int i = 0; i < prmtValues.length; i++) {
						prmtValues[i] = URLDecoder.decode((prmtValues[i].replaceAll("\\%",  "%25")).replaceAll("\\+", "%2B"), "UTF-8");
						if("".equals(prmtValues[i])) {
							prmtValues[i] = null;
						}
					}
					prmtMap.put(prmtNm,  prmtValues);
				} else {
					if("".equals(prmtValues[0])) {
						prmtMap.put(prmtNm, null);
					} else {
						prmtMap.put(prmtNm, URLDecoder.decode((prmtValues[0].replaceAll("\\%", "%25")).replaceAll("\\+", "%2B"), "UTF-8"));
					}					
				}
			}
			catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
		
		return prmtMap;
	}
	
	private boolean deleteDirectory(String path) {
        try {
            File dir = new File(path);
            if(dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                for(File file : files) {
                    if(file.isFile()) {
                        file.delete();
                    }
                    else if(file.isDirectory()) {
                        deleteDirectory(file.getPath());
                    }
                }
                dir.delete();
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
	
	private String getFormatDateTime(Date date, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	private boolean valueCheck(String parameter) {
		// TODO Auto-generated method stub
		if(parameter == null || parameter.trim().equals("")) return false;
		return true;
	}
}

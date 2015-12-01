package com.catenoid.dashbd;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.ServiceScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.dao.ContentsMapper;
import com.catenoid.dashbd.dao.ScheduleContentsMapper;
import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.Schedule;
import com.catenoid.dashbd.dao.model.ScheduleContents;
import com.catenoid.dashbd.dao.model.ScheduleContentsExample;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.dao.model.ServiceSchedule;
import com.catenoid.dashbd.dao.model.ServiceScheduleExample;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class ScheduleController {
	private static final int FB_GUARD_SECOND = 30;
	private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
	
	@Resource
	private Environment env;
	
	@Autowired
	private SqlSession sqlSession;	
	
	@RequestMapping(value = "api/schedule.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doSchedule(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readSchedule(request);			
		}
		else if(request_type.equalsIgnoreCase("read_contents")) {
			if(!valueCheck(request.getParameter("service_area_id"))) return readScheduleContents(request);
			else readScheduleContentsByServiceAreaId(request);
		}
		else if(request_type.equalsIgnoreCase("create")) {
			return creatSchedule(request);
		}
		else if(request_type.equalsIgnoreCase("update")) {
			return updateScheduleByID(request);
		}
		else if(request_type.equalsIgnoreCase("delete")) {
			return deleteScheduleByID(request);
		}
		else if(request_type.equalsIgnoreCase("add_contents")) {
			return addScheduleAtContents(request);
		}
		else if(request_type.equalsIgnoreCase("change_contents")) {
			return changeScheduleAtContents(request);
		}
		else if(request_type.equalsIgnoreCase("remove_contents")) {
			return removeScheduleAtContents(request);
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readSchedule(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);						
			Schedule data = mapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(data == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
						
			root.put("code", 1);
			root.put("message", null);			
			
			JSONObject obj = new JSONObject();
			obj.put("id", data.getId());
			obj.put("name", data.getName());
			obj.put("start_date", getFormatDateTime(data.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("end_date", getFormatDateTime(data.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("created_at", getFormatDateTime(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			obj.put("updated_at", getFormatDateTime(data.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
			root.put("result", obj);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> readScheduleContents(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ScheduleContentsMapper mapper = sqlSession.getMapper(ScheduleContentsMapper.class);		
			ScheduleContentsExample exm = new ScheduleContentsExample();
			exm.and().andScheduleIdEqualTo(Integer.parseInt(request.getParameter("id")));
			List<ScheduleContents> datas = mapper.selectScheduleContents(exm);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					ScheduleContents data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getScheduleId());
					obj.put("content_id", data.getContentId());
					obj.put("cancelled", data.getCancelled());
					obj.put("start_time", getFormatDateTime(data.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("end_time", getFormatDateTime(data.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("content_type", data.getContentType());
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
				ScheduleContents data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getScheduleId());
				obj.put("content_id", data.getContentId());
				obj.put("cancelled", data.getCancelled());
				obj.put("start_time", getFormatDateTime(data.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("end_time", getFormatDateTime(data.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("content_type", data.getContentType());
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
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> readScheduleContentsByServiceAreaId(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("from_datetime"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("to_datetime"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ScheduleContentsMapper mapper = sqlSession.getMapper(ScheduleContentsMapper.class);
			ScheduleContents record = new ScheduleContents();
			record.setServiceAreaId(Integer.parseInt(request.getParameter("service_area_id")));
			record.setFromTime(getFormatDateTime(request.getParameter("from_datetime"), "yyyy-MM-dd HH:mm:ss"));
			record.setToTime(getFormatDateTime(request.getParameter("to_datetime"), "yyyy-MM-dd HH:mm:ss"));
			List<ScheduleContents> datas = mapper.selectScheduleContentsByServiceAreaId(record);
			if(datas == null || datas.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			if(datas.size() > 1) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < datas.size(); i++) {
					ScheduleContents data = datas.get(i);
					JSONObject obj = new JSONObject();
					obj.put("id", data.getScheduleId());
					obj.put("content_id", data.getContentId());
					obj.put("cancelled", data.getCancelled());
					obj.put("start_time", getFormatDateTime(data.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("end_time", getFormatDateTime(data.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
					obj.put("content_type", data.getContentType());
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
				ScheduleContents data = datas.get(0);
				JSONObject obj = new JSONObject();
				obj.put("id", data.getScheduleId());
				obj.put("content_id", data.getContentId());
				obj.put("cancelled", data.getCancelled());
				obj.put("start_time", getFormatDateTime(data.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("end_time", getFormatDateTime(data.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
				obj.put("content_type", data.getContentType());
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
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> creatSchedule(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {			
			if(!valueCheck(request.getParameter("start_date"))) {
				logger.info("Schedule start date is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			else if(!valueCheck(request.getParameter("end_date"))) {
				logger.info("Schedule end date is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}			
			
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			Schedule record = new Schedule();
			if(valueCheck(request.getParameter("id"))) record.setId(Integer.parseInt(request.getParameter("id")));
			record.setStartDate(getFormatDateTime(request.getParameter("start_date"), "yyyy-MM-dd HH:mm:ss"));
			record.setStartDate(getFormatDateTime(request.getParameter("end_date"), "yyyy-MM-dd HH:mm:ss"));
			if(valueCheck(request.getParameter("name"))) record.setName(request.getParameter("name"));
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			if(valueCheck(request.getParameter("id")))  obj.put("id", Integer.parseInt(request.getParameter("id")));
			else obj.put("id", record.getId());
			root.put("result", obj);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
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
	
	private ResponseEntity<String> updateScheduleByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			Schedule record = new Schedule();
			record.setId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("start_date"))) record.setStartDate(getFormatDateTime(request.getParameter("schedule_date"), "yyyy-MM-dd HH:mm:ss"));
			if(valueCheck(request.getParameter("end_date"))) record.setEndDate(getFormatDateTime(request.getParameter("end_date"), "yyyy-MM-dd HH:mm:ss"));
			if(valueCheck(request.getParameter("name"))) record.setName(request.getParameter("name"));
			
			int res = mapper.updateByPrimaryKeySelective(record);
			logger.info("UPDATE RESULT: " + res);
			if(res == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
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
	
	private ResponseEntity<String> deleteScheduleByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			/// TODO: 다른 곳에도 해당 로직이 들어가야 할것 같은데...
			/// TODO: 추가 검토 필요
			/// 서비스와 mapping되어 있는지 check후 mapping되어 있으면 에러 리턴
			ServiceScheduleMapper s2Mapper = sqlSession.getMapper(ServiceScheduleMapper.class);
			ServiceScheduleExample exm = new ServiceScheduleExample();
			exm.createCriteria().andScheduleIdEqualTo(Integer.parseInt(request.getParameter("id")));
			List<ServiceSchedule> datas = s2Mapper.selectByExample(exm);
			if(datas != null && datas.size() > 0) {
				root.put("code", ErrorCodes.DEL_DATA_CONSTRAINT.getCode());
				root.put("message", ErrorCodes.DEL_DATA_CONSTRAINT.getMsg());
				JSONObject obj = new JSONObject();
				obj.put("service_id", datas.get(0).getServiceId());
				root.put("result", obj);
				return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
			}
			
			ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
			int res = mapper.deleteByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE RESULT: " + res);			
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> addScheduleAtContents(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("content_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("start_time"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("end_time"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			/// 추가 할 컨텐츠 정보 select
			ContentsMapper conMapper = sqlSession.getMapper(ContentsMapper.class);
			Contents conData = conMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("content_id")));
			/// 추가 할 컨텐츠의 bitrate 값 저장
			int total_bitrate = conData.getBitrate();
			/// 추가할 컨텐츠의 start_time ~ end_time 계산
			Date fromDate = new Date(getFormatDateTime(request.getParameter("start_time"), "yyyy-MM-dd HH:mm:ss").getTime() - (FB_GUARD_SECOND * 1000));
			Date toDate = new Date(getFormatDateTime(request.getParameter("start_time"), "yyyy-MM-dd HH:mm:ss").getTime() + ((conData.getDuration() + FB_GUARD_SECOND) * 1000));			
			logger.info("from TIME: " + getFormatDateTime(fromDate, "yyyy-MM-dd HH:mm:ss") + ", to TIME: " + getFormatDateTime(toDate, "yyyy-MM-dd HH:mm:ss"));
			
			/// 현재 schedule의 data중 start_time ~ end_time 사이의 컨텐츠 정보 조회
			ScheduleContentsMapper mapper = sqlSession.getMapper(ScheduleContentsMapper.class);
			ScheduleContents record = new ScheduleContents();
			//record.setScheduleId(Integer.parseInt(request.getParameter("id")));
			record.setServiceAreaId(Integer.parseInt(request.getParameter("service_area_id")));
			record.setFromTime(fromDate);
			record.setToTime(toDate);
			List<ScheduleContents> datas = mapper.selectBandwidthCheckScheduleContents(record);
			if(datas != null && datas.size() > 0) {
				for(int i = 0; i < datas.size(); i++) {
					ScheduleContents data = datas.get(i);
					/// 해당 시간대에 등록되어 있는 컨텐츠들의 bitrate값을 sum
					total_bitrate += data.getBitrate();
				}
				ServiceAreaMapper saMapper = sqlSession.getMapper(ServiceAreaMapper.class);
				ServiceArea sa = saMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("service_area_id")));
				if(sa == null) {
					logger.info("Service Area is not exist!!!");
					return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
				}
				/// 서비스 에어리어의 bandwidth보다 특정시간대에 등록되어 있는 컨텐츠들의 bitrate 값의 합이 크면
				/// 에러를 리턴한다.(단, 하나의 schedule는 한개의 service_area와 mapping되어 있다는 가정하에...)
				else if(total_bitrate > sa.getBandwidth()) {
					root.put("code", ErrorCodes.OVER_BANDWIDTH.getCode());
					root.put("code", ErrorCodes.OVER_BANDWIDTH.getMsg());
					return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
				}
			}
			
			record = new ScheduleContents();
			record.setScheduleId(Integer.parseInt(request.getParameter("id")));
			record.setContentId(Integer.parseInt(request.getParameter("content_id")));
			if(valueCheck(request.getParameter("cancelled"))) record.setCancelled(Integer.parseInt(request.getParameter("cancelled")));
			record.setStartTime(getFormatDateTime(request.getParameter("start_time"), "yyyy-MM-dd HH:mm:ss"));
			record.setEndTime(getFormatDateTime(request.getParameter("end_time"), "yyyy-MM-dd HH:mm:ss"));
			int res = mapper.insertSelective(record);
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (DuplicateKeyException e) {
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
	
	private ResponseEntity<String> changeScheduleAtContents(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("old_content_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("new_content_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ScheduleContentsMapper mapper = sqlSession.getMapper(ScheduleContentsMapper.class);
			ScheduleContents record = new ScheduleContents();
			ScheduleContentsExample exm = new ScheduleContentsExample();
			
			record.setContentId(Integer.parseInt(request.getParameter("new_content_id")));
			if(valueCheck(request.getParameter("cancelled"))) record.setCancelled(Integer.parseInt(request.getParameter("cancelled")));
			if(valueCheck(request.getParameter("changed"))) record.setChanged(Integer.parseInt(request.getParameter("changed")));
			if(valueCheck(request.getParameter("start_time"))) record.setStartTime(getFormatDateTime(request.getParameter("start_time"), "yyyy-MM-dd HH:mm:ss"));
			if(valueCheck(request.getParameter("end_time"))) record.setEndTime(getFormatDateTime(request.getParameter("end_time"), "yyyy-MM-dd HH:mm:ss"));
			exm.and().andScheduleIdEqualTo(Integer.parseInt(request.getParameter("id")));
			exm.and().andScheduleIdEqualTo(Integer.parseInt(request.getParameter("old_content_id")));
			int res = mapper.updateByExampleSelective(record, exm);
			logger.info("UPDATE RESULT: " + res);
			
			if(res == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> removeScheduleAtContents(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("content_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("start_time"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ScheduleContentsMapper mapper = sqlSession.getMapper(ScheduleContentsMapper.class);
			ScheduleContentsExample exm = new ScheduleContentsExample();
			exm.createCriteria().andScheduleIdEqualTo(Integer.parseInt(request.getParameter("id")));
			exm.createCriteria().andContentIdEqualTo(Integer.parseInt(request.getParameter("content_id")));
			exm.createCriteria().andStartTimeEqualTo(getFormatDateTime(request.getParameter("start_time"), "yyyy-MM-dd HH:mm:ss"));
			int res = mapper.deleteByExample(exm);
			logger.info("DELETE RESULT: " + res);			
			
			root.put("code", 1);
			root.put("message", null);
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
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
	
	private String getFormatDateTime(Date date, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	private Date getFormatDateTime(String date, String format) throws ParseException{
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		try {
			return sdFormat.parse(date);
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	private boolean valueCheck(String parameter) {
		// TODO Auto-generated method stub
		if(parameter == null || parameter.trim().equals("")) return false;
		return true;
	}
}

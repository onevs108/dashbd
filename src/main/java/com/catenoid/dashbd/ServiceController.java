package com.catenoid.dashbd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.catenoid.dashbd.dao.AdPostFileRepairMapper;
import com.catenoid.dashbd.dao.AdReceptionReportMapper;
import com.catenoid.dashbd.dao.ScheduleContentsMapper;
import com.catenoid.dashbd.dao.ServiceMapper;
import com.catenoid.dashbd.dao.ServiceNamesMapper;
import com.catenoid.dashbd.dao.ServiceScheduleMapper;
import com.catenoid.dashbd.dao.ServiceServiceAreaMapper;
import com.catenoid.dashbd.dao.ServicesMapper;
import com.catenoid.dashbd.dao.TransReportMapper;
import com.catenoid.dashbd.dao.TransferConfigMapper;
import com.catenoid.dashbd.dao.model.AdPostFileRepair;
import com.catenoid.dashbd.dao.model.AdReceptionReport;
import com.catenoid.dashbd.dao.model.ScheduleContents;
import com.catenoid.dashbd.dao.model.ScheduleContentsExample;
import com.catenoid.dashbd.dao.model.Service;
import com.catenoid.dashbd.dao.model.ServiceExample;
import com.catenoid.dashbd.dao.model.ServiceNames;
import com.catenoid.dashbd.dao.model.ServiceSchedule;
import com.catenoid.dashbd.dao.model.ServiceScheduleExample;
import com.catenoid.dashbd.dao.model.ServiceServiceArea;
import com.catenoid.dashbd.dao.model.ServiceServiceAreaExample;
import com.catenoid.dashbd.dao.model.Services;
import com.catenoid.dashbd.dao.model.ServicesExample;
import com.catenoid.dashbd.dao.model.TransReport;
import com.catenoid.dashbd.dao.model.TransReportExample;
import com.catenoid.dashbd.dao.model.TransferConfig;
import com.catenoid.dashbd.util.Base64Coder;
import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.util.MyException;

import catenoid.net.msg.XmlPara;
import catenoid.net.msg.XmlParaSet;
import catenoid.net.msg.XmlTlv;
import catenoid.net.tools.XmlFormer;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class ServiceController {	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	
	private static final int SERVICE_TYPE_FILE_DOWNLOAD 			= 1;
	private static final int SERVICE_TYPE_STREAMING					= 3;
	private static final int SERVICE_TYPE_CAROUSEL_MULTIPLEFILES	= 5;
	private static final int SERVICE_TYPE_CAROUSEL_SINGLEFILE		= 7;
	
	@Resource
	private Environment env;
	
	@Value("#{config['b2.interface.url']}")
	private String b2InterfefaceURL;
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "api/service.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> doService(HttpServletRequest request) {
		String request_type = request.getParameter("request_type"); 
		logger.info("request_type: " + (request_type == null ? "null" : request_type));
		if(request_type == null || request_type.trim().equals("")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		if(request_type.equalsIgnoreCase("read")) {
			return readService(request);
		}
		else if(request_type.equalsIgnoreCase("create")) {
			return creatService(request);
		}
		else if(request_type.equalsIgnoreCase("update")) {
			return updateServiceByID(request);
		}
		else if(request_type.equalsIgnoreCase("delete")) {
			return deleteServiceByID(request);
		}
		else if(request_type.equalsIgnoreCase("add_service_name")) {
			return addNameAtService(request);
		}
		else if(request_type.equalsIgnoreCase("add_transfer_config")) {
			return addTransferConfigAtService(request);
		}
		else if(request_type.equalsIgnoreCase("add_service_area")) {
			return addServiceAreaAtService(request);
		}
		else if(request_type.equalsIgnoreCase("add_service_schedule")) {
			return addServiceScheduleAtService(request);
		}
		else if(request_type.equalsIgnoreCase("add_ad_post_file_repair")) {
			return addAdPostFileRepairAtService(request);
		}
		else if(request_type.equalsIgnoreCase("add_ad_reception_report")) {
			return addAdReceptionReportAtService(request);
		}
		else if(request_type.equalsIgnoreCase("change_service_name")) {
			return changeServiceName(request);
		}
		else if(request_type.equalsIgnoreCase("change_transfer_config")) {
			return changeTransferConfig(request);
		}
		else if(request_type.equalsIgnoreCase("change_ad_post_file_repair")) {
			return changeAdPostFileRepair(request);
		}
		else if(request_type.equalsIgnoreCase("change_ad_reception_report")) {
			return changeAdReceptionReport(request);
		}
		else if(request_type.equalsIgnoreCase("remove_service_name")) {
			return removeServiceName(request);
		}
		else if(request_type.equalsIgnoreCase("remove_transfer_config")) {
			return removeTransferConfig(request);		
		}
		else if(request_type.equalsIgnoreCase("remove_service_schedule")) {
			return removeServiceSchedule(request);
		}
		else if(request_type.equalsIgnoreCase("remove_service_area")) {
			return removeServiceArea(request);
		}
		else if(request_type.equalsIgnoreCase("remove_ad_post_file_repair")) {
			return removeAdPostFileRepair(request);
		}
		else if(request_type.equalsIgnoreCase("remove_ad_reception_report")) {
			return removeAdReceptionReport(request);
		}
		else if(request_type.equalsIgnoreCase("send_create_service")) {
			return sendCreateService(request);
		}
		else if(request_type.equalsIgnoreCase("send_update_service")) {
			return sendUpdateService(request);
		}
		/// DELETE는 사용 안함.(ABORT로 대체)
//		else if(request_type.equalsIgnoreCase("send_delete_service")) {
//			return sendAbortService(request, "SERVICE.DELETE");
//		}
		else if(request_type.equalsIgnoreCase("send_abort_service")) {
			return sendAbortService(request, "SERVICE.ABORT");
		}
		else if(request_type.equalsIgnoreCase("retrieve_service")) {
			return retrieveService(request);
		}		
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> readService(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);									
			Service data = mapper.selectTypeTransferConfig(Integer.parseInt(request.getParameter("id")));
			if(data == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
						
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			obj.put("id", data.getId());
			obj.put("service_type_id", data.getServiceTypeId());
			obj.put("service_identifier", data.getServiceStrId());
			obj.put("service_class", data.getServiceClass());
			obj.put("retrieve_interval", data.getRetrieveInterval());
			
			/// Service Names(0 ~ N)
			ServiceNamesMapper namesMapper = sqlSession.getMapper(ServiceNamesMapper.class);
			List<ServiceNames> names = namesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(names != null && names.size() > 0) {
				JSONArray array = new JSONArray();
				for(int i = 0; i < names.size(); i++) {
					ServiceNames name = names.get(i);
					JSONObject nobj = new JSONObject();
					nobj.put("name", name.getName());
					nobj.put("name_lang", name.getNameLang());
					nobj.put("language", name.getServiceLanguage());
					array.add(nobj);
				}
				obj.put("service_names", array);
			}
			
			TransferConfigMapper trMapper = sqlSession.getMapper(TransferConfigMapper.class);
			TransferConfig config = trMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));			
			/// TransferConfig(1)
			JSONObject tobj = new JSONObject();
			{
				/// QOS
				JSONObject qos = new JSONObject();
				qos.put("gbr", config.getQosGbr());
				qos.put("qci", config.getQosQci());			
				{
					/// QOS-ARP
					JSONObject arp = new JSONObject();
					arp.put("level", config.getArpLevel());
					arp.put("pre_emption_capability", config.getArpPreEmptionCapability());
					arp.put("pre_emption_vulnerability", config.getArpPreEmptionVulnerability());
					qos.put("arp", arp);
				}
				tobj.put("qos", qos);
				
				/// FEC
				JSONObject fec = new JSONObject();
				fec.put("type", config.getFecType());
				fec.put("ratio", config.getFecRatio());
								
				tobj.put("fec", fec);
				tobj.put("segment_avaliable_offset", config.getSegmentAvailableOffset());
			}
			
			/// Service Area(1 ~ N)
			ServiceServiceAreaMapper saMapper = sqlSession.getMapper(ServiceServiceAreaMapper.class);
			List<ServiceServiceArea> areas = saMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(areas == null || areas.size() == 0) {
				logger.error("Not found ServiceArea of Service(" + request.getParameter("id") + ")!!!");
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
			
			JSONArray array = new JSONArray();
			for(int i = 0; i < areas.size(); i++) {
				ServiceServiceArea area = areas.get(i);
				JSONObject areaid = new JSONObject();
				areaid.put("service_area_id", area.getServiceAreaId());
				array.add(areaid);
			}			
			obj.put("service_area_ids", array);
			
			/// Schedules(1 ~ N)
			ServiceScheduleMapper ssMapper = sqlSession.getMapper(ServiceScheduleMapper.class);			
			List<ServiceSchedule> schedules = ssMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(schedules == null || schedules.size() == 0) {
				logger.error("Not found Schedule of Service(" + request.getParameter("id") + ")!!!");
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
			
			array = new JSONArray();
			for(int i = 0; i < schedules.size(); i++) {
				ServiceSchedule schedule = schedules.get(i);
				JSONObject sobj = new JSONObject();
				sobj.put("id", schedule.getScheduleId());
				sobj.put("start_date", getFormatDateTime(schedule.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
				sobj.put("end_date", getFormatDateTime(schedule.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
				ScheduleContentsMapper scMapper = sqlSession.getMapper(ScheduleContentsMapper.class);				
				List<ScheduleContents> contents = scMapper.selectScheduleContentsByScheduleId(schedule.getScheduleId());
				if(contents == null || contents.size() == 0) {
					logger.error("Not found Contents of Schedule(" + schedule.getScheduleId() + ")!!!");
					return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
				}
				
				/// contents(1 ~ N)
				JSONArray cArray = new JSONArray();
				for(int j = 0; j < contents.size(); j++) {
					ScheduleContents content = contents.get(j);
					JSONObject cobj = new JSONObject();
					cobj.put("id", content.getContentId());
					cobj.put("url", content.getUrl());
					cobj.put("start_time", getFormatDateTime(content.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					cobj.put("end_time", getFormatDateTime(new Date(content.getStartTime().getTime() + (content.getDuration() * 1000)), "yyyy-MM-dd HH:mm:ss"));
					cArray.add(cobj);
				}
				sobj.put("contents", cArray);
				array.add(sobj);
			}
			obj.put("schedules", array);
			
			/// Associated Delivery
			JSONObject adobj = new JSONObject();
			AdPostFileRepairMapper reMapper = sqlSession.getMapper(AdPostFileRepairMapper.class);
			AdPostFileRepair repair = reMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(repair != null) {
				JSONObject robj = new JSONObject();
				robj.put("offset_time", repair.getOffsetTime());
				robj.put("random_time", repair.getRandomTime());
				adobj.put("post_file_repair", robj);
			}
			AdReceptionReportMapper rtMapper = sqlSession.getMapper(AdReceptionReportMapper.class);
			AdReceptionReport report = rtMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(report != null) {
				JSONObject robj = new JSONObject();
				robj.put("report_type", report.getReportType());
				robj.put("sample_percentage", report.getSamplePercentage());
				robj.put("offset_time", report.getOffsetTime());
				robj.put("random_time", report.getRandomTime());
				adobj.put("reception_report", robj);
			}
			if(adobj.size() > 0) obj.put("associated_delivery", adobj);
			
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
	
	private ResponseEntity<String> creatService(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {			
			if(!valueCheck(request.getParameter("service_type_id"))) {
				logger.info("Service type id is NULL!!!");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			else if(!valueCheck(request.getParameter("service_identifier"))) {
				request.setAttribute("service_identifier", getServiceIdentifier(Integer.parseInt(request.getParameter("service_type_id"))));
			}
			else {
				request.setAttribute("service_identifier", request.getParameter("service_identifier"));
			}
			
			ServicesMapper mapper = sqlSession.getMapper(ServicesMapper.class);
			Services record = new Services();
			int res = mapper.insertNoValues(record);
			logger.info("INSERT services RESULT: " + res);
			int servicesId = record.getId();
			
			ServiceMapper service = sqlSession.getMapper(ServiceMapper.class);
			Service servicer = new Service();
			if(valueCheck(request.getParameter("id"))) servicer.setId(Integer.parseInt(request.getParameter("id")));
			servicer.setServicesId(servicesId);
			servicer.setServiceTypeId(Integer.parseInt(request.getParameter("service_type_id")));
			servicer.setServiceStrId((String) request.getAttribute("service_identifier"));
			if(valueCheck(request.getParameter("service_class"))) servicer.setServiceClass(request.getParameter("service_class"));
			else servicer.setServiceClass(getServiceClass(Integer.parseInt(request.getParameter("service_type_id"))));
			if(valueCheck(request.getParameter("retrieve_interval"))) servicer.setRetrieveInterval(Integer.parseInt(request.getParameter("retrieve_interval")));
			res = service.insertSelective(servicer);
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();
			if(valueCheck(request.getParameter("id")))  obj.put("id", Integer.parseInt(request.getParameter("id")));
			else obj.put("id", servicer.getId());
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
	
	private ResponseEntity<String> updateServiceByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
			Service record = new Service();
			record.setId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("service_identifier"))) record.setServiceStrId(request.getParameter("service_identifier"));
			if(valueCheck(request.getParameter("service_class"))) record.setServiceClass(request.getParameter("service_class"));
			if(valueCheck(request.getParameter("retrieve_interval"))) record.setRetrieveInterval(Integer.parseInt(request.getParameter("retrieve_interval")));
			
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
	
	private ResponseEntity<String> deleteServiceByID(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			root.put("code", 1);
			root.put("message", null);
			
			ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
			Service record = mapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(record == null) return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
			
			ServiceNamesMapper snMapper = sqlSession.getMapper(ServiceNamesMapper.class);
			int res = snMapper.deleteByServiceId(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE service_names RESULT: " + res);
			
			int servicesId = record.getServicesId();
			TransferConfigMapper trMapper = sqlSession.getMapper(TransferConfigMapper.class);			
			res = trMapper.deleteByServiceId(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE transfer_config RESULT: " + res);
			
			ServiceServiceAreaMapper saMapper = sqlSession.getMapper(ServiceServiceAreaMapper.class);
			res = saMapper.deleteByServiceId(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE service_service_area RESULT: " + res);
			
			ServiceScheduleMapper ssMapper = sqlSession.getMapper(ServiceScheduleMapper.class);
			res = ssMapper.deleteByServiceId(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE service_schedule RESULT: " + res);
			
			AdPostFileRepairMapper prMapper = sqlSession.getMapper(AdPostFileRepairMapper.class);
			res = prMapper.deleteByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE ad_post_file_repair RESULT: " + res);
			
			AdReceptionReportMapper r2Mapper = sqlSession.getMapper(AdReceptionReportMapper.class);
			res = r2Mapper.deleteByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE ad_reception_report RESULT: " + res);

			res = mapper.deleteByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			logger.info("DELETE service RESULT: " + res);			
			
			ServicesMapper sMapper = sqlSession.getMapper(ServicesMapper.class);
			res = sMapper.deleteByPrimaryKey(servicesId);
			logger.info("DELETE services RESULT: " + res);
			
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
	
	private ResponseEntity<String> addNameAtService(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("name"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("name_lang"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("language"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceNamesMapper mapper = sqlSession.getMapper(ServiceNamesMapper.class);
			ServiceNames record = new ServiceNames();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			record.setName(request.getParameter("name"));
			record.setNameLang(request.getParameter("name_lang"));
			record.setServiceLanguage(request.getParameter("language"));
			int res = mapper.insertSelective(record);			
			logger.info("INSERT RESULT: " + res);
			
			root.put("code", 1);
			root.put("message", null);
			
			JSONObject obj = new JSONObject();	
			obj.put("id", record.getId());
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
	
	private ResponseEntity<String> addTransferConfigAtService(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("qos_gbr"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("qos_qci"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("fec_type"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("fec_ratio"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			TransferConfigMapper mapper = sqlSession.getMapper(TransferConfigMapper.class);
			TransferConfig record = new TransferConfig();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			record.setQosGbr(Integer.parseInt(request.getParameter("qos_gbr")));
			record.setQosQci(Integer.parseInt(request.getParameter("qos_qci")));
			if(valueCheck(request.getParameter("arp_level"))){
				if(!valueCheck(request.getParameter("arp_pre_emption_capability")) ||
					!valueCheck(request.getParameter("arp_pre_emption_vulnerability"))) 
						return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
				record.setArpLevel(Integer.parseInt(request.getParameter("arp_level")));
				record.setArpPreEmptionCapability(Integer.parseInt(request.getParameter("arp_pre_emption_capability")));
				record.setArpPreEmptionVulnerability(Integer.parseInt(request.getParameter("arp_pre_emption_vulnerability")));
			}
			record.setFecType(request.getParameter("fec_type"));
			record.setFecRatio(Integer.parseInt(request.getParameter("fec_ratio")));
			if(valueCheck(request.getParameter("segment_avaliable_offset"))) record.setSegmentAvailableOffset(Integer.parseInt(request.getParameter("segment_avaliable_offset")));
			
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
	
	private ResponseEntity<String> addServiceScheduleAtService(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("schedule_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceScheduleMapper mapper = sqlSession.getMapper(ServiceScheduleMapper.class);
			ServiceSchedule record = new ServiceSchedule();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			record.setScheduleId(Integer.parseInt(request.getParameter("schedule_id")));
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
	
	private ResponseEntity<String> addServiceAreaAtService(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceServiceAreaMapper mapper = sqlSession.getMapper(ServiceServiceAreaMapper.class);
			ServiceServiceArea record = new ServiceServiceArea();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			record.setServiceAreaId(Integer.parseInt(request.getParameter("service_area_id")));
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
	
	private ResponseEntity<String> addAdPostFileRepairAtService(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("offset_time"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("random_time"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			AdPostFileRepairMapper mapper = sqlSession.getMapper(AdPostFileRepairMapper.class);
			AdPostFileRepair record = new AdPostFileRepair();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			record.setOffsetTime(Integer.parseInt(request.getParameter("offset_time")));
			record.setRandomTime(Integer.parseInt(request.getParameter("random_time")));
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
	
	private ResponseEntity<String> addAdReceptionReportAtService(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("report_type"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("offset_time"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("random_time"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			AdReceptionReportMapper mapper = sqlSession.getMapper(AdReceptionReportMapper.class);
			AdReceptionReport record = new AdReceptionReport();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			record.setReportType(request.getParameter("report_type"));
			if(valueCheck(request.getParameter("sample_percentage"))) record.setSamplePercentage(Integer.parseInt(request.getParameter("sample_percentage")));
			record.setOffsetTime(Integer.parseInt(request.getParameter("offset_time")));
			record.setRandomTime(Integer.parseInt(request.getParameter("random_time")));
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
	
	private ResponseEntity<String> changeServiceName(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceNamesMapper mapper = sqlSession.getMapper(ServiceNamesMapper.class);
			ServiceNames record = new ServiceNames();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("name"))) record.setName(request.getParameter("name"));
			if(valueCheck(request.getParameter("name_lang"))) record.setNameLang(request.getParameter("name_lang"));
			if(valueCheck(request.getParameter("language"))) record.setServiceLanguage(request.getParameter("language"));
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
	
	private ResponseEntity<String> changeTransferConfig(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			TransferConfigMapper mapper = sqlSession.getMapper(TransferConfigMapper.class);
			TransferConfig record = new TransferConfig();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("qos_gbr"))) record.setQosGbr(Integer.parseInt(request.getParameter("qos_gbr")));
			if(valueCheck(request.getParameter("qos_qci"))) record.setQosQci(Integer.parseInt(request.getParameter("qos_qci")));
			if(valueCheck(request.getParameter("arp_level"))) record.setArpLevel(Integer.parseInt(request.getParameter("arp_level")));
			if(valueCheck(request.getParameter("arp_pre_emption_capability"))) record.setArpPreEmptionCapability(Integer.parseInt(request.getParameter("arp_pre_emption_capability")));
			if(valueCheck(request.getParameter("arp_pre_emption_vulnerability"))) record.setArpPreEmptionVulnerability(Integer.parseInt(request.getParameter("arp_pre_emption_vulnerability")));
			if(valueCheck(request.getParameter("arp_level"))) record.setArpPreEmptionVulnerability(Integer.parseInt(request.getParameter("arp_pre_emption_vulnerability")));
			if(valueCheck(request.getParameter("fec_type"))) record.setFecType(request.getParameter("fec_type"));
			if(valueCheck(request.getParameter("fec_ratio"))) record.setFecRatio(Integer.parseInt(request.getParameter("fec_ratio")));
			if(valueCheck(request.getParameter("segment_avaliable_offset"))) record.setSegmentAvailableOffset(Integer.parseInt(request.getParameter("segment_avaliable_offset")));
			
			int res = mapper.updateByPrimaryKeySelective(record);
			logger.info("UPDATE RESULT: " + res);
			
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
	
	private ResponseEntity<String> changeAdPostFileRepair(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			AdPostFileRepairMapper mapper = sqlSession.getMapper(AdPostFileRepairMapper.class);
			AdPostFileRepair record = new AdPostFileRepair();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("offset_time"))) record.setOffsetTime(Integer.parseInt(request.getParameter("offset_time")));
			if(valueCheck(request.getParameter("random_time"))) record.setRandomTime(Integer.parseInt(request.getParameter("random_time")));
			int res = mapper.updateByPrimaryKeySelective(record);
			logger.info("UPDATE RESULT: " + res);
			
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
	
	private ResponseEntity<String> changeAdReceptionReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			AdReceptionReportMapper mapper = sqlSession.getMapper(AdReceptionReportMapper.class);
			AdReceptionReport record = new AdReceptionReport();
			record.setServiceId(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("report_type"))) record.setReportType(request.getParameter("report_type"));
			if(valueCheck(request.getParameter("sample_percentage"))) record.setSamplePercentage(Integer.parseInt(request.getParameter("sample_percentage")));
			if(valueCheck(request.getParameter("offset_time"))) record.setOffsetTime(Integer.parseInt(request.getParameter("offset_time")));
			if(valueCheck(request.getParameter("random_time"))) record.setRandomTime(Integer.parseInt(request.getParameter("random_time")));
			int res = mapper.updateByPrimaryKeySelective(record);
			logger.info("UPDATE RESULT: " + res);
			
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
	
	private ResponseEntity<String> removeServiceName(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceNamesMapper mapper = sqlSession.getMapper(ServiceNamesMapper.class);			
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
	
	private ResponseEntity<String> removeTransferConfig(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			TransferConfigMapper mapper = sqlSession.getMapper(TransferConfigMapper.class);			
			int res = mapper.deleteByServiceId(Integer.parseInt(request.getParameter("id")));			
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
	
	private ResponseEntity<String> removeServiceSchedule(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("schedule_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceScheduleMapper mapper = sqlSession.getMapper(ServiceScheduleMapper.class);	
			ServiceScheduleExample exm = new ServiceScheduleExample();		
			exm.and().andServiceIdEqualTo(Integer.parseInt(request.getParameter("id")));
			exm.and().andScheduleIdEqualTo(Integer.parseInt(request.getParameter("schedule_id")));
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
	
	private ResponseEntity<String> removeServiceArea(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("service_area_id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			ServiceServiceAreaMapper mapper = sqlSession.getMapper(ServiceServiceAreaMapper.class);	
			ServiceServiceAreaExample exm = new ServiceServiceAreaExample();		
			exm.and().andServiceIdEqualTo(Integer.parseInt(request.getParameter("id")));
			exm.and().andServiceAreaIdEqualTo(Integer.parseInt(request.getParameter("service_area_id")));
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
	
	private ResponseEntity<String> removeAdPostFileRepair(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			AdPostFileRepairMapper mapper = sqlSession.getMapper(AdPostFileRepairMapper.class);			
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
	
	private ResponseEntity<String> removeAdReceptionReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			AdReceptionReportMapper mapper = sqlSession.getMapper(AdReceptionReportMapper.class);			
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
	
	private ResponseEntity<String> sendCreateService(HttpServletRequest request) {
		try {			
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			ServiceMapper sMapper = sqlSession.getMapper(ServiceMapper.class);
			Service service = sMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(service == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);		
			
			XmlParaSet root = makeHeadAndTransactionInfo(Integer.parseInt(request.getParameter("id")), "SERVICE.CREATE", "REQUEST");
			XmlParaSet params = getServiceInfoXmlParaSet(request);			
			root.addPara(params);		// </parameters>
			XmlPara tr_id = root.delPara("tr_id");
			
			XmlParaSet resXml = sendPostHttpRequest(root);
			XmlParaSet trans = getPara(resXml, "transaction");
			XmlParaSet result = getPara(trans, "result");
			JSONObject resRoot = new JSONObject();
			resRoot.put("code", result.getIntValue("code") == 100 ? 1 : result.getIntValue("code"));
			resRoot.put("message", result.getValue("message").equalsIgnoreCase("OK") ? null : result.getValue("message"));
			
			TransReportMapper trMapper = sqlSession.getMapper(TransReportMapper.class);
			TransReport record = new TransReport();
			record.setResultCode(result.getIntValue("code") == 100 ? 0 : result.getIntValue("code"));
			record.setResultMsg(result.getValue("message"));
			record.setSendMsg(XmlFormer.toXmlString(root));
			record.setRecvMsg(XmlFormer.toXmlString(resXml));
			TransReportExample exm = new TransReportExample();			
			exm.and().andIdEqualTo(tr_id.getIntValue());
			exm.and().andTransactionIdEqualTo(trans.getIntAttr("id"));
			trMapper.updateByExampleSelective(record, exm);
			
			return new ResponseEntity<String>(resRoot.toJSONString(), HttpStatus.OK);
		}
		catch (MyException e) {
			return new ResponseEntity<String>(e.getMessage(), e.getErrcode());
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> sendUpdateService(HttpServletRequest request) {		
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			ServiceMapper sMapper = sqlSession.getMapper(ServiceMapper.class);
			Service service = sMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(service == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			XmlParaSet root = makeHeadAndTransactionInfo(Integer.parseInt(request.getParameter("id")), "SERVICE.UPDATE", "REQUEST");			
			
			XmlParaSet params = getServiceInfoXmlParaSet(request);	
			root.addPara(params);		// </parameters>
			XmlPara tr_id = root.delPara("tr_id");
			
			XmlParaSet resXml = sendPostHttpRequest(root);
			XmlParaSet trans = getPara(resXml, "transaction");
			XmlParaSet result = getPara(trans, "result");
			JSONObject resRoot = new JSONObject();
			resRoot.put("code", result.getIntValue("code") == 100 ? 1 : result.getIntValue("code"));
			resRoot.put("message", result.getValue("message").equalsIgnoreCase("OK") ? null : result.getValue("message"));
			
			TransReportMapper trMapper = sqlSession.getMapper(TransReportMapper.class);
			TransReport record = new TransReport();
			record.setResultCode(result.getIntValue("code") == 100 ? 0 : result.getIntValue("code"));
			record.setResultMsg(result.getValue("message"));
			record.setSendMsg(XmlFormer.toXmlString(root));
			record.setRecvMsg(XmlFormer.toXmlString(resXml));
			TransReportExample exm = new TransReportExample();
			exm.and().andIdEqualTo(tr_id.getIntValue());
			exm.and().andTransactionIdEqualTo(trans.getIntAttr("id"));
			trMapper.updateByExampleSelective(record, exm);
			
			/// 전송 성공 시 changed 필드를 모두 0으로 업데이트
			if(result.getIntValue("code") == 100) {
				ServiceScheduleMapper ssMapper = sqlSession.getMapper(ServiceScheduleMapper.class);
				List<ServiceSchedule> schedules = ssMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
				for(int i = 0; i < schedules.size(); i++) {
					ServiceSchedule schedule = schedules.get(i);
					ScheduleContentsMapper scMapper = sqlSession.getMapper(ScheduleContentsMapper.class);
					List<ScheduleContents> contents = scMapper.selectScheduleContentsByScheduleId(schedule.getScheduleId());
					for(int j = 0; j < contents.size(); j++) {
						ScheduleContents content = contents.get(j);
						ScheduleContentsExample scExm = new ScheduleContentsExample();
						scExm.and().andScheduleIdEqualTo(schedule.getScheduleId());
						scExm.and().andContentIdEqualTo(content.getContentId());
						
						/// cancelled인 데이터는 delete한다.
						/// TOTO: 추후에 이력성 테이블에 INSERT처리 하던지...
						if(content.getCancelled() == 1) {							
							scMapper.deleteByExample(scExm);
						}
						else if(content.getChanged() == 1) {
							content = new ScheduleContents();
							content.setChanged(0);
							scMapper.updateByExampleSelective(content, scExm);
						}
					}
					
					/// cancelled인 데이터는 delete한다.
					/// TOTO: 추후에 이력성 테이블에 INSERT처리 하던지...
					if(schedule.getCancelled() == 1) {
						ServiceScheduleExample ssExm = new ServiceScheduleExample();
						ssExm.and().andServiceIdEqualTo(Integer.parseInt(request.getParameter("id")));
						ssExm.and().andScheduleIdEqualTo(schedule.getScheduleId());
						ssMapper.deleteByExample(ssExm);
					}
				}
			}
			
			return new ResponseEntity<String>(resRoot.toJSONString(), HttpStatus.OK);
		}
		catch (MyException e) {
			return new ResponseEntity<String>(e.getMessage(), e.getErrcode());
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> sendAbortService(HttpServletRequest request, String msgType) {
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			ServiceMapper sMapper = sqlSession.getMapper(ServiceMapper.class);
			Service service = sMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(service == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			XmlParaSet root = makeHeadAndTransactionInfo(Integer.parseInt(request.getParameter("id")), msgType, "REQUEST");
			XmlParaSet params = new XmlParaSet("parameters");
			XmlParaSet servQuery = new XmlParaSet("serviceQuery");
			XmlParaSet condt = new XmlParaSet("condition");
			
			ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
			Service data = mapper.selectTypeTransferConfig(Integer.parseInt(request.getParameter("id")));
			if(data == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);			

			condt.addPara(new XmlPara("serviceId")); condt.setValue("serviceId", data.getServiceStrId());
			servQuery.addPara(condt);
			params.addPara(servQuery);
			root.addPara(params);
			XmlPara tr_id = root.delPara("tr_id");
			
			XmlParaSet resXml = sendPostHttpRequest(root);
			XmlParaSet trans = getPara(resXml, "transaction");
			XmlParaSet result = getPara(trans, "result");
			JSONObject resRoot = new JSONObject();
			resRoot.put("code", result.getIntValue("code") == 100 ? 1 : result.getIntValue("code"));
			resRoot.put("message", result.getValue("message").equalsIgnoreCase("OK") ? null : result.getValue("message"));	
			
			TransReportMapper trMapper = sqlSession.getMapper(TransReportMapper.class);
			TransReport record = new TransReport();
			record.setResultCode(result.getIntValue("code") == 100 ? 0 : result.getIntValue("code"));
			record.setResultMsg(result.getValue("message"));
			record.setSendMsg(XmlFormer.toXmlString(root));
			record.setRecvMsg(XmlFormer.toXmlString(resXml));
			TransReportExample exm = new TransReportExample();
			exm.and().andIdEqualTo(tr_id.getIntValue());
			exm.and().andTransactionIdEqualTo(trans.getIntAttr("id"));
			trMapper.updateByExampleSelective(record, exm);
			
			if(result.getIntValue("code") == 100) {
				ServicesMapper ssMapper = sqlSession.getMapper(ServicesMapper.class);
				Services ssRecord = new Services();
				ssRecord.setCancelled(1);
				ServicesExample ssExm = new ServicesExample();
				ssExm.createCriteria().andIdEqualTo(data.getServicesId());
				ssMapper.updateByExampleSelective(ssRecord, ssExm);
				
				Service sRecord = new Service();
				sRecord.setCancelled(1);
				ServiceExample sExm = new ServiceExample();
				sExm.and().andIdEqualTo(Integer.parseInt(request.getParameter("id")));				
				mapper.updateByExampleSelective(sRecord, sExm);
			}
			
			return new ResponseEntity<String>(resRoot.toJSONString(), HttpStatus.OK);			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> retrieveService(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			XmlParaSet msg = makeHeadAndTransactionInfo(Integer.parseInt(request.getParameter("id")), "SERVICE.RETRIEVE", "REQUEST");
			XmlPara tr_id = msg.delPara("tr_id");
			
			//XmlParaSet resXml = sendPostHttpRequest(msg);
			String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<message name=\"SERVICE.RETRIEVE\" type=\"RESPONSE\">\n" +
                    "    <transaction id=\"2\">\n" +
                    "        <result>\n" +
                    "            <code>100</code>\n" +
                    "            <message>OK</message>\n" +
                    "        </result>" +
                    "    </transaction>\n" +
                    "    <parameters>\n" +
                    "        <services>\n" +
                    "            <service serviceType=\"streaming\">\n" +
                    "                <streaming serviceId=\"urn:3gpp:streaming:1\" serviceClass=\"urn:3gpp:mbms:ds\">\n" +
                    "                    <transferConfig>\n" +
                    "                        <QoS>\n" +
                    "                            <GBR>70200</GBR>\n" +
                    "                            <QCI>149</QCI>\n" +
                    "                            <ARP>\n" +
                    "                                <level>9</level>\n" +
                    "                                <preEmptionCapability>1</preEmptionCapability>\n" +
                    "                                <preEmptionVulnerability>1</preEmptionVulnerability>\n" +
                    "                            </ARP>\n" +
                    "                        </QoS>\n" +
                    "                        <FEC>\n" +
                    "                            <fecType>Raptor</fecType>\n" +
                    "                            <fecRatio>10</fecRatio>\n" +
                    "                        </FEC>\n" +
                    "                        <SegmentAvailableOffset>10</SegmentAvailableOffset>\n" +
                    "                    </transferConfig>\n" +
                    "                    <schedule index=\"1\" cancelled=\"false\" start=\"2015-05-04T16:00:00.000+09:00\" stop=\"2015-05-04T17:00:00.000+09:00\"/>\n" +
                    "                    <contentSet contentSetId=\"1\" cancelled=\"false\">\n" +
                    "                        <serviceArea>\n" +
                    "                            <said>2</said>\n" +
                    "                        </serviceArea>\n" +
                    "                        <mpd changed=\"false\">\n" +
                    "                            <mpdURI>http://192.168.1.115:8088/SonySix24h/Manifest_2.mpd</mpdURI>\n" +
                    "                        </mpd>\n" +
                    "                    </contentSet>\n" +
                    "                    <associatedDelivery>\n" +
                    "                        <receptionReport reportType=\"StaR-only\" cancelled=\"false\" offsetTime=\"100\" randomTime=\"100\" samplePercentage=\"100\"/>\n" +
                    "                    </associatedDelivery>\n" +
                    "                </streaming>\n" +
                    "            </service>\n" +
                    "        </services>\n" +
                    "    </parameters>\n" +
                    "</message>";
			XmlParaSet resXml = XmlFormer.toXmlParaSet(xmlStr);
			
			XmlParaSet trans = getPara(resXml, "transaction");
			XmlParaSet result = getPara(trans, "result");
			
			root.put("code", result.getIntValue("code"));
			root.put("message", result.getValue("message").equalsIgnoreCase("OK") ? null : result.getValue("message"));
			
			XmlParaSet parameters = getPara(resXml, "parameters");
			XmlParaSet services = getPara(parameters, "services");
			JSONArray svArray = new JSONArray();
			for(int i = 0; i < services.size(); i++) {
				if(!services.get(i).getTag().equalsIgnoreCase("service")) continue;
				JSONObject svObj = new JSONObject();
				svObj.put("id", Integer.parseInt(request.getParameter("id")));
				XmlParaSet service = (XmlParaSet) services.get(i);
				int serviceTypeId = getServiceTypeId(service.getAttr("serviceType"));
				if(serviceTypeId == 0) return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
				svObj.put("service_type_id", serviceTypeId);
				
				XmlParaSet servt = getPara(service, service.getAttr("serviceType"));
				svObj.put("service_identifier", servt.getAttr("serviceId"));
				svObj.put("service_class", servt.getAttr("serviceClass"));
				svObj.put("retrieve_interval", servt.getAttr("retrieveInterval"));
				
				JSONArray nameArray = null;
				JSONObject nmObj =  new JSONObject();
				for(int j = 0; j < servt.size(); j++) {					
					if(servt.get(j).getTag().equalsIgnoreCase("name")) {
						if(nameArray == null) nameArray = new JSONArray();	
						XmlPara name = servt.get(j);
						nmObj.put("name", name.getValue());
						nmObj.put("name_lang", name.getAttr("lang"));
					}
					else if(servt.get(j).getTag().equalsIgnoreCase("serviceLanguage")) {
						if(nameArray == null) nameArray = new JSONArray();
						XmlPara lang = (XmlPara) servt.get(j);
						nmObj.put("language", lang.getValue());
					}
					if(nmObj.size() == 3) {
						nameArray.add(nmObj);
						nmObj = new JSONObject();
					}
				}
				if(nameArray != null) svObj.put("service_names", nameArray);	// service_names=[{}]
				
				XmlParaSet tranCfg = getPara(servt, "transferConfig");
				XmlParaSet qos = getPara(tranCfg, "QoS");
				JSONObject transObj = new JSONObject();
				JSONObject qosObj = new JSONObject();
				qosObj.put("gbr", qos.getIntValue("GBR"));
				qosObj.put("qci", qos.getIntValue("QCI"));
				
				XmlParaSet arp = (XmlParaSet) qos.getPara("ARP");
				if(arp != null || arp.size() > 0) {
					JSONObject arpObj = new JSONObject();
					arpObj.put("level", arp.getIntValue("level"));
					arpObj.put("pre_emption_capability", arp.getIntValue("preEmptionCapability"));
					arpObj.put("pre_emption_vulnerability", arp.getIntValue("preEmptionVulnerability"));
					qosObj.put("arp", arpObj);
				}
				transObj.put("qos", qosObj);	// qos={}
				
				XmlParaSet fec = getPara(tranCfg, "FEC");
				JSONObject fecObj = new JSONObject();
				fecObj.put("type", fec.getValue("fecType"));
				fecObj.put("ratio", fec.getValue("fecRatio"));				
				transObj.put("fec", fecObj);	// fec={}
				
				XmlPara saOffset = tranCfg.getPara("SegmentAvailableOffset");
				if(saOffset != null) transObj.put("segment_avaliable_offset", saOffset.getIntValue());
				svObj.put("transfer_config", transObj);	// transfer_config={}
				
				JSONArray saArray = new JSONArray();
				JSONArray scArray = new JSONArray();
				if(serviceTypeId == SERVICE_TYPE_STREAMING) {				
					JSONObject schObj = new JSONObject();
					for(int k = 0; k < servt.size(); k++) {						
						if(servt.get(k).getTag().equalsIgnoreCase("schedule")) {
							XmlPara schedule = servt.get(k);				
							schObj.put("id", schedule.getIntAttr("index"));
							schObj.put("start_date", schedule.getAttr("start"));
							schObj.put("end_date", schedule.getAttr("stop"));
						}
						else if(servt.get(k).getTag().equalsIgnoreCase("contentSet")) {
							XmlParaSet content = (XmlParaSet) servt.get(k);
							JSONObject contObj = new JSONObject();
							contObj.put("id", content.getIntAttr("contentSetId"));
							
							for(int m = 0; m < content.size(); m++) {
								if(!content.get(m).getTag().equalsIgnoreCase("serviceArea")) continue;
								XmlParaSet serviceArea = (XmlParaSet) content.get(m);
								JSONObject obj = new JSONObject();
								obj.put("service_area_id", serviceArea.getIntValue("said"));
								saArray.add(obj);
							}
							for(int m = 0; m < content.size(); m++) {
								if(!content.get(m).getTag().equalsIgnoreCase("mpd")) continue;
								XmlParaSet mpd = (XmlParaSet) content.get(m);
								contObj.put("url", mpd.getValue("mpdURI"));
								break;
							}
							schObj.put("contents", contObj);
						}
						if(schObj.size() >= 4) {
							scArray.add(schObj);
							schObj = new JSONObject();
						}
					}
					
				}
				else {
					for(int k = 0; k < servt.size(); k++) {						
						if(servt.get(k).getTag().equalsIgnoreCase("serviceArea")) {
							XmlParaSet serviceArea = (XmlParaSet) servt.get(k);
							JSONObject obj = new JSONObject();
							obj.put("service_area_id", serviceArea.getIntValue("said"));
							saArray.add(obj);
							continue;
						}						
						if(!servt.get(k).getTag().equalsIgnoreCase("schedule")) continue;
						XmlParaSet schedule = (XmlParaSet) servt.get(k);
						JSONObject schObj = new JSONObject();
						schObj.put("id", schedule.getIntAttr("index"));
						schObj.put("start_date", schedule.getAttr("start"));
						schObj.put("end_date", schedule.getAttr("stop"));
						
						JSONArray contArray = new JSONArray();
						for(int m = 0; m < schedule.size(); m++) {
							if(!schedule.get(m).getTag().equalsIgnoreCase("content")) continue;
							XmlParaSet content = (XmlParaSet) schedule.get(m);
							JSONObject contObj = new JSONObject();
							contObj.put("id", content.getIntAttr("contentId"));
							contObj.put("url", content.getValue("fileURI"));
							XmlPara dvrInfo = content.getPara("deliveryInfo");
							contObj.put("start_time", dvrInfo.getAttr("start"));
							contObj.put("end_time", dvrInfo.getAttr("end"));
							contArray.add(contObj);
						}
						schObj.put("contents", contArray);
						scArray.add(schObj);
					}					
				}
				svObj.put("service_area_ids", saArray); // service_area_ids=[{}]
				svObj.put("schedules", scArray);	// schedules=[{}]
				
				XmlParaSet adSet = (XmlParaSet) servt.getPara("associatedDelivery");
				if(adSet != null && adSet.size() > 0) {
					JSONObject adObj = new JSONObject();
					XmlPara repair = adSet.getPara("postFileRepair");
					if(repair != null) {
						JSONObject repairObj = new JSONObject();
						repairObj.put("offset_time", repair.getIntAttr("offsetTime"));
						repairObj.put("random_time", repair.getIntAttr("randomTime"));
						adObj.put("post_file_repair", repairObj);
					}
					XmlPara report = adSet.getPara("receptionReport");
					if(report != null) {
						JSONObject reportObj = new JSONObject();
						reportObj.put("report_type", report.getAttr("reportType"));
						reportObj.put("sample_percentage", report.getIntAttr("samplePercentage"));
						reportObj.put("offset_time", report.getAttr("offsetTime"));
						reportObj.put("random_time", report.getAttr("randomTime"));
						adObj.put("reception_report", reportObj);
					}
					svObj.put("associated_delivery", adObj); // associated_delivery={}
				}
				
				svArray.add(svObj);
			}
			
			TransReportMapper trMapper = sqlSession.getMapper(TransReportMapper.class);
			TransReport record = new TransReport();
			record.setResultCode(result.getIntValue("code") == 100 ? 0 : result.getIntValue("code"));
			record.setResultMsg(result.getValue("message"));
			record.setSendMsg(XmlFormer.toXmlString(msg));
			record.setRecvMsg(XmlFormer.toXmlString(resXml));
			TransReportExample exm = new TransReportExample();
			exm.and().andIdEqualTo(tr_id.getIntValue());
			exm.and().andTransactionIdEqualTo(trans.getIntAttr("id"));			
			trMapper.updateByExampleSelective(record, exm);			
			
			root.put("result", svArray);	// result=[{}]
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private XmlParaSet makeHeadAndTransactionInfo(int serviceId, String name, String type) throws MyException {
		try {
			XmlParaSet root = new XmlParaSet("message");
			root.addAttr(new XmlTlv("name"));
			root.addAttr(new XmlTlv("type"));
			root.setAttr("name", name);
			root.setAttr("type", type);
			
			ServiceMapper sMapper = sqlSession.getMapper(ServiceMapper.class);
			Map<String, String> agentInfo = sMapper.selectBmscAgentKey(serviceId);
			if(agentInfo == null || agentInfo.size() == 0) throw new MyException(HttpStatus.NOT_FOUND, "Not found Service Info");
			
			Service record = sMapper.selectByPrimaryKey(serviceId);
			TransReportMapper trMapper = sqlSession.getMapper(TransReportMapper.class);			
			TransReport trans = new TransReport();
			trans.setServicesId(record.getServicesId());
			//String agentKey = Base64Coder.encode(agentInfo.get("ipaddress"));
			String agentKey = Base64Coder.encode(agentInfo.get("bmsc_name"));
			trans.setAgentKey(agentKey);
			trans.setSendDtm(new Date());
			trans.setResultCode(0);
			int res = 0;
			if(trMapper.selectCount() == 0) {
				trans.setTransactionId(101);
				res = trMapper.insertSelective(trans);
			}
			else {
				res = trMapper.insertSelective(trans);
			}
			if(res == 0) throw new Exception("Insert Failed!!! Transcaction Report table");
						
			trans = trMapper.selectByPrimaryKey(trans.getId());			
			XmlParaSet transaction = new XmlParaSet("transaction");
			transaction.addAttr(new XmlTlv("id"));
			transaction.setAttr("id", trans.getTransactionId());
			transaction.addPara(new XmlPara("agentKey"));
			transaction.setValue("agentKey", agentKey);
			root.addPara(transaction);	// </transaction>
			root.addPara(new XmlPara("tr_id")); root.setValue("tr_id", trans.getId());
			
			return root;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new MyException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	private XmlParaSet getServiceInfoXmlParaSet(HttpServletRequest request) throws MyException{
		try {
			XmlParaSet params = new XmlParaSet("parameters");
			XmlParaSet servs = new XmlParaSet("services");
			
			ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
			Service data = mapper.selectTypeTransferConfig(Integer.parseInt(request.getParameter("id")));
			if(data == null) throw new MyException(HttpStatus.NOT_FOUND, "Not found Transfer Config Information!!!");
			
			XmlParaSet serv = new XmlParaSet("service");
			serv.addAttr(new XmlTlv("serviceType"));
			serv.setAttr("serviceType", getServiceName(data.getServiceTypeId()));
			XmlParaSet servt = new XmlParaSet(getServiceName(data.getServiceTypeId()));
			if(valueCheck(data.getServiceStrId())) {
				servt.addAttr(new XmlTlv("serviceId")); 
				servt.setAttr("serviceId", data.getServiceStrId());
			}
			if(valueCheck(data.getServiceClass())) {
				servt.addAttr(new XmlTlv("serviceClass"));
				servt.setAttr("serviceClass", data.getServiceClass());
			}
			if(data.getRetrieveInterval() != null) {
				servt.addAttr(new XmlTlv("retrieveInterval"));
				servt.setAttr("retrieveInterval", data.getServiceTypeId());
			}
			
			/// Service Names(0 ~ N)
			ServiceNamesMapper namesMapper = sqlSession.getMapper(ServiceNamesMapper.class);
			List<ServiceNames> names = namesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(names != null && names.size() > 0) {
				for(int i = 0; i < names.size(); i++) {
					ServiceNames name = names.get(i);
					XmlPara namel = new XmlPara("name");
					namel.addAttr(new XmlTlv("lang"));
					namel.setAttr("lang", name.getNameLang());
					namel.setValue(name.getName());
					XmlPara lang = new XmlPara("serviceLanguage");
					lang.setValue(name.getServiceLanguage());
					servt.addPara(namel);
					servt.addPara(lang);
				}
			}
			XmlParaSet transcfg = new XmlParaSet("transferConfig");			
			
			TransferConfigMapper tcMapper = sqlSession.getMapper(TransferConfigMapper.class);
			TransferConfig config = tcMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			/// TransferConfig(1)
			XmlParaSet qos = new XmlParaSet("QoS");
			{
				/// QOS				
				qos.addPara(new XmlPara("GBR")); qos.setValue("GBR", config.getQosGbr()); 
				qos.addPara(new XmlPara("QCI")); qos.setValue("QCI", config.getQosQci());	
				XmlParaSet arp = new XmlParaSet("ARP");
				{
					/// QOS-ARP					
					arp.addPara(new XmlPara("level")); arp.setValue("level", config.getArpLevel());
					arp.addPara(new XmlPara("preEmptionCapability")); arp.setValue("preEmptionCapability", config.getArpPreEmptionCapability());
					arp.addPara(new XmlPara("preEmptionVulnerability")); arp.setValue("preEmptionVulnerability", config.getArpPreEmptionVulnerability());
				}
				qos.addPara(arp); // </ARP>
				transcfg.addPara(qos); // </QOS>
				
				/// FEC
				XmlParaSet fec = new XmlParaSet("FEC");
				fec.addPara(new XmlPara("fecType")); fec.setValue("fecType", config.getFecType());
				fec.addPara(new XmlPara("fecRatio")); fec.setValue("fecRatio", config.getFecRatio());
				transcfg.addPara(fec);
				if(config.getSegmentAvailableOffset() != null) {
					transcfg.addPara(new XmlPara("SegmentAvailableOffset")); transcfg.setValue("SegmentAvailableOffset", config.getSegmentAvailableOffset());
				} // </FEC>
			}
			servt.addPara(transcfg); // </trasferConfig>

			/// Service Area(1 ~ N)
			ServiceServiceAreaMapper saMapper = sqlSession.getMapper(ServiceServiceAreaMapper.class);
			List<ServiceServiceArea> areas = saMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(areas == null || areas.size() == 0) {
				throw new Exception("Not found ServiceArea of Service(" + request.getParameter("id") + ")!!!");				
			}
			
			if(data.getServiceTypeId() != SERVICE_TYPE_STREAMING) {
				XmlParaSet areaid = new XmlParaSet("serviceArea");
				for(int i = 0; i < areas.size(); i++) {
					ServiceServiceArea area = areas.get(i);					
					XmlPara said = new XmlPara("said"); 
					said.setValue(area.getServiceAreaId());
					areaid.addPara(said);
				}
				servt.addPara(areaid); // </serviceArea>
			}
			
			/// Schedules(1 ~ N)
			ServiceScheduleMapper ssMapper = sqlSession.getMapper(ServiceScheduleMapper.class);
			List<ServiceSchedule> schedules = ssMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(schedules == null || schedules.size() == 0) {
				throw new Exception("Not found Schedule of Service(" + request.getParameter("id") + ")!!!");
			}
			
			for(int i = 0; i < schedules.size(); i++) {
				ServiceSchedule schedule = schedules.get(i);
				XmlParaSet sobj = new XmlParaSet("schedule");
				sobj.addAttr(new XmlTlv("index")); sobj.setAttr("index", i + 1);
				/// create_service이면서, cancelled인 schedule은 전송하지 않는다.
				if(request.getParameter("request_type").equalsIgnoreCase("send_create_service") && schedule.getCancelled() == 1) continue;
				sobj.addAttr(new XmlTlv("cancelled")); sobj.setAttr("cancelled", schedule.getCancelled() == 0 ? "false" : "true");
				sobj.addAttr(new XmlTlv("start")); sobj.setAttr("start", getFormatDateTime(schedule.getStartDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
				sobj.addAttr(new XmlTlv("stop")); sobj.setAttr("stop", getFormatDateTime(schedule.getEndDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
				
				ScheduleContentsMapper scMapper = sqlSession.getMapper(ScheduleContentsMapper.class);
				List<ScheduleContents> contents = scMapper.selectScheduleContentsByScheduleId(schedule.getScheduleId());
				if(contents == null || contents.size() == 0) {
					throw new Exception("Not found Contents of Schedule(" + schedule.getScheduleId() + ")!!!");
				}
				
				// contents(1 ~ N)
				JSONArray cArray = new JSONArray();
				for(int j = 0; j < contents.size(); j++) {
					ScheduleContents content = contents.get(j);
					XmlParaSet cobj = new XmlParaSet("content");
					if(data.getServiceTypeId() == SERVICE_TYPE_STREAMING) {
						cobj.setTag("contentSet");
						cobj.addAttr(new XmlTlv("contentSetId")); cobj.setAttr("contentSetId", j + 1);
						/// create_service이면서, cancelled인 contents는 전송하지 않는다.
						if(request.getParameter("request_type").equalsIgnoreCase("send_create_service") && content.getCancelled() == 1) continue;
						cobj.addAttr(new XmlTlv("cancelled")); cobj.setAttr("cancelled", content.getCancelled() == 0 ? "false" : "true");
						XmlParaSet areaid = new XmlParaSet("serviceArea");
						for(int k = 0; k < areas.size(); k++) {
							ServiceServiceArea area = areas.get(k);							
							XmlPara said = new XmlPara("said"); 
							said.setValue(area.getServiceAreaId());
							areaid.addPara(said);
						}
						cobj.addPara(areaid); // </serviceArea>
						XmlParaSet mpd = new XmlParaSet("mpd");
						mpd.addAttr(new XmlTlv("changed")); mpd.setAttr("changed", content.getChanged() == 0 ? "false" : "true");
						mpd.addPara(new XmlPara("mpdURI")); mpd.setValue("mpdURI", content.getUrl());
						cobj.addPara(mpd); 	 // </mpd>
						servt.addPara(sobj); // </schedule>
						servt.addPara(cobj); // </contentSet>
						continue;
					}
					else {						
						if(data.getServiceTypeId() != SERVICE_TYPE_CAROUSEL_SINGLEFILE){
							cobj.addAttr(new XmlTlv("contentId")); cobj.setAttr("contentId", j + 1);
							cobj.addAttr(new XmlTlv("contentType")); cobj.setAttr("contentType", content.getContentType());
							cobj.addAttr(new XmlTlv("cancelled")); cobj.setAttr("cancelled", content.getCancelled() == 0 ? "false" : "true");							
						}
						else {
							cobj.addAttr(new XmlTlv("contentType")); cobj.setAttr("contentType", content.getContentType());
						}
						if(data.getServiceTypeId() == SERVICE_TYPE_FILE_DOWNLOAD) {
							cobj.addAttr(new XmlTlv("changed")); cobj.setAttr("changed", content.getChanged() == 0 ? "false" : "true");
						}
					}
					
					cobj.addPara(new XmlPara("fileURI"));
					cobj.setValue("fileURI", content.getUrl());
					if(data.getServiceTypeId() == SERVICE_TYPE_FILE_DOWNLOAD) {
						XmlPara deliveryInfo = new XmlPara("deliveryInfo");
						deliveryInfo.addAttr(new XmlTlv("start")); deliveryInfo.setAttr("start", getFormatDateTime(content.getStartTime(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));						
						deliveryInfo.addAttr(new XmlTlv("end")); deliveryInfo.setAttr("end", getFormatDateTime(content.getEndTime(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
						cobj.addPara(deliveryInfo);
					}
					sobj.addPara(cobj); //</content>
				}
				if(data.getServiceTypeId() != SERVICE_TYPE_STREAMING) servt.addPara(sobj); // </schedule>
			}
			
			/// Associated Delivery
			XmlParaSet adobj = new XmlParaSet("associatedDelivery");
			AdPostFileRepairMapper reMapper = sqlSession.getMapper(AdPostFileRepairMapper.class);
			AdPostFileRepair repair = reMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(repair != null) {				
				XmlPara frepair = new XmlPara("postFileRepair");
				frepair.addAttr(new XmlTlv("offsetTime")); frepair.setAttr("offsetTime", repair.getOffsetTime());
				frepair.addAttr(new XmlTlv("randomTime")); frepair.setAttr("randomTime", repair.getRandomTime());
				frepair.addAttr(new XmlTlv("cancelled")); frepair.setAttr("cancelled", repair.getCancelled() == 0 ? "false" : "true");
				adobj.addPara(frepair); // </postFileRepair>				
			}
			
			AdReceptionReportMapper rtMapper = sqlSession.getMapper(AdReceptionReportMapper.class);
			AdReceptionReport report = rtMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			if(report != null) {
				XmlPara robj = new XmlPara("receptionReport");
				robj.addAttr(new XmlTlv("reportType")); robj.setAttr("reportType", report.getReportType());
				if(report.getSamplePercentage() != null) {
					robj.addAttr(new XmlTlv("samplePercentage"));
					robj.setAttr("samplePercentage", report.getSamplePercentage());
				}
				robj.addAttr(new XmlTlv("offsetTime")); robj.setAttr("offsetTime", report.getOffsetTime());
				robj.addAttr(new XmlTlv("randomTime")); robj.setAttr("randomTime", report.getRandomTime());
				adobj.addPara(robj); // </receptionReport>
			}
			servt.addPara(adobj); 		// </associatedDelivery>
			serv.addPara(servt);  		// </fileDownload등>
			servs.addPara(serv);  		// </service>
			params.addPara(servs); 		// </services>
			
			return params;
		}
		catch (Exception e) {
			throw new MyException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}		
	}
	
	private XmlParaSet sendPostHttpRequest(XmlParaSet xmlParaSet) throws Exception{
		try {
			URL obj = new URL(b2InterfefaceURL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("POST");
			//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(XmlFormer.toXmlString(xmlParaSet).getBytes("UTF-8"));
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			logger.info("Sending 'POST' request to URL : " + b2InterfefaceURL);
			//logger.info("Post parameters : " + urlParameters);
			logger.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//print result
			logger.info(response.toString());
			
			XmlParaSet resXml = XmlFormer.toXmlParaSet(response.toString());		
			return resXml;
		}
		catch (Exception e) {
			throw e;			
		}
	}
	
	private void rollbackServiceInfo(int serviceId) {
		try {
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
	
	@RequestMapping(value = "B2interface.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> b2Interface(HttpServletRequest request) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String inputLine;
			StringBuffer req = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				req.append(inputLine);
			}
			in.close();
			//print request
			logger.info(req.toString());
			
			XmlParaSet reqXml = XmlFormer.toXmlParaSet(req.toString().replaceAll("&", "&amp;"));
			
			String strXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<message name=\"SERVICE.CREATE\" type=\"RESPONSE\">\n" +
                    "    <transaction id=\"" + getPara(reqXml, "transaction").getIntAttr("id") + "\">\n" +
                    "        <result>\n" +
                    "            <code>8410</code>\n" +
                    "            <message>OK</message>\n" +
                    "        </result>\n" +
                    "    </transaction>\n" +
                    "</message>";
			
			return new ResponseEntity<String>(strXml, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}		
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "B3interface.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> B3interface(HttpServletRequest request) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String inputLine;
			StringBuffer req = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				req.append(inputLine);
			}
			in.close();
			//print request
			logger.info(req.toString());
			
			XmlParaSet reqXml = XmlFormer.toXmlParaSet(req.toString().replaceAll("&", "&amp;"));
			
			String mode = reqXml.getAttr("name").substring(reqXml.getAttr("name").indexOf(".")+1).toLowerCase();
			
			String message = "";
			if(mode.equals("create")){
				message = "service created successfully";
			}else if(mode.equals("update")){
				message = "service updated successfully";
			}else{	//delete
				message = "service is deleted successfully";
			}
			
			String strXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
	                    "<message name=\"SERVICE."+mode.toUpperCase()+"\" type=\"RESPONSE\">\n" +
	                    "    <transaction id=\"" + getPara(reqXml, "transaction").getIntAttr("id") + "\">\n" +
	                    "        <agentKey>"+ getPara(reqXml, "transaction").getPara("agentKey").getValue() +"</agentKey>\n" +
	                    "        <result>\n" +
	                    "            <code>200</code>\n" +
	                    "            <message>"+message+"</message>\n" +
	                    "        </result>\n" +
	                    "    </transaction>\n" +
	                    "    <reply>\n" +
	                    "        <service>\n" +
	                    "            <"+mode+">\n" +
	                    "                <code>200</code>\n" +
	                    "                <message>"+message+"</message>\n" +
	                    "            </"+mode+">\n" +
	                    "        </service>\n" +
	                    "    </reply>\n" +
	                    "</message>";
			
			return new ResponseEntity<String>(strXml, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}		
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "B3interfaceUpdate.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> B3interfaceUpdate(HttpServletRequest request) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String inputLine;
			StringBuffer req = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				req.append(inputLine);
			}
			in.close();
			//print request
			logger.info(req.toString());
			
			XmlParaSet reqXml = XmlFormer.toXmlParaSet(req.toString().replaceAll("&", "&amp;"));
			
			String strXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<message xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" name=\"MOOD.REPORT\" type=\"RESPONSE\">\n" +
                    "    <transaction id=\"" + getPara(reqXml, "transaction").getIntAttr("id") + "\">\n" +
                    "        <agentKey>"+ getPara(reqXml, "transaction").getPara("agentKey").getValue() +"</agentKey>\n" +
                    "    </transaction>\n" +
                    "    <reply>\n" +
                    "        <service>\n" +
                    "            <update>\n" +
                    "                <code>200</code>\n" +
                    "                <message>service updated successfully</message>\n" +
                    "            </update>\n" +
                    "        </service>\n" +
                    "    </reply>\n" +
                    "</message>";
			
			return new ResponseEntity<String>(strXml, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}		
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> functionTemplate(HttpServletRequest request) {
		try {
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private String getServiceName(int service_type_id) {
		switch(service_type_id) {
		case SERVICE_TYPE_FILE_DOWNLOAD:
			return "fileDownload";
		case SERVICE_TYPE_STREAMING:
			return "streaming";
		case SERVICE_TYPE_CAROUSEL_MULTIPLEFILES:
			return "carousel-MultipleFiles";
		case SERVICE_TYPE_CAROUSEL_SINGLEFILE:
			return "carousel-SingleFile";
		default:
			return null;
		}
	}
	
	private int getServiceTypeId(String serviceName) {
		if(serviceName.equalsIgnoreCase("fileDownload")) return SERVICE_TYPE_FILE_DOWNLOAD;		
		if(serviceName.equalsIgnoreCase("streaming")) return SERVICE_TYPE_STREAMING;
		if(serviceName.equalsIgnoreCase("carousel-MultipleFiles")) return SERVICE_TYPE_CAROUSEL_MULTIPLEFILES;		
		if(serviceName.equalsIgnoreCase("carousel-SingleFile")) return SERVICE_TYPE_CAROUSEL_SINGLEFILE;
		
		return 0;
	}
	
	private String getServiceIdentifier(int serviceTypeId) {
		switch(serviceTypeId) {
		case SERVICE_TYPE_FILE_DOWNLOAD:
			return "urn:3gpp:filedownload:1";
		case SERVICE_TYPE_STREAMING:
			return "urn:3gpp:streaming:1";
		case SERVICE_TYPE_CAROUSEL_MULTIPLEFILES:
			return "urn:3gpp:carousel:multi:1";
		case SERVICE_TYPE_CAROUSEL_SINGLEFILE:
			return "urn:3gpp:carousel:single:1";
		default:
			return null;
		}
	}
	
	private String getServiceClass(int serviceTypeId) {
		switch(serviceTypeId) {
		case SERVICE_TYPE_FILE_DOWNLOAD:
			return "urn:3gpp:mbms:fd";
		case SERVICE_TYPE_STREAMING:
			return "urn:3gpp:mbms:ds";
		case SERVICE_TYPE_CAROUSEL_MULTIPLEFILES:
			return "urn:3gpp:mbms:cfc";
		case SERVICE_TYPE_CAROUSEL_SINGLEFILE:
			return "urn:3gpp:mbms:cfc";
		default:
			return null;
		}
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
	
	private XmlParaSet getPara(XmlParaSet set, String tag) throws Exception{
		XmlParaSet ret = (XmlParaSet) set.getPara(tag);
		if(ret == null || ret.size() == 0) throw new Exception("Not found tag: " + tag);
		return ret;
	}
}

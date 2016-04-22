package com.catenoid.dashbd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.util.ErrorCodes;
import com.catenoid.dashbd.dao.ServiceAreaEnbApMapper;
import com.catenoid.dashbd.dao.ServiceAreaScheduleMapper;
import com.catenoid.dashbd.dao.ServiceAreaMapper;
import com.catenoid.dashbd.dao.model.Bmsc;
import com.catenoid.dashbd.dao.model.BmscServiceArea;
import com.catenoid.dashbd.dao.model.BmscServiceAreaSearchParam;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.OperatorSearchParam;
import com.catenoid.dashbd.dao.model.ScheduleSummary;
import com.catenoid.dashbd.dao.model.ScheduleSummarySearchParam;
import com.catenoid.dashbd.dao.model.ServiceArea;
import com.catenoid.dashbd.dao.model.ServiceAreaCount;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbAp;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbApExample;
import com.catenoid.dashbd.dao.model.ServiceAreaEnbSearchParam;
import com.catenoid.dashbd.dao.model.ServiceAreaPermissionAp;
import com.catenoid.dashbd.dao.model.ServiceAreaSchedule;
import com.catenoid.dashbd.dao.model.ServiceAreaScheduleExample;
import com.catenoid.dashbd.dao.model.ServiceAreaSearchParam;
import com.catenoid.dashbd.dao.model.Users;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class SystemController {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	@Value("#{config['main.contents.max']}")
	private Integer contentMax;
		
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
			searchParam.put("start", offset);
			searchParam.put("end", offset + limit);
			
			List<ServiceAreaPermissionAp> datas = mapper.getPermissionList(searchParam);
			
			JSONArray rows = new JSONArray();
			for(int i = 0; i < datas.size(); i++) {
				ServiceAreaPermissionAp data = datas.get(i);
				JSONObject obj = new JSONObject();
				obj.put("permissionName", data.getPermissionName());
				obj.put("permissionCount", data.getPermissionCount());
				rows.add(obj);
				
				if( i == 0 ) {
					jsonResult.put("total", data.getTotalCount());
				}
			}
			
			jsonResult.put("rows", rows);

		} catch (Exception e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
}

package com.catenoid.dashbd.ctrl;

import java.io.IOException;
import java.util.Calendar;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catenoid.dashbd.dao.ContentsMapper;
import com.catenoid.dashbd.dao.ScheduleMapper;

@Controller
public class ContentMgmtController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping( value = "/view/getContents.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public Map< String, Object > getContents( @RequestParam Map< String, Object > params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
		//params.put("serviceId", "3048");
		List<Map> list = mapper.selectContents(params);
		
        
        Map< String, Object > returnMap = new HashMap< String, Object >();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", "SUCCESS");
        
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
        resultMap.put( "contents", list );
        
		return (Map<String, Object>) resultMap;
	}
}

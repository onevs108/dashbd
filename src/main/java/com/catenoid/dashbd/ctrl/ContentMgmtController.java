package com.catenoid.dashbd.ctrl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.ContentsImagesMapper;
import com.catenoid.dashbd.dao.ContentsMapper;
import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.ContentsImages;
import com.catenoid.dashbd.dao.model.FileDTO;
import com.catenoid.dashbd.dao.model.Operator;
import com.catenoid.dashbd.dao.model.Users;
import com.catenoid.dashbd.service.ContentService;
import com.catenoid.dashbd.service.UserService;

@Controller
public class ContentMgmtController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private ContentService contentServiceImpl;
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	@RequestMapping(value = "/view/content.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String getContentMgmt(
			@RequestParam(value = "isBack", required = false) Boolean isBack,
			ModelMap modelMap) {
		logger.info("-> [isBack = {}]", isBack);
	
		modelMap.addAttribute("isBack", isBack == null ? false : isBack);
		
//		List<Operator> operatorList = operatorServiceImpl.getOperatorListAll();
//		modelMap.addAttribute("operatorList", operatorList);
//		
//		logger.info("<- [operatorListSize = {}]", operatorList.size());
		return "cont/contentMgmt";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/content/list.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;")
	@ResponseBody
	public String postUserList(
			@RequestBody String body,
			HttpServletRequest request) {
		logger.info("-> [body = {}]", body);
		
		JSONObject jsonResult = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject requestJson = (JSONObject) jsonParser.parse(body);
			
			String searchOperatorId = (String) requestJson.get("searchOperatorId");
			String searchKeyword = (String) requestJson.get("searchKeyword");
			String searchColumn = (String) requestJson.get("searchColumn");
			String sort = (String) requestJson.get("sort");
			String order = (String) requestJson.get("order");
			long offset = (Long) requestJson.get("offset");
			long limit = (Long) requestJson.get("limit");
			
			HttpSession session = request.getSession(false);
			if (session != null) {
				Users user = (Users) session.getAttribute("USER");
				if (user != null) {
					Integer operatorId = searchOperatorId == null || searchOperatorId.isEmpty() ? null : Integer.parseInt(searchOperatorId);
					JSONArray rows = contentServiceImpl.getContentListToJsonArray(searchColumn, searchKeyword, operatorId, sort, order, offset, limit);
					jsonResult.put("rows", rows);
					int total = contentServiceImpl.getContentListCount(searchColumn, searchKeyword, operatorId);
					jsonResult.put("total", total);
					
					logger.info("<- [rows = {}], [total = {}]", rows.size(), total);
				}
			}
			else {
				logger.info("<- [Session is null!!]");
			}
		} catch (ParseException e) {
			logger.error("~~ [An error occurred!]", e);
		}
		return jsonResult.toString();
	}
	
	
	@RequestMapping( value = "/view/getContents.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public Map< String, Object > getContents( @RequestParam Map< String, Object > params,
	            HttpServletRequest req, Locale locale ) throws JsonGenerationException, JsonMappingException, IOException {
		
		ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
		//params.put("serviceId", "3048");
		String page = (String)params.get("page");
		Integer perPage = 5; 
		
		if (page == null || "".equals(page))
			page = "1";
		
		params.put("page", (Integer.parseInt(page) - 1) * perPage);
		params.put("perPage", perPage);
		
		List<Map> list = mapper.selectContents(params);
		
        
        Map< String, Object > returnMap = new HashMap< String, Object >();
        returnMap.put( "resultCode", "1000" );
        returnMap.put( "resultMsg", "SUCCESS");
        
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
        resultMap.put( "contents", list );
        
		return (Map<String, Object>) resultMap;
	}
	
	
	@RequestMapping( value = "/view/addContent.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView addContent( @RequestParam Map< String, Object > params,  HttpServletRequest req) throws UnsupportedEncodingException {
		logger.info("schdMgmtDetail {}", params);
		ModelAndView mv = new ModelAndView( "cont/addContent" );
		mv.addObject( "serviceAreaId", params.get("serviceAreaId")); 
		mv.addObject("searchDate", params.get("searchDate"));
		mv.addObject("title", params.get("title"));
		mv.addObject("category", params.get("category"));
		
		return mv;
	}
	
	@RequestMapping( value = "/view/addContentOK.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public  Map< String, Object >  addContentOK( HttpServletRequest request, @RequestParam Map< String, Object > params)  {
		Map< String, Object > returnMap = new HashMap< String, Object >();
		int res = 0;
		ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
	
		res = mapper.insertContent(params);
		logger.info("INSERT RESULT: " + res);
		
		try {
			fileUpload(request, params, "ThumbnailsFiles", "thumbnail");
			fileUpload(request, params, "preViewFiles", "preview");
			
		    returnMap.put( "resultCode", "1000" );
            returnMap.put( "resultMsg", "SUCCESS");
             
         } catch (Exception e) {
         	logger.error("",e);
         	returnMap.put( "resultCode", "2000" );
            returnMap.put( "resultMsg", "error=" + e.getMessage());
 		}
       
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
        return (Map<String, Object>) resultMap;
	}
	
	@RequestMapping( value = "/view/viewContent.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView viewContent ( @RequestParam Map< String, Object > params,  HttpServletRequest req) throws UnsupportedEncodingException {
		logger.info("schdMgmtDetail {}", params);
		ModelAndView mv = new ModelAndView( "cont/viewContent" );
		
		ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
		
		//@select db
		Map<String, String> mapContent = mapper.selectContent(params);
		mv.addObject( "mapContent", mapContent );
		
		//@select thumnail
		params.put("type", "thumbnail");
		List<Map> thumnails = mapper.selectContentImages(params);
		mv.addObject( "thumnails", thumnails );
		
		//@select preview
		params.put("type", "preview");
		List<Map> previews = mapper.selectContentImages(params);
		mv.addObject( "previews", previews );
		
		return mv;
	}
	
	@RequestMapping( value = "/view/editContent.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView editContent ( @RequestParam Map< String, Object > params,  HttpServletRequest req) throws UnsupportedEncodingException {
		logger.info("editContent {}", params);
		ModelAndView mv = new ModelAndView( "cont/editContent" );
		
		ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
		
		//@select db
		Map<String, String> mapContent = mapper.selectContent(params);
		mv.addObject( "mapContent", mapContent );
		
		//@select thumnail
		params.put("type", "thumbnail");
		List<Map> thumnails = mapper.selectContentImages(params);
		mv.addObject( "thumnails", thumnails );
		
		//@select preview
		params.put("type", "preview");
		List<Map> previews = mapper.selectContentImages(params);
		mv.addObject( "previews", previews );
		mv.addObject( "params", params );
		
		return mv;
	}
	
	
	@RequestMapping( value = "/view/editContentOK.do", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public  Map< String, Object >  editContentOK( HttpServletRequest request, @RequestParam Map< String, Object > params)  {
		Map< String, Object > returnMap = new HashMap< String, Object >();
		int res = 0;
		ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
	
		res = mapper.updateContent(params);
		logger.info("UPDARE RESULT: " + res);
		
		try {
			
			fileUpload(request, params, "ThumbnailsFiles", "thumbnail");
			fileUpload(request, params, "preViewFiles", "preview");
			
		    returnMap.put( "resultCode", "1000" );
            returnMap.put( "resultMsg", "SUCCESS");
             
         } catch (Exception e) {
         	logger.error("",e);
         	returnMap.put( "resultCode", "2000" );
            returnMap.put( "resultMsg", "error=" + e.getMessage());
 		}
       
        Map< String, Object > resultMap = new HashMap< String, Object >();
        resultMap.put( "resultInfo", returnMap );
        return (Map<String, Object>) resultMap;
	}
	
	
	
	
	@RequestMapping(value = "view/delContentImage.do")
	@ResponseBody
	public Map< String, Object > delContentImage( @RequestParam Map< String, String > params,
            HttpServletRequest req, Locale locale ) {
		
		logger.info("delContentImage params{}", params);
		try{
			ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);

			//@ delete  
			int ret = mapper.deleteContentImage(params);
			logger.info("deleteContentImage ret{}", ret);

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
	
	private void fileUpload(HttpServletRequest request, Map< String, Object > params,String fname, String typeName) throws Exception{
		MultipartRequest mReq = ( MultipartHttpServletRequest ) request;
		 // 홀로태그 이미지 업로드
        MultipartFile uploadfile = mReq.getFile( fname );
		
        if (uploadfile != null) {
            String fileName = uploadfile.getOriginalFilename();

           String storePath = null, rootPath = null;
			logger.info("OS: " + System.getProperty("os.name"));
			if(System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) {
				rootPath = "";
            	storePath = String.format("%s/%s/%s/", fileUploadPath, String.valueOf(params.get("id")), params.get("type"));
			}
			else {
				HttpSession session  = request.getSession();
            	rootPath = session.getServletContext().getRealPath("/");	
            	storePath = String.format("%s/%s/%s/", fileUploadPath, String.valueOf(params.get("id")), params.get("type"));
			}
			
        	logger.info("UploadDirectory : " + rootPath + storePath);
            File file = new File(rootPath + storePath);
            if(!file.exists() || !file.isDirectory()) {
            	if(!file.mkdirs()) {
            		throw new Exception("Make directory failed");
            	}
            }
            file = new File(rootPath + storePath + fileName);
            uploadfile.transferTo(file);
            
            /// contents_images 테이블에 insert
            ContentsImagesMapper ciMapper = sqlSession.getMapper(ContentsImagesMapper.class);
			ContentsImages ciRecord = new ContentsImages();
			long id = Long.parseLong((String)params.get("id"));
			ciRecord.setContentId((int)id);					
			ciRecord.setType(typeName);	
            ciRecord.setPath(storePath + fileName);
            
            int res = ciMapper.insertSelective(ciRecord);
            logger.debug("INSERT RES: " + res);
                
           
        } // if
	}
	
	
	private boolean valueCheck(String parameter) {
		// TODO Auto-generated method stub
		if(parameter == null || parameter.trim().equals("")) return false;
		return true;
	}
	
	private Map<String, Object> makeRetMsg(String code, String resStr) {
		
		Map< String, Object > returnMap = new HashMap< String, Object >();
	    returnMap.put( "resultCode", code );
	    returnMap.put( "resultMsg", resStr);
	      
	    Map< String, Object > resultMap = new HashMap< String, Object >();
	    resultMap.put( "resultInfo", returnMap );
	              
		return (Map<String, Object>) resultMap;
	}
}

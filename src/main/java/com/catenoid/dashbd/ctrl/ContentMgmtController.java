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
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

@Controller
public class ContentMgmtController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
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
		logger.info("schdMgmtDetail {}", params);
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
		
		return mv;
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
            	storePath = String.format("%s/%d/%s/", fileUploadPath, (Long)params.get("id"), params.get("type"));
			}
			else {
				HttpSession session  = request.getSession();
            	rootPath = session.getServletContext().getRealPath("/");	
            	storePath = String.format("%s/%d/%s/", fileUploadPath, (Long)params.get("id"), params.get("type"));
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
			long id = (Long)params.get("id");
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
}

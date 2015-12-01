package com.catenoid.dashbd;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.catenoid.dashbd.dao.ContentsImagesMapper;
import com.catenoid.dashbd.dao.ContentsMapper;
import com.catenoid.dashbd.dao.model.Contents;
import com.catenoid.dashbd.dao.model.ContentsImages;
import com.catenoid.dashbd.dao.model.ContentsImagesExample;
import com.catenoid.dashbd.dao.model.FileDTO;

@Controller
public class UploadController {
	private Logger logger = Logger.getLogger(UploadController.class);
	
	@Autowired
	private SqlSession sqlSession;	
	
	@Value("#{config['file.upload.path']}")
	private String fileUploadPath;
	
	private File uploadDir;
	
	public void setUploadDir(File uploadDir) {
		this.uploadDir = uploadDir;
	}
	
	@RequestMapping(value = "/file.do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public ModelAndView fileForm() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("fileForm");
		return mv;
	}
	
	@RequestMapping(value = "/file.do", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> fileSubmit(FileDTO dto, HttpServletRequest request) {
		JSONObject root = new JSONObject();
		
		if(!valueCheck(dto.getContentid())) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		if(!valueCheck(dto.getType())) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		if(dto.getUploadfile() == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		MultipartFile uploadfile = dto.getUploadfile();
        if (uploadfile != null) {
            String fileName = uploadfile.getOriginalFilename();
            dto.setFileName(fileName);
            try {    			
            	root.put("code", 0);
    			root.put("message", null);
    			
    			/// contents 테이블에서 content_id로 조회(없으면 에러)
    			ContentsMapper mapper = sqlSession.getMapper(ContentsMapper.class);
    			Contents record = mapper.selectByPrimaryKey(Integer.parseInt(dto.getContentid()));
    			if(record == null) {
    				logger.error("Contents not found, content id is " + dto.getContentid());
    				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    			}    			
                // 1. FileOutputStream 사용
                // byte[] fileData = file.getBytes();
                // FileOutputStream output = new FileOutputStream("C:/images/" + fileName);
                // output.write(fileData);
                 
                // 2. File 사용
    			String storePath = null, rootPath = null;
    			logger.info("OS: " + System.getProperty("os.name"));
    			if(System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) {
    				rootPath = "E:\\jee\\workspace2\\dashbd\\src\\main\\webapp\\";
                	storePath = String.format("%s/%d/%s/", fileUploadPath, Integer.parseInt(dto.getContentid()), dto.getType());
    			}
    			else {
    				HttpSession session  = request.getSession();
                	rootPath = session.getServletContext().getRealPath("/");	
                	storePath = String.format("%s/%d/%s/", fileUploadPath, Integer.parseInt(dto.getContentid()), dto.getType());
    			}
    			
            	logger.info("UploadDirectory : " + rootPath + storePath);
                File file = new File(rootPath + storePath);
                if(!file.exists() || !file.isDirectory()) {
                	if(!file.mkdirs()) {
                		root.put("code", 1);
            			root.put("message", "Make directory failed!!!(" + file.getPath() + ")");
                		return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
                	}
                }
                file = new File(rootPath + storePath + fileName);
                uploadfile.transferTo(file);
                
                /// contents_images 테이블에 insert
                ContentsImagesMapper ciMapper = sqlSession.getMapper(ContentsImagesMapper.class);
    			ContentsImages ciRecord = new ContentsImages();
    			ciRecord.setContentId(Integer.parseInt(dto.getContentid()));
    			ciRecord.setType(dto.getType());
                ciRecord.setPath(storePath + fileName);
                
                int res = ciMapper.insertSelective(ciRecord);
                logger.debug("INSERT RES: " + res);
                
                JSONObject obj = new JSONObject();
                obj.put("path", storePath + fileName);
                root.put("result", obj);
                
                return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                root.put("code", 0);
    			root.put("message", e.getMessage());
            } catch (Exception e) {
            	e.printStackTrace();
            	root.put("code", 0);
    			root.put("message", e.getMessage()); 
    		} // try - catch
        } // if
        
        return new ResponseEntity<String>(root.toJSONString(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "api/contents_images.do", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> readContentsImages(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		try {
			if(request.getParameter("request_type") == null || !request.getParameter("request_type").equalsIgnoreCase("read")) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			if(!valueCheck(request.getParameter("id"))) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			ContentsImagesMapper mapper = sqlSession.getMapper(ContentsImagesMapper.class);
			ContentsImagesExample exm = new ContentsImagesExample();
			exm.and().andContentIdEqualTo(Integer.parseInt(request.getParameter("id")));
			if(valueCheck(request.getParameter("type"))) exm.and().andTypeEqualTo(request.getParameter("type"));
			List<ContentsImages> images = mapper.selectByExample(exm);
			if(images == null || images.size() == 0) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			
			root.put("code", 1);
			root.put("message", null);
			if(images.size() == 1) {
				ContentsImages image = images.get(0);
				JSONObject obj = new JSONObject();
				obj.put("content_id", image.getContentId());
				obj.put("type", image.getType());
				obj.put("path", image.getPath());
				root.put("result", obj);
			}
			else {
				JSONArray array = new JSONArray();
				for(int i = 0; i < images.size(); i++){
					ContentsImages image = images.get(i);
					JSONObject obj = new JSONObject();
					obj.put("content_id", image.getContentId());
					obj.put("type", image.getType());
					obj.put("path", image.getPath());
					array.add(obj);
				}
				root.put("result", array);
			}
			
			return new ResponseEntity<String>(root.toJSONString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			root.put("code", 0);
			root.put("message", e.getMessage());
		}
		return new ResponseEntity<String>(root.toJSONString(), HttpStatus.INTERNAL_SERVER_ERROR);
	} 
	
	private boolean valueCheck(String parameter) {
		// TODO Auto-generated method stub
		if(parameter == null || parameter.trim().equals("")) return false;
		return true;
	}
}

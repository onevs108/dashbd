package com.catenoid.dashbd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.catenoid.dashbd.dao.StatusNotifyMapper;
import com.catenoid.dashbd.dao.model.StatusNotifyWithBLOBs;

import catenoid.net.msg.XmlPara;
import catenoid.net.msg.XmlParaSet;
import catenoid.net.msg.XmlTlv;
import catenoid.net.tools.XmlFormer;

/**
 * Handles requests for the application home page.
 */
@Controller
@PropertySource("classpath:/config.properties")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Resource
	private Environment env;
		
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String home(Locale locale, Model model) throws UnsupportedEncodingException {
		
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		System.out.println(formattedDate);
				
		return "home";
	}
	
	@RequestMapping(value = "B2interface", method = {RequestMethod.GET, RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> b2Interface(HttpServletRequest request) {
		try {
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(request.getInputStream()));
			String inputLine;
			StringBuffer req = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				req.append(inputLine);
			}
			in.close();			
			logger.info("REQUEST: " + req.toString());
			XmlParaSet reqXml = XmlFormer.toXmlParaSet(req.toString().replaceAll("&", "&amp;"));
			StatusNotifyMapper mapper = sqlSession.getMapper(StatusNotifyMapper.class);
			StatusNotifyWithBLOBs record = new StatusNotifyWithBLOBs();
			
			XmlParaSet trans = (XmlParaSet) reqXml.getPara("transaction");
			record.setTransactionId(trans.getIntAttr("id"));
			record.setAgentKey(trans.getValue("agentKey"));
			
			for(int i = 0; i < reqXml.size(); i++) {
				if(!reqXml.get(i).getTag().equalsIgnoreCase("parameters")) continue;
				XmlParaSet parameters = (XmlParaSet) reqXml.get(i);
				XmlParaSet notify = (XmlParaSet) parameters.getPara("notify");
				record.setCode(notify.getIntValue("code"));
				record.setMessage(notify.getValue("message"));
			
				XmlParaSet params = (XmlParaSet) notify.getPara("params");
				if(params.getPara("serviceId") != null) record.setServiceId(params.getIntValue("serviceId"));
				if(params.getPara("bmsc") != null) record.setBmscId(params.getIntValue("bmsc"));
				if(params.getPara("saillst") != null) record.setSaillst(params.getValue("saillst"));
				
				int res = mapper.insertSelective(record);
				logger.info("INSERT RESULT(" + (i + 1) + "): " + res);
			}
			
			XmlParaSet resXml = new XmlParaSet("message");
			resXml.addAttr(new XmlTlv("name")); resXml.setAttr("name", "STATUS.NOTIFY");
			resXml.addAttr(new XmlTlv("type")); resXml.setAttr("type", "RESPONSE");
			
			trans.delPara("agentKey");
			for(int i = 0; i < reqXml.size(); i++) {
				if(!reqXml.get(i).getTag().equalsIgnoreCase("parameters")) continue;
				XmlParaSet result = new XmlParaSet("result");
				result.addPara(new XmlPara("code")); result.addPara(new XmlPara("message"));
				XmlParaSet parameters = (XmlParaSet) reqXml.get(i);
				XmlParaSet notify = (XmlParaSet) parameters.getPara("notify");
				result.setValue("code", notify.getIntValue("code"));
				result.setValue("message", notify.getValue("message"));
				
				XmlParaSet params = (XmlParaSet) notify.getPara("params");				
				if(params != null) result.addPara(params);
				
				trans.addPara(result);
			}
			resXml.addPara(trans);
			
			return new ResponseEntity<String>(XmlFormer.toXmlString(resXml), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "/image/**", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> getImage(
			HttpServletRequest request) throws IOException {
		
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		
		System.out.println(env.getProperty("image.root"));
		System.out.println(restOfTheUrl);
		String imagePath = "D:/catenoid/kollus/samplevideos/17565_20151013-6whdtz7s_mobile1_kwchoffmpeg-mobile1-normal-20151013042655.mp4.144x108x1200.jpg";
		File file = new File(imagePath);
		FileInputStream in = new FileInputStream(file);
		
		final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_JPEG);
	    
	    return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
	}
}

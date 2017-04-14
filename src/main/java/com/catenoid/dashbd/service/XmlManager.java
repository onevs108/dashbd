package com.catenoid.dashbd.service;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.util.Base64Coder;
import com.catenoid.dashbd.util.HttpNetAgent;
import com.google.gson.Gson;

@Service
public class XmlManager {
	
	private static final Logger logger = LoggerFactory.getLogger(XmlManager.class);
	
	public final int BMSC_XML_RETRIEVE = 1;
	public final int BMSC_XML_CREATE = 2; 
	public final int BMSC_XML_UPDATE = 3;
	public final int BMSC_XML_DELETE = 4;
	public final String SERVICE_TYPE_FILE_DOWNLOAD = "fileDownload";
	public final String SERVICE_TYPE_STREAMING = "streaming";
	public final String SERVICE_TYPE_CAROUSEL_MULTIPLE = "carouselMultiple";
	public final String SERVICE_TYPE_CAROUSEL_SINGLE = "carouselSingle";
	
	@Value("#{config['b2.interface.url']}")
	private String b2InterfefaceURL;
	
	@Value("#{config['b3.interface.url']}")
	private String b3InterfefaceURL;
	
	@Value("#{config['b3.moodUsageDataReportInterval']}")
	private String moodUsageDataReportInterval;

	@Autowired
	private SqlSession sqlSession;

	public String[] sendBroadcast(Map<String, String> params, int mode){
		return sendBroadcast(params, mode, null, null);
	}
	
	public String[] sendBroadcast(Map<String, String> params, int mode, List<String> saidData, List<List<String>> paramList){
 		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
 		ScheduleMapper scheduleMapper = sqlSession.getMapper(ScheduleMapper.class);
		String[] rtvs = new String[2];
		String respBody = "SUCCESS";
		String reqBody = "";
		String reqBodyCrs = "";
		String respBodyCrs = "";
		String bmscIp = params.get("bmscIp");
		params.put("methodMode", String.valueOf(mode));
		String agentKey = Base64Coder.encode(bmscIp);
		params.put("agentKey", agentKey);
		
		try {
			//@set param to XML
			if (BMSC_XML_RETRIEVE == mode)
				reqBody = makeXmlRetrieve(params);
			else if (BMSC_XML_CREATE == mode || BMSC_XML_UPDATE == mode)
				reqBody = makeXmlCreate(params, mode, saidData, paramList);
			else
				reqBody = makeXmlDelete(params);
			
			respBody = new HttpNetAgent().execute("http://" + bmscIp + b2InterfefaceURL, "", reqBody, false);
			
			//@ xml send
			if("MooD".equals(params.get("serviceMode"))){
				List<HashMap<String, String>> crsList = new ArrayList<HashMap<String, String>>();
				if(BMSC_XML_DELETE == mode)
				{
					String[] saidList = params.get("said").split(",");
					for (int i = 0; i < saidList.length; i++) {
						String crsInfo = scheduleMapper.getCrsInfoFromMapping(saidList[i]);
						String[] crsInfoArray = crsInfo.split(",");
						HashMap<String, String> param = new HashMap<String, String>();
						param.put("id", crsInfoArray[0]);
						param.put("ip", crsInfoArray[1]);
						param.put("createUrl", crsInfoArray[2]);
						param.put("said", saidList[i]);
						crsList.add(param);
					}
				}
				else
				{
					for (int i = 0; i < paramList.get(6).size(); i++) {
						String crsInfo = scheduleMapper.getCrsInfoFromMapping(paramList.get(6).get(i)); //said
						String[] crsInfoArray = crsInfo.split(",");
						HashMap<String, String> param = new HashMap<String, String>();
						param.put("id", crsInfoArray[0]);
						param.put("ip", crsInfoArray[1]);
						param.put("createUrl", crsInfoArray[2]);
						param.put("said", paramList.get(6).get(i));
						crsList.add(param);
					}
				}
				HashMap<String, JSONArray> obj = new HashMap<String, JSONArray>();
				Gson gson = new Gson();
				JSONArray array = null;
				for (int i = 0; i < crsList.size(); i++) {
					String id = crsList.get(i).get("id");
					if(obj.get(id) == null){
						array = new JSONArray();
						array.put(new JSONObject(gson.toJson(crsList.get(i))));
					}
					else
					{	
						array = (JSONArray)obj.get(id);
						array.put(new JSONObject(gson.toJson(crsList.get(i))));
					}
					obj.put(id, array);
				}
				
				Iterator<String> it = obj.keySet().iterator();
				while(it.hasNext()) {
					String id = it.next();
					String crsIp = (String) ((JSONObject)obj.get(id).get(0)).get("ip");
					String crsUrl = (String) ((JSONObject)obj.get(id).get(0)).get("createUrl");
					String agentKeyCRS = Base64Coder.encode(crsIp);
					params.put("agentKeyCRS", agentKeyCRS);
					if(BMSC_XML_DELETE == mode){
						reqBodyCrs = makeXmlDeleteCRS(params, id);
						respBodyCrs = new HttpNetAgent().execute("http://" + crsIp + crsUrl, "", reqBodyCrs, false);
					}else{
						reqBodyCrs = makeXmlCreateCRS(params, mode, saidData, paramList, id, obj.get(id));
						respBodyCrs = new HttpNetAgent().execute("http://" + crsIp + crsUrl, "", reqBodyCrs, false);
					}
				}
				
				if(!isSuccessCRS(respBodyCrs)){
					String deleteStr = makeXmlDelete(params);
					respBody = new HttpNetAgent().execute("http://" + bmscIp + b2InterfefaceURL, "", deleteStr, false);
				}
			}
			
			logger.info("[returnXML=" + respBody + "]");
			if (BMSC_XML_RETRIEVE == mode)
				respBody = tmpRespRETRIEVE_Body();

			params.put("methodCode", "SUCCESS");
			params.put("methodMsg", "");			
			usersMapper.insertSystemInterFaceLog(params);
		} catch (Exception e) {
			params.put("methodCode", "Fail");
			params.put("methodMsg", e.getMessage());
			usersMapper.insertSystemInterFaceLog(params);
			System.out.println(params);
			logger.error("", e);
			respBody = e.getMessage();
		}
		rtvs[0] = respBody;
		rtvs[1] = reqBody;		
		return rtvs;
	}
	
	private String makeXmlDeleteCRS(Map<String, String> params, String id) {
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.DELETE"));
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);
		
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(params.get("agentKeyCRS")));		
		doc.getRootElement().addContent(transaction);
		
		
		Element request = new Element("request");
		Element service = new Element("service");
		Element delete = new Element("delete");
		delete.addContent(new Element("crsId").setText(id));		
		delete.addContent(new Element("timestamp").setText(convertDateFormat3(new Date().toString())));		
		delete.addContent(new Element("serviceId").setText(params.get("serviceId")));		
		service.addContent(delete);
		request.addContent(service);
		doc.getRootElement().addContent(request);
		return outString(doc);
	}

	private String makeXmlCreateCRS(Map<String, String> params, int mode, List<String> saidData, List<List<String>> paramList, String id, JSONArray bcSaidList) {
		Element message = new Element("message");
		String crsMode = "";
		if (BMSC_XML_CREATE == mode){
			crsMode = "create";
			message.setAttribute(new Attribute("name", "SERVICE.CREATE"));
		}else{
			crsMode = "update";	
			message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));	
		}
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(params.get("agentKeyCRS")));
		
		doc.getRootElement().addContent(transaction);

		Element request = new Element("request");
		Element service = new Element("service");
		
		String serviceId = params.get("serviceId");
		if (serviceId == null) {
			serviceId = "";
		}
		
		Element create = new Element(crsMode);
		
		create.addContent(new Element("crsid").setText(id));
		create.addContent(new Element("timestamp").setText(convertDateFormat3(new Date().toString())));
		create.addContent(new Element("serviceId").setText(serviceId));
		
		Element associatedDelivery = new Element("associatedDelivery");
		Element consumptionReport = null;
		consumptionReport = new Element("consumptionReport");
		consumptionReport.addContent(new Element("reportInterval").setText(params.get("moodReportInterval")));
		consumptionReport.addContent(new Element("moodUsageDataReportInterval").setText(moodUsageDataReportInterval));
		associatedDelivery.addContent(consumptionReport);
		
		Element schedule = null;
		for (int i = 0; i < paramList.get(0).size(); i++) {	//schedule start 갯수에 따라 동작
			schedule = new Element("schedule");
			schedule.addContent(new Element("index").setText(String.valueOf(i+1)));
			//time format ex) 2015-04-10T17:24:09.000+09:00 
			schedule.addContent(new Element("start").setText(convertDateFormatNew(paramList.get(0).get(i))));
			schedule.addContent(new Element("stop").setText(convertDateFormatNew(paramList.get(1).get(i))));
			Element contentSet = new Element("contentSet");
			Element serviceArea = new Element("serviceArea");
			for (int j = 0; j < bcSaidList.length(); j++) {
				serviceArea.addContent( new Element("said").setText(((JSONObject)bcSaidList.get(j)).get("said").toString()));
			}
			contentSet.addContent(serviceArea);
			create.addContent(schedule);
			create.addContent(contentSet);
			create.addContent(associatedDelivery);
		}
		
		service.addContent(create);
		request.addContent(service);
		
		doc.getRootElement().addContent(request);
		System.out.println("============================ Mood Create Start ============================");
		System.out.println(outString(doc));
		System.out.println("============================ Mood Create End ============================");
		return outString(doc);
	}

	public boolean isSuccess(String retStr) throws JDOMException, IOException{
		Document doc = null;
		doc = new SAXBuilder().build(new StringReader(retStr));
		Element message = doc.getRootElement();
		int resultCode = Integer.parseInt(message.getChild("transaction").getChild("result").getChild("code").getValue());
		
		if (resultCode == 1000 || resultCode == 200)
			return true;
		
		return false;
	}
	
	public boolean isSuccessCRS(String retStr) throws JDOMException, IOException{
		Document doc = null;
		doc = new SAXBuilder().build(new StringReader(retStr));
		Element message = doc.getRootElement();
		String mode = message.getAttributeValue("name").substring(message.getAttributeValue("name").indexOf(".")+1).toLowerCase();
		int resultCode = Integer.parseInt(message.getChild("reply").getChild("service").getChild(mode).getChild("code").getValue());
		
		if (resultCode == 1000 || resultCode == 200)
			return true;
		
		return false;
	}
	
	public Map<String, String> paringRetrieve(String strXmlBody) throws JDOMException, IOException{
		Map< String, String > xmlMap = new HashMap< String, String >();
		Document doc = null;
		doc = new SAXBuilder().build(new StringReader(strXmlBody));
		Element message = doc.getRootElement();
		Element service = message.getChild("parameters").getChild("services").getChild("service");
		String serviceTypeStr = service.getAttributeValue("serviceType");
		
		Element serviceType = null;
		if (SERVICE_TYPE_FILE_DOWNLOAD.equalsIgnoreCase(serviceTypeStr)){
			serviceType = service.getChild("fileDownload");
		}else{
			serviceType = service.getChild("streaming");
		}
			

		xmlMap.put("transactionId", message.getChild("transaction").getAttributeValue("id"));
		xmlMap.put("serviceId", serviceType.getAttributeValue("serviceId") );
		xmlMap.put("serviceType", serviceTypeStr);

		xmlMap.put("name", serviceType.getChildText("name"));
		xmlMap.put("serviceLanguage", serviceType.getChildText("serviceLanguage"));
		xmlMap.put("GBR", serviceType.getChild("transferConfig").getChild("QoS").getChildText("GBR"));
		xmlMap.put("QCI", serviceType.getChild("transferConfig").getChild("QoS").getChildText("QCI"));
//		xmlMap.put("level", serviceType);
		xmlMap.put("preEmptionCapabiity", serviceType.getChild("transferConfig").getChild("QoS").getChild("ARP").getChildText("preEmptionCapability"));
		xmlMap.put("preEmptionVulnerability", serviceType.getChild("transferConfig").getChild("QoS").getChild("ARP").getChildText("preEmptionVulnerability"));
		xmlMap.put("fecType", serviceType.getChild("transferConfig").getChild("FEC").getChildText("fecType"));
		xmlMap.put("fecRatio", serviceType.getChild("transferConfig").getChild("FEC").getChildText("fecRatio"));
		xmlMap.put("said", serviceType.getChild("serviceArea").getChildText("said"));	//fileDown만??
		xmlMap.put("schedule_start", serviceType.getChild("schedule").getAttributeValue("start"));
		xmlMap.put("schedule_stop", serviceType.getChild("schedule").getAttributeValue("stop"));
//		xmlMap.put("reportType", serviceType);
//		xmlMap.put("offsetTime", serviceType);
//		xmlMap.put("randomTime", serviceType);
		xmlMap.put("fileURI", serviceType.getChild("schedule").getChild("content").getChildText("fileURI"));
		xmlMap.put("deliveryInfo_start", xmlMap.put("fileURI", serviceType.getChild("schedule").getChild("content").getChild("deliveryInfo").getAttributeValue("start")));
		xmlMap.put("deliveryInfo_end", xmlMap.put("fileURI", serviceType.getChild("schedule").getChild("content").getChild("deliveryInfo").getAttributeValue("end")));
//		xmlMap.put("mpdURI", serviceType);
//		xmlMap.put("samplePercentage", serviceType);

		return xmlMap;
	}
	
	public String makeXmlRetrieve(Map<String, String> params){
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.RETRIEVE"));
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);
		
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(params.get("agentKey")));		//key ??
		
		doc.getRootElement().addContent(transaction);
		return outString(doc);
	}
	
	public String makeXmlDelete(Map<String, String> params){
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.ABORT"));
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);
		
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(params.get("agentKey")));		
		doc.getRootElement().addContent(transaction);
		
		
		Element parameters = new Element("parameters");
		Element services = new Element("serviceQuery");
		Element service = new Element("condition");
		service.addContent(new Element("serviceId").setText(params.get("serviceId")));		
		services.addContent(service);
		parameters.addContent(services);
		doc.getRootElement().addContent(parameters);
		return outString(doc);
	}
	
	public String makeXmlCreate(Map<String, String> params, int mode, List<String> saidData, List<List<String>> paramList){
		Element message = new Element("message");
		if (BMSC_XML_CREATE == mode){
			message.setAttribute(new Attribute("name", "SERVICE.CREATE"));
		}else{
			message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));	
		}
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(params.get("agentKey")));
		
		doc.getRootElement().addContent(transaction);

		Element parameters = new Element("parameters");
		Element services = new Element("services");
		Element service = new Element("service");
		
		Element name = new Element("name");
		name.setAttribute(new Attribute("lang", "en"));	
		name.setText(params.get("name"));
		Element serviceLanguage = new Element("serviceLanguage");
		serviceLanguage.setText(params.get("serviceLanguage"));
		
		Element transferConfig = new Element("transferConfig");
		Element QoS = new Element("QoS");
		QoS.addContent( new Element("GBR").setText(params.get("GBR")));
		QoS.addContent( new Element("QCI").setText(params.get("QCI")));
		
		Element ARP = new Element("ARP");
		ARP.addContent( new Element("level").setText(params.get("level")));
		String flag = "on".equals(params.get("preEmptionCapabiity"))? "0" : "1";
		ARP.addContent( new Element("preEmptionCapability").setText(flag));
		flag = "on".equals(params.get("preEmptionVulnerability"))? "0" : "1";
		ARP.addContent( new Element("preEmptionVulnerability").setText(flag));
		QoS.addContent(ARP);
		
		Element FEC = new Element("FEC");
		FEC.addContent( new Element("fecType").setText(params.get("fecType")));
		String ratio = "0";
		
		if (null != params.get("fecRatio") && !"".equals(params.get("fecRatio"))){
			ratio = params.get("fecRatio");
		}
		
		FEC.addContent( new Element("fecRatio").setText(ratio));

		transferConfig.addContent(QoS);
		transferConfig.addContent(FEC);
		//schedule 은 배열이 될수도 있음. 일딴 한개만 처리
		Element receptionReport = new Element("receptionReport");
		Element associatedDelivery = new Element("associatedDelivery");
		
		if ("on".equals(params.get("receptionReport"))){
			receptionReport.setAttribute(new Attribute("reportType", params.get("reportType")));
			receptionReport.setAttribute(new Attribute("cancelled", "false"));
			receptionReport.setAttribute(new Attribute("offsetTime", params.get("offsetTime")));
			receptionReport.setAttribute(new Attribute("randomTime", params.get("randomTime")));			
		}
		
		String serviceId = params.get("serviceId");
		if (serviceId == null) {
			serviceId = "";
		}
		
		Element serviceType = null;
		if (SERVICE_TYPE_FILE_DOWNLOAD.equals(params.get("serviceType")) 
				|| SERVICE_TYPE_CAROUSEL_MULTIPLE.equals(params.get("serviceType")) 
				|| SERVICE_TYPE_CAROUSEL_SINGLE.equals(params.get("serviceType"))) {
			
			Element fileDownload = null;
			if(SERVICE_TYPE_FILE_DOWNLOAD.equals(params.get("serviceType"))){
				service.setAttribute(new Attribute("serviceType", "fileDownload"));
				fileDownload = new Element("fileDownload");
			}else if(SERVICE_TYPE_CAROUSEL_MULTIPLE.equals(params.get("serviceType"))){
				service.setAttribute(new Attribute("serviceType", "carousel-MultipleFiles"));
				fileDownload = new Element("carousel-MultipleFiles");
			}else if(SERVICE_TYPE_CAROUSEL_SINGLE.equals(params.get("serviceType"))){
				service.setAttribute(new Attribute("serviceType", "carousel-SingleFile"));
				fileDownload = new Element("carousel-SingleFile");
			}
			
			fileDownload.setAttribute(new Attribute("serviceId", serviceId)); 
			
			if (null != params.get("serviceClass") && !"".equals(params.get("serviceClass"))){
				fileDownload.setAttribute(new Attribute("serviceClass", params.get("serviceClass")));
			}
			
			if (SERVICE_TYPE_CAROUSEL_MULTIPLE.equals(params.get("serviceType"))){
				fileDownload.setAttribute(new Attribute("retrieveInterval", params.get("retrieveInterval")));
			}
			
			Element serviceArea = new Element("serviceArea");
			String[] saidArray = paramList.get(6).get(0).split(",");
			for (int i = 0; i < saidArray.length; i++) {
				if(!saidArray[i].equals("")){
					serviceArea.addContent( new Element("said").setText(saidArray[i]));
				}
			}
			
			if ("on".equals(params.get("FileRepair"))){
				Element fileRepair= null; 
				fileRepair = new Element("postFileRepair");
				fileRepair.setAttribute(new Attribute("offsetTime", params.get("frOffsetTime")));
				fileRepair.setAttribute(new Attribute("randomTime", params.get("frRandomTime")));
				fileRepair.setAttribute(new Attribute("cancelled", "false"));
				associatedDelivery.addContent(fileRepair);
			}
			
			if ("on".equals(params.get("receptionReport"))){
				associatedDelivery.addContent(receptionReport);
			}
			
			fileDownload.addContent(name);
			fileDownload.addContent(serviceLanguage);
			fileDownload.addContent(transferConfig);
			fileDownload.addContent(serviceArea);
			
			serviceType = fileDownload;
		}
		else{ //streaming
			service.setAttribute(new Attribute("serviceType", "streaming"));
			
			Element streaming = new Element("streaming");
			streaming.setAttribute(new Attribute("serviceId", serviceId));
			if ( null != params.get("serviceClass") && !"".equals(params.get("serviceClass"))){
				streaming.setAttribute(new Attribute("serviceClass", params.get("serviceClass")));
			}
			streaming.setAttribute(new Attribute("serviceMode", params.get("serviceMode")));
			
			transferConfig.addContent(new Element("SegmentAvailableOffset").setText(params.get("segmentAvailableOffset")));
			
			receptionReport.setAttribute(new Attribute("samplePercentage", params.get("samplePercentage")));
			if("on".equals(params.get("receptionReport"))){
				associatedDelivery.addContent(receptionReport);
			}
			
			if ( null != params.get("name") && !"".equals(params.get("name")))
				streaming.addContent(name);
			
			if ( null != params.get("serviceLanguage") && !"".equals(params.get("serviceLanguage")))
				streaming.addContent(serviceLanguage);
		 	
			streaming.addContent(transferConfig);
			Element consumptionReport = null;
			if ("MooD".equals(params.get("serviceMode"))){
				consumptionReport = new Element("consumptionReport");
				consumptionReport.setAttribute(new Attribute("location", params.get("moodLocation")));
				if ("on".equals(params.get("reportClientId"))){
					consumptionReport.setAttribute(new Attribute("reportClientId", "true"));
					consumptionReport.setAttribute(new Attribute("reportInterval", params.get("moodReportInterval")));
					consumptionReport.setAttribute(new Attribute("offsetTime", params.get("moodOffsetTime")));
					consumptionReport.setAttribute(new Attribute("randomTime", params.get("moodRandomTimePeriod")));			
					consumptionReport.setAttribute(new Attribute("samplePercentage", params.get("moodSamplePercentage")));
				}else{
					consumptionReport.setAttribute(new Attribute("reportClientId", "false"));
				}
				associatedDelivery.addContent(consumptionReport);
			}
			
			serviceType = streaming;
		} 
		
		Element schedule = null;
		for (int i = 0; i < paramList.get(0).size(); i++) {		//schedule start 갯수에 따라 동작
			schedule = new Element("schedule");
			schedule.setAttribute(new Attribute("index", String.valueOf(i+1)));
			schedule.setAttribute(new Attribute("cancelled", "false"));
			//time format ex) 2015-04-10T17:24:09.000+09:00 
			schedule.setAttribute(new Attribute("start", convertDateFormatNew(paramList.get(0).get(i))));
			schedule.setAttribute(new Attribute("stop", convertDateFormatNew(paramList.get(1).get(i))));
			if(!SERVICE_TYPE_STREAMING.equals(params.get("serviceType"))){
				int contentLength = Integer.parseInt(paramList.get(5).get(i));
				for (int j = i*contentLength; j < contentLength; j++) {	//content 갯수에 따라 동작
					Element content = new Element("content");
					if(SERVICE_TYPE_CAROUSEL_SINGLE.equals(params.get("serviceType"))) {
						content.setAttribute(new Attribute("contentType", "text/plain"));
					}
					else if(SERVICE_TYPE_CAROUSEL_MULTIPLE.equals(params.get("serviceType")))
					{
						content.setAttribute(new Attribute("contentId", String.valueOf(j+1)));						
						content.setAttribute(new Attribute("contentType", "text/plain"));							
						content.setAttribute(new Attribute("cancelled", "false"));			
					}
					else
					{
						content.setAttribute(new Attribute("contentId", String.valueOf(j+1)));						
						content.setAttribute(new Attribute("contentType", "text/plain"));							
						content.setAttribute(new Attribute("cancelled", "false"));									
						content.setAttribute(new Attribute("changed", "false"));	
					}
					content.addContent(new Element("fileURI").setText(paramList.get(2).get(j)));
					Element deliveryInfo = new Element("deliveryInfo");
					//time format ex) 2015-04-10T17:24:09.000+09:00
					if(SERVICE_TYPE_FILE_DOWNLOAD.equals(params.get("serviceType"))) {
						deliveryInfo.setAttribute(new Attribute("start", convertDateFormatNew(paramList.get(3).get(j))));
						deliveryInfo.setAttribute(new Attribute("end", convertDateFormatNew(paramList.get(4).get(j))));
						content.addContent(deliveryInfo);
					}
					schedule.addContent(content);
				}
				serviceType.addContent(schedule);
				if ("on".equals(params.get("FileRepair")) || "on".equals(params.get("receptionReport"))){
					serviceType.addContent(associatedDelivery);
				}
			}
			else
			{
				Element contentSet = new Element("contentSet");
				contentSet.setAttribute(new Attribute("contentSetId", params.get("contentSetId"))); 					
				contentSet.setAttribute(new Attribute("cancelled", "false"));				
									
				Element serviceArea = new Element("serviceArea");
				for (int j = 0; j < paramList.get(6).size(); j++) {
					serviceArea.addContent( new Element("said").setText(paramList.get(6).get(j)));
				}
				
				Element mpd = new Element("mpd");
				mpd.setAttribute(new Attribute("changed", "false"));									
				mpd.addContent(new Element("mpdURI").setText(paramList.get(7).get(i)));
				
				Element mood = new Element("mood");
				Element r12MpdURI = new Element("r12MpdURI");
				r12MpdURI.setText(params.get("r12mpdURI"));
				r12MpdURI.setAttribute(new Attribute("changed", "false"));				
				mood.addContent(r12MpdURI);
				
				for (int k = 0; k < paramList.get(10).size(); k++) {
					mood.addContent(new Element("bcBasePattern").setText(paramList.get(10).get(k)));
				}
				
				if (BMSC_XML_UPDATE == mode){
					Element bcServiceArea = new Element("bcServiceArea");
					for (int j = 0; j < paramList.get(9).size(); j++) {
						bcServiceArea.addContent(new Element("said").setText(paramList.get(9).get(j)));
					}
					mood.addContent(bcServiceArea);
				}
				
				contentSet.addContent(serviceArea);
				contentSet.addContent(mpd);
				if (!("BC".equals(params.get("serviceMode")))){
					contentSet.addContent(mood);
				}
				serviceType.addContent(schedule);
				serviceType.addContent(contentSet);
				if ("MooD".equals(params.get("serviceMode")) || "on".equals(params.get("receptionReport"))){
					serviceType.addContent(associatedDelivery);
				} 
			}
		}
		
		service.addContent(serviceType);
		services.addContent(service);
		parameters.addContent(services);
		
		doc.getRootElement().addContent(parameters);
		System.out.println(outString(doc));
		return outString(doc);
//		return "NO";
	}
	
	public String testMaking(){

			Element message = new Element("message");
			Document doc = new Document(message);
			doc.setRootElement(message);

			Element transaction = new Element("transaction");
			transaction.setAttribute(new Attribute("id", "1"));
			transaction.addContent(new Element("agentKey").setText("agentKey"));
			
			doc.getRootElement().addContent(transaction);

			Element parameters = new Element("parameters");
			Element services = new Element("services");
			Element service = new Element("service");
			service.setAttribute(new Attribute("serviceType", "fileDownload"));
			
			Element fileDownload = new Element("fileDownload");
			fileDownload.setAttribute(new Attribute("serviceId", "urn:3gpp:filedownload-0410172238-1"));
			
			Element name = new Element("name");
			name.setAttribute(new Attribute("id", "1"));
			name.setText("test");
			Element serviceLanguage = new Element("serviceLanguage");
			serviceLanguage.setText("EN");
			
			Element transferConfig = new Element("transferConfig");
			Element QoS = new Element("QoS");
			QoS.addContent( new Element("GBR").setText("3000000"));
			QoS.addContent( new Element("QCI").setText("149"));
			
			Element ARP = new Element("ARP");
			ARP.addContent( new Element("level").setText("9"));
			ARP.addContent( new Element("preEmptionCapability").setText("0"));
			ARP.addContent( new Element("preEmptionVulnerability").setText("0"));
			QoS.addContent(ARP);
			
			Element FEC = new Element("FEC");
			FEC.addContent( new Element("fecType").setText("NoFEC"));
			FEC.addContent( new Element("fecRatio").setText("0"));

			transferConfig.addContent(QoS);
			transferConfig.addContent(FEC);
			
			Element serviceArea = new Element("serviceArea");
			serviceArea.addContent( new Element("said").setText("10000"));
			
			Element schedule = new Element("schedule");
			schedule.setAttribute(new Attribute("index", "1"));
			schedule.setAttribute(new Attribute("cancelled", "false"));
			schedule.setAttribute(new Attribute("start", "2015-04-10T17:24:09.000+09:00"));
			schedule.setAttribute(new Attribute("stop", "2015-04-10T17:39:09.000+09:00"));
			
			Element content = new Element("content");
			content.setAttribute(new Attribute("contentId", "1"));
			content.setAttribute(new Attribute("contentType", "text/plain"));
			content.setAttribute(new Attribute("cancelled", "false"));
			content.setAttribute(new Attribute("changed", "false"));
			content.addContent( new Element("fileURI").setText("http://192.168.1.115:8088/data/100M-RQ.txt"));
			Element deliveryInfo = new Element("deliveryInfo");
			deliveryInfo.setAttribute(new Attribute("start", "2015-04-10T17:24:09.000+09:00"));
			deliveryInfo.setAttribute(new Attribute("stop", "2015-04-10T17:39:09.000+09:00"));
			content.addContent(deliveryInfo);
			schedule.addContent(content);
			Element associatedDelivery = new Element("associatedDelivery");
			Element fileRepair = new Element("fileRepair");
			fileRepair.setAttribute(new Attribute("offsetTime", "5"));
			fileRepair.setAttribute(new Attribute("randomTime", "300"));
			fileRepair.setAttribute(new Attribute("cancelled", "false"));
			
			Element receptionReport = new Element("receptionReport");
			receptionReport.setAttribute(new Attribute("reportType", "RAck"));
			receptionReport.setAttribute(new Attribute("cancelled", "false"));
			receptionReport.setAttribute(new Attribute("offsetTime", "305"));
			receptionReport.setAttribute(new Attribute("randomTime", "300"));
			
			associatedDelivery.addContent(fileRepair);
			associatedDelivery.addContent(receptionReport);
			
			fileDownload.addContent(name);
			fileDownload.addContent(serviceLanguage);
			fileDownload.addContent(transferConfig);
			fileDownload.addContent(serviceArea);
			fileDownload.addContent(schedule);
			fileDownload.addContent(associatedDelivery);
			
			service.addContent(fileDownload);
			
			services.addContent(service);
			parameters.addContent(services);
			
			doc.getRootElement().addContent(parameters);
			return outString(doc);
	}
	
	private String outString(Document doc){
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString(doc);
	}
	

	private String convertDateFormat(String dateTime){
		
		if (dateTime == null)
			return null;
		
		String retStr = "";
		
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		//SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
//			sdfFrom.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date dateFrom = sdfFrom.parse(dateTime);
		    Calendar calFrom = Calendar.getInstance();
		    calFrom.setTime(dateFrom);
		    //calFrom.add(Calendar.HOUR, -9);
		    retStr = sdfTo.format(calFrom.getTime());
		    
		} catch (ParseException e) {
			logger.error("", e);
		}
				
		return retStr;
		
	}
	
	//Wed Mar 15 17:17:00 2017 --> 2017-02-27 16:00:00
	private String convertDateFormat3(String dateTime){
		Date dt = new Date();
		
		String retStr = "";
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");
		try {
			retStr = sdfTo.format(dt);
		} catch (Exception e) {
			logger.error("", e);
		}
		return retStr;
	}
	
	//2017-02-27T16:00:00.000+09:00
	private String convertDateFormatNew(String dateTime){
		String retStr = "";
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");
		try {
//			sdfFrom.setTimeZone(TimeZone.getTimeZone("IST"));
//			sdfTo.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date dateFrom = sdfFrom.parse(dateTime);
		    retStr = sdfTo.format(dateFrom);
		} catch (Exception e) {
			logger.error("", e);
		}
		return retStr;
	}
	
    public static String getFileDate(String format) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.add(Calendar.DATE ,0);
        return sdf.format(cal.getTime());
    }
    
    public String tmpRespRETRIEVE_Body(){
    	String retStr =
			  //  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			    "	<message name=\"SERVICE.RETRIEVE\" type=\"RESPONSE\">\n" + 
			    "	<transaction id=\"2\">\n" +
			    "	<result>\n" +
			    "		<code>100</code>\n" +
			    "		<message>OK</message>\n" +
			    "	</result>\n" +
			    "	</transaction>\n" +
			    "	<parameters>\n" +
			    "    <services>\n" +
			    "        <service serviceType=\"fileDownload\">\n" +
			    "            <fileDownload serviceId=\"ricky_urn:3gpp:download_multi150\">\n" +
			    "                <name lang=\"EN\">test</name>\n" +
			    "                <serviceLanguage>EN</serviceLanguage>\n" +
				"				<transferConfig>\n" +
			    "                   <QoS>\n" +
			    "                       <GBR>2048576</GBR>\n" +
			    "                       <QCI>1</QCI>\n" +
			    "                       <ARP>\n" +
			    "                           <preEmptionCapability>1</preEmptionCapability>\n" +
			    "                           <preEmptionVulnerability>1</preEmptionVulnerability>\n" +
			    "                       </ARP>\n" +
			    "                   </QoS>\n" +
			    "                   <FEC>\n" +
			    "                       <fecType>NoFEC</fecType>\n" +
			    "                       <fecRatio>0</fecRatio>\n" +
			    "                   </FEC>\n" +
			    "               </transferConfig>\n" +
			    "               <serviceArea>\n" +
			    "                   <said>1005</said>\n" +
			    "               </serviceArea>            \n" +      
			    "               <schedule index=\"1\" cancelled=\"false\" start=\"2015-04-09T10:48:27.000+09:00\" stop=\"2015-04-09T12:08:27.000+09:00\">\n" +
			    "                   <content contentId=\"1\" contentType=\"text/plain\" cancelled=\"false\" changed=\"false\">\n" +                            
			    "                    <fileURI>http://192.168.1.89:8088/data/100M-RQ.txt</fileURI>\n" +
			    "                       <deliveryInfo start=\"2015-04-09T10:48:27.000+09:00\" end=\"2015-04-09T11:08:27.000+09:00\"/>\n" +
			    "                   </content> \n" +
			    "                   <content contentId=\"2\" contentType=\"text/plain\" cancelled=\"false\" changed=\"false\">\n" +
			    "                       <fileURI>http://192.168.1.89:8088/data/100M-RQ.txt</fileURI>\n" +
			    "                       <deliveryInfo start=\"2015-04-09T11:58:27.000+09:00\" end=\"2015-04-09T12:08:27.000+09:00\"/>\n" +
			    "                   </content>\n" +
			    "               </schedule>\n" +
			    "               <schedule index=\"2\" cancelled=\"true\" start=\"2015-04-09T12:38:27.000+09:00\" stop=\"2015-04-09T12:58:27.000+09:00\">\n" +
			    "                   <content contentId=\"1\" contentType=\"text/plain\" cancelled=\"false\" changed=\"false\">                            \n" +
			    "                   <fileURI>http://192.168.1.89:8088/data/100M-RQ.txt</fileURI>\n" +
			    "                       <deliveryInfo start=\"2015-04-09T12:38:27.000+09:00\" end=\"2015-04-09T12:48:27.000+09:00\"/>\n" +
			    "                   </content> \n" +
			    "                   <content contentId=\"2\" contentType=\"text/plain\" cancelled=\"false\" changed=\"false\">\n" +
			    "                       <fileURI>http://192.168.1.89:8088/data/100M-RQ.txt</fileURI>\n" +
			    "                       <deliveryInfo start=\"2015-04-09T12:48:27.000+09:00\" end=\"2015-04-09T12:58:27.000+09:00\"/>\n" +
			    "                   </content>\n" +
			    "               </schedule>\n" +
			    "           </fileDownload>\n" +
			    "       </service>\n" +
			    "   </services>\n" +
			    "</parameters>\n" +
			    "</message>";
	
    	return retStr;
    }
	public static void main( String[] args ) {
		//System.out.println(new XmlManager().testMaking());

		//System.out.println(getFileDate("YYYYMMdd"));
		System.out.println(new XmlManager().convertDateFormat("20151223201100"));
		
		
		
		
//		String tt = "";
//		try {
//			tt = new XmlManager().tmpRespRETRIEVE_Body();
//			Map<String, String> mapRet = new XmlManager().paringRetrieve(tt);
//			System.out.println(mapRet);
//		} catch (JDOMException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(tt);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(tt);
//		}
		
	    
	}
}


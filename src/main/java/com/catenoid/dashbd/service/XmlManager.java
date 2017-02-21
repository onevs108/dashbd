package com.catenoid.dashbd.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.dao.ScheduleMapper;
import com.catenoid.dashbd.dao.UsersMapper;
import com.catenoid.dashbd.util.Base64Coder;
import com.catenoid.dashbd.util.HttpNetAgent;

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

	@Autowired
	private SqlSession sqlSession;
//	@Value("#{config['b2.server.ipaddress']}")
//	private String b2serverIpaddress;

	public String[] sendBroadcast(Map<String, String> params, int mode){
		return sendBroadcast(params, mode, null, null);
	}
	
	public String[] sendBroadcast(Map<String, String> params, int mode, List<String> saidData, List<List<String>> paramList){
		UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
		String[] rtvs = new String[2];
		String respBody = "SUCCESS";
		String reqBody = "";
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
			
			//@ xml send 
			respBody = new HttpNetAgent().execute("http://" + bmscIp + b2InterfefaceURL, "", reqBody, false);
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
			logger.error("", e);
			respBody = e.getMessage();
		}
		rtvs[0] = respBody;
		rtvs[1] = reqBody;		
		return rtvs;
	}
	
	public boolean isSuccess(String retStr) throws JDOMException, IOException{
		Document doc = null;
		doc = new SAXBuilder().build(new StringReader(retStr));
		Element message = doc.getRootElement();
		int resultCode = Integer.parseInt(message.getChild("transaction").getChild("result").getChild("code").getValue());
		
		if (resultCode == 100)
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
		}
		else{
			message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));	
		}
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		//common- depth one
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText(params.get("agentKey")));		//key ??
		
		doc.getRootElement().addContent(transaction);

		Element parameters = new Element("parameters");
		Element services = new Element("services");
		Element service = new Element("service");
		
		//common- depth five		
		
		Element name = new Element("name");
		name.setAttribute(new Attribute("lang", "en"));										//??
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
		Element receptionReport = null;
		Element associatedDelivery = new Element("associatedDelivery");
		
		if ("on".equals(params.get("receptionReport"))){
			receptionReport = new Element("receptionReport");
			receptionReport.setAttribute(new Attribute("reportType", params.get("reportType")));
			receptionReport.setAttribute(new Attribute("cancelled", "false"));
			receptionReport.setAttribute(new Attribute("offsetTime", params.get("offsetTime")));
			receptionReport.setAttribute(new Attribute("randomTime", params.get("randomTime")));			
		}
		
		String serviceId = params.get("serviceId");
		if (serviceId == null){
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
			serviceArea.addContent( new Element("said").setText(params.get("said")));
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
			
			if ("on".equals(params.get("FileRepair")) || "on".equals(params.get("receptionReport"))){
				fileDownload.addContent(associatedDelivery);
			}
			serviceType = fileDownload;
		}
		else{ //streaming
			service.setAttribute(new Attribute("serviceType", "streaming"));
			Element streaming = new Element("streaming");
			streaming.setAttribute(new Attribute("serviceId", serviceId));

			if ( null != params.get("serviceClass") && !"".equals(params.get("serviceClass"))){
				streaming.setAttribute(new Attribute("serviceClass", params.get("serviceClass")));
			}
			
			transferConfig.addContent(new Element("SegmentAvailableOffset").setText(params.get("segmentAvailableOffset")));
			
			
			if ("on".equals(params.get("reportType"))){
				receptionReport.setAttribute(new Attribute("samplePercentage", params.get("samplePercentage")));
				associatedDelivery.addContent(receptionReport);
			}
			
			if ( null != params.get("name") && !"".equals(params.get("name")))
				streaming.addContent(name);
			
			if ( null != params.get("serviceLanguage") && !"".equals(params.get("serviceLanguage")))
				streaming.addContent(serviceLanguage);
			
			streaming.addContent(transferConfig);
			
			if ("on".equals(params.get("FileRepair")) || "on".equals(params.get("receptionReport")))
				streaming.addContent(associatedDelivery);
			
			serviceType = streaming;
		}
		
		Element schedule = null;
		for (int i = 0; i < paramList.get(0).size(); i++) {	//schedule start 갯수에 따라 동작
			schedule = new Element("schedule");
			schedule.setAttribute(new Attribute("index", String.valueOf(i+1)));
			schedule.setAttribute(new Attribute("cancelled", "false"));
			//time format ex) 2015-04-10T17:24:09.000+09:00 
			schedule.setAttribute(new Attribute("start", convertDateFormat(paramList.get(0).get(i))));
			schedule.setAttribute(new Attribute("stop", convertDateFormat(paramList.get(1).get(i))));
			if(!SERVICE_TYPE_STREAMING.equals(params.get("serviceType"))){
				for (int j = 0; j < Integer.parseInt(paramList.get(5).get(i)); j++) {	//fileURI 갯수에 따라 동작
					Element content = new Element("content");
					content.setAttribute(new Attribute("contentId", String.valueOf(j+1)));						//??
					content.setAttribute(new Attribute("contentType", "text/plain"));							//??
					content.setAttribute(new Attribute("cancelled", "false"));									//??
					content.setAttribute(new Attribute("changed", "false"));				
					content.addContent( new Element("fileURI").setText(paramList.get(2).get(i)));
					Element deliveryInfo = new Element("deliveryInfo");
					//time format ex) 2015-04-10T17:24:09.000+09:00
					ScheduleMapper scheduleMapper = sqlSession.getMapper(ScheduleMapper.class);
//					scheduleMapper.insertContents();
					if(SERVICE_TYPE_FILE_DOWNLOAD.equals(params.get("serviceType"))) {
						deliveryInfo.setAttribute(new Attribute("start", convertDateFormat(paramList.get(3).get(i))));
						deliveryInfo.setAttribute(new Attribute("end", convertDateFormat(paramList.get(4).get(i))));
						content.addContent(deliveryInfo);
					}
					schedule.addContent(content);
				}
				serviceType.addContent(schedule);
			}
			else
			{
				Element contentSet = new Element("contentSet");
				contentSet.setAttribute(new Attribute("contentSetId", String.valueOf(i+1))); 					
				contentSet.setAttribute(new Attribute("cancelled", "false"));				
									
				Element serviceArea = new Element("serviceArea");
				
				String[] saidArray = paramList.get(6).get(i).split(",");
				for (int j = 0; j < saidArray.length; j++) {
					if(!saidArray[i].equals("")){
						serviceArea.addContent( new Element("said").setText(saidArray[i]));
					}
				}
				serviceArea.addContent( new Element("said").setText(params.get("said")));
				Element mpd = new Element("mpd");
				mpd.setAttribute(new Attribute("changed", "false"));									
				mpd.addContent(new Element("mpdURI").setText(paramList.get(7).get(i)));
				
				contentSet.addContent(serviceArea);
				contentSet.addContent(mpd);
				serviceType.addContent(schedule);
				serviceType.addContent(contentSet);
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
			//sdfFrom.setTimeZone(TimeZone.getTimeZone("GMT"));
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

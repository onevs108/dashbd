package com.catenoid.dashbd.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.catenoid.dashbd.ctrl.ScheduleMgmtController;
import com.catenoid.dashbd.util.HttpNetAgent;
import com.catenoid.dashbd.util.HttpNetAgentException;

@Service
public class XmlManager {
	
	private static final Logger logger = LoggerFactory.getLogger(XmlManager.class);
	
	public final int BMSC_XML_RETRIEVE = 1;
	public final int BMSC_XML_CREATE = 2; 
	public final int BMSC_XML_UPDATE = 3;
	public final int BMSC_XML_DELETE = 4;
	public final String SERVICE_TYPE_FILE_DOWNLOAD = "FileDownload";
	
	
	@Value("#{config['b2.interface.url']}")
	private String b2InterfefaceURL;
	
	public String sendBroadcast(Map params, int mode){
		String respBody = "SUCCESS";
		String reqBody = "";
		try {
			//@set param to XML
			if (BMSC_XML_RETRIEVE == mode)
				reqBody= makeXmlRetrieve(params);
			else if (BMSC_XML_CREATE == mode || BMSC_XML_UPDATE == mode)
				reqBody= makeXmlCreate(params, mode);
			else
				reqBody= makeXmlDelete(params);
			
			//@ xml send 
			respBody = new HttpNetAgent().execute(b2InterfefaceURL, "", reqBody, false);
		
			
		} catch (Exception e) {
			logger.error("", e);
			respBody = e.getMessage();
		}
		
		return respBody;
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
		if (SERVICE_TYPE_FILE_DOWNLOAD.equals(serviceTypeStr)){
			serviceType = service.getChild("fileDownload");
		}else{
			serviceType = service.getChild("streaming");
		}
			

		xmlMap.put("transactionId", message.getChild("transaction").getAttributeValue("id"));
		xmlMap.put("serviceId", serviceType.getAttributeValue("id") );
		xmlMap.put("serviceType", serviceTypeStr);

		xmlMap.put("name", serviceType.getChildText("name"));
		xmlMap.put("serviceLanguage", serviceType.getChildText("serviceLanguage"));
		xmlMap.put("GBR", serviceType.getChild("QoS").getChildText("GBR"));
		xmlMap.put("QCI", serviceType.getChild("QoS").getChildText("QCI"));
//		xmlMap.put("level", serviceType);
		xmlMap.put("preEmptionCapabiity", serviceType.getChild("QoS").getChild("ARP").getChildText("preEmptionCapability"));
		xmlMap.put("preEmptionVulnerability", serviceType.getChild("QoS").getChild("ARP").getChildText("preEmptionVulnerability"));
		xmlMap.put("fecType", serviceType.getChild("FEC").getChildText("fecType"));
		xmlMap.put("fecRatio", serviceType.getChild("FEC").getChildText("fecRatio"));
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
		transaction.addContent(new Element("agentKey").setText("dGVzdA=="));		//key ??
		
		doc.getRootElement().addContent(transaction);
		return outString(doc);
	}
	
	public String makeXmlDelete(Map<String, String> params){
		Element message = new Element("message");
		message.setAttribute(new Attribute("name", "SERVICE.DELETE"));
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);
		
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText("dGVzdA=="));		
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
	
	public String makeXmlCreate(Map<String, String> params, int mode){
		Element message = new Element("message");
		if (BMSC_XML_CREATE == mode)
			message.setAttribute(new Attribute("name", "SERVICE.CREATE"));
		else
			message.setAttribute(new Attribute("name", "SERVICE.UPDATE"));
		
		message.setAttribute(new Attribute("type", "REQUEST"));
		Document doc = new Document(message);
		doc.setRootElement(message);

		//common- depth one
		Element transaction = new Element("transaction");
		transaction.setAttribute(new Attribute("id", params.get("transactionId")));
		transaction.addContent(new Element("agentKey").setText("dGVzdA=="));		//key ??
		
		doc.getRootElement().addContent(transaction);

		Element parameters = new Element("parameters");
		Element services = new Element("services");
		Element service = new Element("service");
		
		//common- depth five		
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
		FEC.addContent( new Element("fecRatio").setText(params.get("fecRatio")));

		transferConfig.addContent(QoS);
		transferConfig.addContent(FEC);
		//schedule 은 배열이 될수도 있음. 일딴 한개만 처리
		Element schedule = new Element("schedule");
		schedule.setAttribute(new Attribute("index", "1"));
		schedule.setAttribute(new Attribute("cancelled", "false"));
		//time format ex) 2015-04-10T17:24:09.000+09:00
		schedule.setAttribute(new Attribute("start", params.get("schedule_start")));
		schedule.setAttribute(new Attribute("stop", params.get("schedule_stop")));
		
		Element associatedDelivery = new Element("associatedDelivery");
		Element receptionReport = new Element("receptionReport");
		receptionReport.setAttribute(new Attribute("reportType", params.get("reportType")));
		receptionReport.setAttribute(new Attribute("cancelled", "false"));
		receptionReport.setAttribute(new Attribute("offsetTime", params.get("offsetTime")));
		receptionReport.setAttribute(new Attribute("randomTime", params.get("randomTime")));
		
		if (SERVICE_TYPE_FILE_DOWNLOAD.equals(params.get("serviceType"))){
			service.setAttribute(new Attribute("serviceType", "fileDownload"));
			Element fileDownload = new Element("fileDownload");
			fileDownload.setAttribute(new Attribute("serviceId", params.get("serviceId"))); 
			Element name = new Element("name");
			name.setAttribute(new Attribute("id", "1"));										//??
			name.setText(params.get(""));
			Element serviceLanguage = new Element("serviceLanguage");
			serviceLanguage.setText(params.get("serviceLanguage"));
			
			Element serviceArea = new Element("serviceArea");
			serviceArea.addContent( new Element("said").setText(params.get("said")));
			
			Element content = new Element("content");
			content.setAttribute(new Attribute("contentId", "1"));						//??
			content.setAttribute(new Attribute("contentType", "text/plain"));			//??
			content.setAttribute(new Attribute("cancelled", "false"));					//??
			content.setAttribute(new Attribute("changed", "false"));				
			content.addContent( new Element("fileURI").setText(params.get("fileURI")));
			Element deliveryInfo = new Element("deliveryInfo");
			//time format ex) 2015-04-10T17:24:09.000+09:00
			deliveryInfo.setAttribute(new Attribute("start", params.get("deliveryInfo_start")));
			deliveryInfo.setAttribute(new Attribute("end", params.get("deliveryInfo_end")));
			content.addContent(deliveryInfo);
			schedule.addContent(content);
			
			Element postFileRepair = new Element("postFileRepair");
			postFileRepair.setAttribute(new Attribute("offsetTime", params.get("offsetTime")));
			postFileRepair.setAttribute(new Attribute("randomTime", params.get("randomTime")));
			postFileRepair.setAttribute(new Attribute("cancelled", "false"));
			
			associatedDelivery.addContent(postFileRepair);
			associatedDelivery.addContent(receptionReport);
			
			fileDownload.addContent(name);
			fileDownload.addContent(serviceLanguage);
			fileDownload.addContent(transferConfig);
			fileDownload.addContent(serviceArea);
			fileDownload.addContent(schedule);
			fileDownload.addContent(associatedDelivery);
			
			service.addContent(fileDownload);
		}
		else{
			service.setAttribute(new Attribute("serviceType", "streaming"));
			Element streaming = new Element("streaming");
			streaming.setAttribute(new Attribute("serviceId", params.get("serviceId"))); 
			streaming.setAttribute(new Attribute("serviceClass", "urn:3gpp:mbms:ds"));				
			
			transferConfig.addContent(new Element("SegmentAvailableOffset").setText(params.get("SegmentAvailableOffset")));
			
			Element contentSet = new Element("contentSet");
			contentSet.setAttribute(new Attribute("contentSetId", "1")); 					
			contentSet.setAttribute(new Attribute("cancelled", "false"));				
								
			Element serviceArea = new Element("serviceArea");
			serviceArea.addContent(new Element("said").setText(params.get("said")));
			
			Element mpd = new Element("mpd");
			mpd.setAttribute(new Attribute("changed", "false"));									
			mpd.addContent(new Element("mpdURI").setText(params.get("mpdURI")));
			
			contentSet.addContent(serviceArea);
			contentSet.addContent(mpd);
			
			receptionReport.setAttribute(new Attribute("samplePercentage", params.get("samplePercentage")));
			associatedDelivery.addContent(receptionReport);
			
			streaming.addContent(transferConfig);
			streaming.addContent(schedule);
			streaming.addContent(contentSet);
			streaming.addContent(associatedDelivery);
			service.addContent(streaming);
		}
		
		services.addContent(service);
		parameters.addContent(services);
		
		doc.getRootElement().addContent(parameters);
		return outString(doc);
	}
	
	public String testMaking(){

			Element message = new Element("message");
			Document doc = new Document(message);
			doc.setRootElement(message);

			Element transaction = new Element("transaction");
			transaction.setAttribute(new Attribute("id", "1"));
			transaction.addContent(new Element("agentKey").setText("dGVzdA=="));
			
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
			Element postFileRepair = new Element("postFileRepair");
			postFileRepair.setAttribute(new Attribute("offsetTime", "5"));
			postFileRepair.setAttribute(new Attribute("randomTime", "300"));
			postFileRepair.setAttribute(new Attribute("cancelled", "false"));
			
			Element receptionReport = new Element("receptionReport");
			receptionReport.setAttribute(new Attribute("reportType", "RAck"));
			receptionReport.setAttribute(new Attribute("cancelled", "false"));
			receptionReport.setAttribute(new Attribute("offsetTime", "305"));
			receptionReport.setAttribute(new Attribute("randomTime", "300"));
			
			associatedDelivery.addContent(postFileRepair);
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
	
	public static void main( String[] args ) {
		System.out.println(new XmlManager().testMaking());
	}
}
